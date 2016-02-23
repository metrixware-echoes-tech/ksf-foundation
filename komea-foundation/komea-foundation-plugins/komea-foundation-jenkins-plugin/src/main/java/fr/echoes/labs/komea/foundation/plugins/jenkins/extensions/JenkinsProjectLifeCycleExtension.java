package fr.echoes.labs.komea.foundation.plugins.jenkins.extensions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.komea.foundation.plugins.jenkins.services.IJenkinsService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsErrorHandlingService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

/**
 * @author dcollard
 *
 */
@Order(value=3)
@Extension
public class JenkinsProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsProjectLifeCycleExtension.class);

	@Autowired
	private JenkinsErrorHandlingService errorHandler;

	@Autowired
	private IJenkinsService jenkinsService;

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private ApplicationContext applicationContext;

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
			LOGGER.error("[Jenkins] No user found. Aborting project creation in Jenkins module");
			return;
		}

		LOGGER.info("[Jenkins] project {} creation detected [demanded by: {}]", project.getKey(), logginName);
		try {

			this.jenkinsService.createProject(project.getName());

		} catch (final Exception ex) {
			LOGGER.error("[Jenkins] Failed to create project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to create Jenkins project. Please verify your Jenkins configuration.");
		}
	}

	@Override
	public void notifyDeletedProject(ProjectDto project) {

		try {

			this.jenkinsService.deleteProject(project.getName());

		} catch (final Exception ex) {
			LOGGER.error("[Jenkins] Failed to delete project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to delete Jenkins project. Please verify your Jenkins configuration.");
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
