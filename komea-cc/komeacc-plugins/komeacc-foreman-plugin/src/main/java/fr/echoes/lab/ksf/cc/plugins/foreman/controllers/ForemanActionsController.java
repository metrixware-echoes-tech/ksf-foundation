package fr.echoes.lab.ksf.cc.plugins.foreman.controllers;

import java.io.IOException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanapi.model.Host;
import fr.echoes.lab.foremanclient.ForemanClient;
import fr.echoes.lab.foremanclient.ForemanHelper;
import fr.echoes.lab.ksf.cc.plugins.foreman.dao.IForemanEnvironmentDAO;
import fr.echoes.lab.ksf.cc.plugins.foreman.dao.IForemanTargetDAO;
import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanEnvironnment;
import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanTarget;
import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanErrorHandlingService;
import fr.echoes.lab.puppet.PuppetClient;
import fr.echoes.lab.puppet.PuppetException;

@Controller
public class ForemanActionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForemanActionsController.class);

    @Value("${ksf.foreman.url}")
    private String url;

    @Value("${ksf.foreman.username}")
    private String username;

    @Value("${ksf.foreman.password}")
    private String password;

    @Value("${ksf.foreman.host.smartProxyId}")
    private String smartProxyId;

    @Value("${ksf.foreman.host.computeResourceId}")
    private String computeResourceId;

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private IForemanEnvironmentDAO environmentDAO;

    @Autowired
    private IForemanTargetDAO targetDAO;

    @Autowired
    private ForemanErrorHandlingService errorHandler;

    @RequestMapping(value = "/ui/foreman/environment/new", method = RequestMethod.POST)
    public String createEnvironment(@RequestParam("projectId") String projectId, @RequestParam("name") String name, @RequestParam("configuration") String configuration) {

        final Project project = this.projectDAO.findOne(projectId);

        final ForemanEnvironnment env = new ForemanEnvironnment();
        env.setName(name);
        env.setConfiguration(configuration);
        env.setProjectId(projectId);

        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<ForemanEnvironnment>> errors = validator.validate(env);

        if (errors.size() == 0) {
            final boolean success = createEnvironment(name, configuration);
            if (success) {
                this.environmentDAO.save(env);
            }

        } else {
            LOGGER.error("Failed to create environment {}", errors);
            this.errorHandler.registerError("Invalid data provided. Failed to create environment.");
        }

        return "redirect:/ui/projects/" + project.getKey();
    }

    private boolean createEnvironment(String envName, String configuration) {
        final ObjectMapper mapper = new ObjectMapper();
        boolean success = true;
        try {
            final JsonNode rootNode = mapper.readTree(configuration);
            final JsonNode modulesNode = rootNode.get("modules");
            if (modulesNode.isArray()) {
                final PuppetClient puppetClient = new PuppetClient();
                for (final JsonNode moduleNode : modulesNode) {
                    final String moduleName = moduleNode.path("name").asText();
                    final String moduleVersion = moduleNode.path("version").asText();
                    try {
                        puppetClient.installModule(moduleName, moduleVersion, envName);
                    } catch (final PuppetException e) {
                        success = false;
                        LOGGER.error("Failed to create environment {} : {}", envName, e);
                        this.errorHandler.registerError("Failed to create Puppet environment.");
                    }
                }
            }
        } catch (final IOException e) {
            success = false;
            LOGGER.error("Failed to create environment " + envName, e);
            this.errorHandler.registerError("Failed to create Puppet environment. Error parsing configuration file.");
        }
        try {
            ForemanHelper.importPuppetClasses(this.url, this.username, this.password, this.smartProxyId);
        } catch (final Exception e) {
            success = false;
            LOGGER.error("[foreman] Failed to import puppet classes.");
            this.errorHandler.registerError("Failed to import Puppet classes.");
        }
        return success;
    }

    @RequestMapping(value = "/ui/foreman/targets/new", method = RequestMethod.POST)
    public String createTarget(@RequestParam("projectId") String projectId, @RequestParam("name") String name, @RequestParam("environment") String env, @RequestParam("operatingsystem") String operatingsystem, @RequestParam("computeprofiles") String computeprofiles, @RequestParam("puppetConfiguration") String puppetConfiguration) {
        final Project project = this.projectDAO.findOne(projectId);

        final ForemanTarget foremanTarget = new ForemanTarget();
        foremanTarget.setProject(project);
        foremanTarget.setName(name);
        foremanTarget.setComputeProfile(computeprofiles);
        foremanTarget.setPuppetConfiguration(puppetConfiguration);

        if (!StringUtils.isEmpty(operatingsystem)) {
            final String[] os = StringUtils.split(operatingsystem, '-');
            foremanTarget.setOperatingSystemId(os[0]);
            foremanTarget.setOperatingSystemName(os[1]);
        }

        final ForemanEnvironnment environment = this.environmentDAO.findOne(env);
        foremanTarget.setEnvironment(environment);

        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<ForemanTarget>> errors = validator.validate(foremanTarget);

        if (errors.size() == 0) {
            this.targetDAO.save(foremanTarget);
        } else {
            LOGGER.error("[foreman] Failed to create target : {}", errors);
            this.errorHandler.registerError("Invalid data provided. Failed to create target.");
        }

        return "redirect:/ui/projects/" + project.getKey();
    }

	@RequestMapping(value = "/ui/foreman/targets/instantiate", method = RequestMethod.POST)
	public String instantiateTarget(@RequestParam("projectId") String projectId, @RequestParam("hostName") String hostName, @RequestParam("targetId") String targetId) {

		final Project project = this.projectDAO.findOne(projectId);

		final ForemanTarget target = this.targetDAO.findOne(targetId);

		final String name = target.getName();
		final ForemanEnvironnment environment = target.getEnvironment();

		final String puppetConfiguration = target.getPuppetConfiguration();
		LOGGER.info("[puppet] conf: {}", puppetConfiguration);

		try {
			final Host host = ForemanHelper.createHost(this.url, this.username, this.password, name, "1", "1", project.getName(), environment.getName(), Integer.parseInt(target.getOperationSystemId()), 1, target.getPuppetConfiguration());
			final IForemanApi api = ForemanClient.createApi(this.url, this.username, this.password);
			api.hostPower(host.id, "start");
		} catch (final Exception e) {
			LOGGER.error("[foreman] Host creation failed : {}", puppetConfiguration);
			this.errorHandler.registerError("Failed to instantiate target. Please verify your Foreman configuration.");
		}


		return "redirect:/ui/projects/"+project.getKey();
	}
}
