package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanclient.ForemanHelper;
import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanClientFactory;
import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanConfigurationService;
import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanErrorHandlingService;
import fr.echoes.lab.ksf.extensions.annotations.Extension;
import fr.echoes.lab.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.lab.ksf.extensions.projects.ProjectDto;

@Extension
public class ForemanProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanProjectLifeCycleExtension.class);

	@Autowired
	private ForemanConfigurationService configurationService;

	@Autowired
	private ForemanErrorHandlingService errorHandler;

	@Override
	public void notifyCreatedProject(ProjectDto _project) {

		LOGGER.info("[foreman] project creation detected : "+_project.getKey());

		final String logginName = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			final IForemanApi foremanApi = new ForemanClientFactory().createForemanClient();
			
			ForemanHelper.createProject(foremanApi, _project.getName(), logginName);
		} catch (final Exception e) {
			LOGGER.error("[foreman] project creation failed", e);
			this.errorHandler.registerError("Unable to create Foreman project. Please verify your Foreman configuration.");
		}
	}

	@Override
	public void notifyDeletedProject(ProjectDto _project) {
        try {
            // TODO Auto-generated method stub

        	final IForemanApi foremanApi = new ForemanClientFactory().createForemanClient();
        	
            ForemanHelper.deleteProject(foremanApi, _project.getName());
        } catch (final Exception ex) {
            LOGGER.error("[foreman] project delete failed", ex);
            this.errorHandler.registerError("Unable to delete Foreman project. Please verify your Foreman configuration.");
        }
	}

	@Override
	public void notifyDuplicatedProject(ProjectDto _project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyUpdatedProject(ProjectDto _project) {
		// TODO Auto-generated method stub

	}

}
