package fr.echoes.labs.ksf.cc.plugins.foreman.extensions;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.foremanapi.IForemanApi;
import fr.echoes.labs.foremanclient.IForemanService;
import fr.echoes.labs.ksf.cc.plugins.foreman.dao.IForemanTargetDAO;
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanTarget;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanClientFactory;
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanErrorHandlingService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

@Extension
public class ForemanProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanProjectLifeCycleExtension.class);

	@Autowired
	private IForemanService foremanService;
	
	@Autowired
	private ForemanClientFactory foremanClientFactory;

	@Autowired
	private ForemanErrorHandlingService errorHandler;
	
	@Autowired
	private IForemanTargetDAO targetDAO;
	
	@Autowired
	private IProjectDAO projectDAO;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private ICurrentUserService currentUserService;

	public void init() {
		if (currentUserService == null) {
			currentUserService = applicationContext.getBean(ICurrentUserService.class);
		}
	}
	
	@Override
	public void notifyCreatedProject(ProjectDto _project) {

		init();
		
		final String logginName = currentUserService.getCurrentUserLogin();
		//SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (StringUtils.isEmpty(logginName)) {
			LOGGER.error("[Foreman] No user found. Aborting project creation in Foreman module");
			return;
		}
		
		LOGGER.info("[Foreman] project {} creation detected [demanded by: {}]", _project.getKey(), logginName);
		
		try {
			
			// Create the project in Foreman
			final IForemanApi foremanApi = foremanClientFactory.createForemanClient();		
			foremanService.createProject(foremanApi, _project.getName(), logginName);
			
		} catch (final Exception e) {
			LOGGER.error("[Foreman] project creation failed", e);
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
