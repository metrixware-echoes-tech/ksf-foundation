package fr.echoes.labs.komea.foundation.plugins.git.extensions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.komea.foundation.plugins.git.services.GitErrorHandlingService;
import fr.echoes.labs.komea.foundation.plugins.git.services.IGitService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

/**
 * @author dcollard
 *
 */
@Order(value=2)
@Extension
public class GitProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(GitProjectLifeCycleExtension.class);

	@Autowired
	private GitErrorHandlingService errorHandler;

	@Autowired
	private IGitService gitService;

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
			LOGGER.error("[Git] No user found. Aborting project creation in Git module");
			return;
		}

		LOGGER.info("[Git] project {} creation detected [demanded by: {}]", project.getKey(), logginName);

		try {

			this.gitService.createProject(project.getName());
		} catch (final Exception ex) {
			LOGGER.error("[Git] Failed to create project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to create Git repository.");
		}

	}

	@Override
	public void notifyDeletedProject(ProjectDto project) {

	}

	@Override
	public void notifyDuplicatedProject(ProjectDto project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyUpdatedProject(ProjectDto project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyCreatedRelease(ProjectDto project, String releaseVersion) {
		try {

			this.gitService.createRelease(project.getName(), releaseVersion);
		} catch (final Exception ex) {
			LOGGER.error("[Git] Failed to create release for project {} ", project.getName(), ex);
			this.errorHandler.registerError("Failed to create release.");
		}
	}

	@Override
	public void notifyCreatedFeature(ProjectDto project, String featureId,
			String featureSubject) {
		try {

			this.gitService.createFeature(project.getName(), featureId, featureSubject);
		} catch (final Exception ex) {
			LOGGER.error("[Git] Failed to create feature for project {} ", project.getName(), ex);
			this.errorHandler.registerError("Failed to create release.");
		}		
	}

	@Override
	public void notifyFinishedFeature(ProjectDto project, String featureId,
			String featureSubject) {
		try {
			this.gitService.closeFeature(project.getName(), featureId, featureSubject);
		} catch (final Exception ex) {
			LOGGER.error("[Git] Failed to close feature for project {} ", project.getName(), ex);
			this.errorHandler.registerError("Failed to create release.");
		}		
	}

	@Override
	public void notifyFinishedRelease(ProjectDto project, String releaseName) {
		// TODO Auto-generated method stub
		
	}

}
