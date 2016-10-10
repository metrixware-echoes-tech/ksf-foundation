package fr.echoes.labs.ksf.cc.plugins.foreman.controllers;

import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import fr.echoes.labs.foremanapi.IForemanApi;
import fr.echoes.labs.foremanapi.model.Host;
import fr.echoes.labs.foremanapi.model.Image;
import fr.echoes.labs.foremanclient.IForemanService;
import fr.echoes.labs.ksf.cc.plugins.foreman.dao.IForemanEnvironmentDAO;
import fr.echoes.labs.ksf.cc.plugins.foreman.dao.IForemanTargetDAO;
import fr.echoes.labs.ksf.cc.plugins.foreman.exceptions.ForemanHostAlreadyExistException;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanEnvironnment;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanHostDescriptor;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanTarget;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanClientFactory;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanErrorHandlingService;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanHostDescriptorFactory;
import fr.echoes.labs.puppet.PuppetClient;
import fr.echoes.labs.puppet.PuppetException;
import java.io.IOException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForemanActionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForemanActionsController.class);

    @Autowired
    private ForemanConfigurationService configurationService;

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private IForemanEnvironmentDAO environmentDAO;

    @Autowired
    private IForemanTargetDAO targetDAO;

    @Autowired
    private ForemanErrorHandlingService errorHandler;

    @Autowired
    private IForemanService foremanService;

    @Autowired
    private ForemanClientFactory foremanClientFactory;

    @Autowired
    private ForemanHostDescriptorFactory hostDescriptorFactory;

    @RequestMapping(value = "/ui/foreman/environment/delete", method = RequestMethod.POST)
    public String deleteEnvironment(@RequestParam("projectId") String projectId, @RequestParam("name") String name) {
        final Project project = this.projectDAO.findOne(projectId);
        final ForemanEnvironnment environnment = this.environmentDAO.findByName(name);
        if (environnment != null) {
            this.environmentDAO.delete(environnment);
        }
        return "redirect:/ui/projects/" + project.getKey();
    }

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
            this.errorHandler.registerError("Invalid data provided. Failed to create environment.", errors);
        }

        return "redirect:/ui/projects/" + project.getKey();
    }

    private boolean createEnvironment(String envName, String configuration) {

        final ObjectMapper mapper = new ObjectMapper();
        boolean success = true;
        try {
            final JsonNode rootNode = mapper.readTree(configuration);
            final JsonNode modulesNode = rootNode.get("modules");
            if (modulesNode == null) {
                return true;
            }
            if (modulesNode.isArray()) {
                final PuppetClient puppetClient = new PuppetClient(this.configurationService.getPuppetModuleInstallScript());
                for (final JsonNode moduleNode : modulesNode) {
                    final String moduleName = moduleNode.path("name").asText(); // name is required
                    if (moduleName == null) {
                        return false;
                    }
                    final String moduleVersion = moduleNode.path("version").asText(); // version is optional
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
            //success = false;
            LOGGER.error("Failed to create environment " + envName, e);
            this.errorHandler.registerError("Failed to create Puppet environment. Error parsing configuration file.");
        }
        try {

            final IForemanApi foremanApi = this.foremanClientFactory.createForemanClient();
            this.foremanService.importPuppetClasses(foremanApi, this.configurationService.getSmartProxyId());

        } catch (final Exception e) {
            //success = false;
            LOGGER.error("[foreman] Failed to import puppet classes.", e);
            this.errorHandler.registerError("Failed to import Puppet classes.");
        }
        return success;
    }

    @RequestMapping(value = "/ui/foreman/targets/new", method = RequestMethod.POST)
    public String createTarget(@RequestParam("projectId") String projectId, @RequestParam("name") String name, @RequestParam("environment") String env, @RequestParam("computeprofiles") String computeprofiles, @RequestParam("operatingSystemsImage") Integer imageId, @RequestParam("puppetConfiguration") String puppetConfiguration) throws Exception {

        final Project project = this.projectDAO.findOne(projectId);

        final ForemanTarget foremanTarget = new ForemanTarget();
        foremanTarget.setProject(project);
        foremanTarget.setName(name);
        foremanTarget.setComputeProfile(computeprofiles);
        foremanTarget.setPuppetConfiguration(puppetConfiguration);
        foremanTarget.setImageId(imageId);

        // retrieve OS image
        final IForemanApi foremanApi = this.foremanClientFactory.createForemanClient();
        LOGGER.info("Retrieving image with id {}...", imageId);
        final Image image = this.foremanService.findOperatingSystemImage(foremanApi, imageId);

        // associate the target to an OS
        if (image != null) {
            LOGGER.info("Image {} found [os={}, architecure={}]", imageId, image.getOperatingSystemName(), image.getArchitectureName());
            foremanTarget.setOperatingSystemId(Integer.toString(image.getOperatingSystemId()));
            foremanTarget.setOperatingSystemName(image.getOperatingSystemName());
            foremanTarget.setArchitectureId(image.getArchitectureId());
        }

        // associate the target to an environment
        LOGGER.info("Retrieving environment {}...", env);
        final ForemanEnvironnment environment = this.environmentDAO.findOne(env);
        foremanTarget.setEnvironment(environment);

        // validate the target object
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        final Set<ConstraintViolation<ForemanTarget>> errors = validator.validate(foremanTarget);

        if (errors.size() == 0) {
            this.targetDAO.save(foremanTarget);
        } else {
            LOGGER.error("[foreman] Failed to create target : {}", errors);
            this.errorHandler.registerError("Invalid data provided. Failed to create target.", errors);
        }

        return "redirect:/ui/projects/" + project.getKey();
    }

    @RequestMapping(value = "/ui/foreman/targets/instantiate", method = RequestMethod.POST)
    public String instantiateTarget(@RequestParam("projectId") String projectId, @RequestParam("hostName") String hostName, @RequestParam("hostPass") String hostPass, @RequestParam("targetId") String targetId) {

        final Project project = this.projectDAO.findOne(projectId);
        final ForemanTarget target = this.targetDAO.findOne(targetId);

        String redirectURL = "/ui/projects/" + project.getKey();

        try {

            // Create a host descriptor using the provided data and the data from the configuration file
            final ForemanHostDescriptor hostDescriptor = this.hostDescriptorFactory.createHostDescriptor(project, target, hostName, hostPass);

            // Call Foreman to create the VM
            final IForemanApi foremanApi = this.foremanClientFactory.createForemanClient();
            final Host host = this.foremanService.createHost(foremanApi, hostDescriptor);

            //TODO find a way to generate the plugin tab ID dynamically
            redirectURL += "?foremanHost=" + host.name + "#foreman";

        } catch (final ForemanHostAlreadyExistException e) {
            LOGGER.error("[foreman] Failed to create host {} : {}", hostName, e);
            this.errorHandler.registerError("Failed to instantiate target. The provided name is already used for another instance.");
        } catch (final Exception e) {
            LOGGER.error("[foreman] Failed to create host {} : {}.", hostName, e);
            this.errorHandler.registerError("Failed to instantiate target. Please verify your Foreman configuration.");
        }

        return "redirect:" + redirectURL;
    }

    @RequestMapping(value = "/ui/foreman/targets/delete", method = RequestMethod.POST)
    public String instantiateTarget(@RequestParam("projectId") String projectId, @RequestParam("targetId") String targetId) {
        final Project project = this.projectDAO.findOne(projectId);
        final ForemanTarget target = this.targetDAO.findOne(targetId);
        if (target != null) {
            this.targetDAO.delete(target);
        }
        String redirectURL = "/ui/projects/" + project.getKey();
        return "redirect:" + redirectURL;
    }

}
