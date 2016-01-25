package fr.echoes.lab.ksf.cc.plugins.foreman.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanclient.ForemanHelper;
import fr.echoes.lab.ksf.cc.plugins.foreman.dao.IForemanEnvironmentDAO;
import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanEnvironnment;
import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanClientFactory;
import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanErrorHandlingService;

@RestController
public class ForemanRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanRestController.class);

	@Autowired
	private IForemanEnvironmentDAO environmentDAO;

    @Autowired
    private ForemanErrorHandlingService errorHandler;


    @Value("${ksf.foreman.puppet.configuration.create.parameters.enabled}")
    private Boolean createParametersEnabled;

	@RequestMapping(value = "/rest/foreman/environments/all")
	public List<ForemanEnvironnment> findAll() {

		return this.environmentDAO.findAll();
	}

	@RequestMapping(value = "/rest/foreman/environments/{id}")
	public ForemanEnvironnment findOne(@PathVariable("id") String id) {

		return this.environmentDAO.findOne(id);
	}

	@RequestMapping(value = "/rest/foreman/puppet_class_parameters/{id}")
	public String puppetClassParameters(@PathVariable("id") String id) {
		final ForemanEnvironnment environment = this.environmentDAO.findOne(id);
		String json = "";
		final String environmentName = environment.getName();
		try {

			final IForemanApi foremanApi = new ForemanClientFactory().createForemanClient();

			json = ForemanHelper.getModulesPuppetClassParameters(foremanApi, environmentName, this.createParametersEnabled);
		} catch (final Exception e) {
            LOGGER.error("Failed to get puppet classes parameters for the environment: " + environmentName, e);
            this.errorHandler.registerError("Failed to get puppet classes parameters for " + environmentName);
		}
		return json;
	}
}
