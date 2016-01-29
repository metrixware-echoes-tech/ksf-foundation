package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.lab.foremanapi.IForemanApi;
import fr.echoes.lab.foremanclient.IForemanService;
import fr.echoes.lab.ksf.cc.plugins.foreman.dao.IForemanTargetDAO;
import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanTarget;
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
	private IForemanService foremanService;
	
	@Autowired
	private ForemanConfigurationService configurationService;
	
	@Autowired
	private ForemanClientFactory foremanClientFactory;

	@Autowired
	private ForemanErrorHandlingService errorHandler;
	
	@Autowired
	private IForemanTargetDAO targetDAO;
	
	@Autowired
	private IProjectDAO projectDAO;

	@Override
	public void notifyCreatedProject(ProjectDto _project) {

		LOGGER.info("[foreman] project creation detected : "+_project.getKey());

		final String logginName = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			
			// Create the project in Foreman
			final IForemanApi foremanApi = foremanClientFactory.createForemanClient();		
			foremanService.createProject(foremanApi, _project.getName(), logginName);
			
		} catch (final Exception e) {
			LOGGER.error("[foreman] project creation failed", e);
			this.errorHandler.registerError("Unable to create Foreman project. Please verify your Foreman configuration.");
		}
	}

	@Override
	public void notifyDeletedProject(ProjectDto _project) {
        try {

        	// Delete targets associated to the project
        	List<ForemanTarget> targets = targetDAO.findByProject(projectDAO.findOne(_project.getId()));
        	targetDAO.delete(targets);
        	
        	// Delete project data in Foreman
        	final IForemanApi foremanApi = foremanClientFactory.createForemanClient();
            foremanService.deleteProject(foremanApi, _project.getName());
            
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
