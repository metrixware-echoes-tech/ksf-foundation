package fr.echoes.labs.ksf.cc.plugins.redmine.extensions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.ksf.cc.plugins.redmine.services.IRedmineService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineErrorHandlingService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

/**
 * @author dcollard
 *
 */
@Order(value=1)
@Extension
public class RedmineProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineProjectLifeCycleExtension.class);

	@Autowired
	private RedmineErrorHandlingService errorHandler;

	@Autowired
	private IRedmineService redmineService;

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RedmineConfigurationService configurationService;

	private ICurrentUserService currentUserService;

	public void init() {

		if (this.currentUserService == null) {
			this.currentUserService = this.applicationContext.getBean(ICurrentUserService.class);
		}
	}

	@Override
	public void notifyCreatedProject(ProjectDto project) {

		init();

		final String logginName = this.currentUserService.getCurrentUserLogin();

		if (StringUtils.isEmpty(logginName)) {
			LOGGER.error("[Redmine] No user found. Aborting project creation in Redmine module");
			return;
		}

		LOGGER.info("[Redmine] project {} creation detected [demanded by: {}]", project.getKey(), logginName);
		try {

			this.redmineService.createProject(project.getName());

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to create project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to create Redmine project. Please verify your Redmine configuration.");
		}
	}

	@Override
	public void notifyDeletedProject(ProjectDto project) {

		try {

			this.redmineService.deleteProject(project.getName());

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to delete project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to delete Redmine project. Please verify your Redmine configuration.");
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

	@Override
	public void notifyCreatedRelease(ProjectDto project, String releaseVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyCreatedFeature(ProjectDto project, String ticketId,
			String featureSubject) {

		try {
			this.redmineService.changeStatus(ticketId, this.configurationService.getFeatureStatusAssignedId());

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to change ticket #{} status", ticketId, ex);
			this.errorHandler.registerError("Failed to change Redmine ticket status. Please verify your Redmine configuration.");
		}
	}

}
