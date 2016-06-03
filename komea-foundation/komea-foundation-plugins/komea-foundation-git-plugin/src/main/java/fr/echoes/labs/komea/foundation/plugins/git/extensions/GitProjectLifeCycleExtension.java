package fr.echoes.labs.komea.foundation.plugins.git.extensions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.komea.foundation.plugins.git.GitExtensionMergeException;
import fr.echoes.labs.komea.foundation.plugins.git.services.GitErrorHandlingService;
import fr.echoes.labs.komea.foundation.plugins.git.services.IGitService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
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
	public NotifyResult notifyCreatedProject(ProjectDto project) {

		init();

		final String logginName = this.currentUserService.getCurrentUserLogin();

		if (StringUtils.isEmpty(logginName)) {
			LOGGER.error("[Git] No user found. Aborting project creation in Git module");
			return NotifyResult.CONTINUE;
		}

		LOGGER.info("[Git] project {} creation detected [demanded by: {}]", project.getKey(), logginName);

		try {

			this.gitService.createProject(project.getName());
		} catch (final Exception ex) {
			LOGGER.error("[Git] Failed to create project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to create Git repository.");
		}
		return NotifyResult.CONTINUE;

	}

	@Override
	public NotifyResult notifyDeletedProject(ProjectDto project) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDuplicatedProject(ProjectDto project) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyUpdatedProject(ProjectDto project) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedRelease(ProjectDto project, String releaseVersion) {
		try {

			this.gitService.createRelease(project.getName(), releaseVersion);
		} catch (final Exception ex) {
			LOGGER.error("[Git] Failed to create release for project {} ", project.getName(), ex);
			this.errorHandler.registerError("Failed to create release.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedFeature(ProjectDto project, String featureId,
			String featureSubject) {
		try {

			this.gitService.createFeature(project.getName(), featureId, featureSubject);
		} catch (final Exception ex) {
			LOGGER.error("[Git] Failed to create feature for project {} ", project.getName(), ex);
			this.errorHandler.registerError("Failed to create Git feature branch.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedFeature(ProjectDto project, String featureId,
			String featureSubject) {
		try {
			this.gitService.closeFeature(project.getName(), featureId, featureSubject);
		} catch (final GitExtensionMergeException e) {
			LOGGER.error("[Git] Failed to finish feature for project {} ", project.getName(), e);
			this.errorHandler.registerError("Failed to finish feature: " + e.getMessage());
			return NotifyResult.TERMINATE;
		} catch (final Exception e) {
			LOGGER.error("[Git] Failed to finish feature for project {} ", project.getName(), e);
			this.errorHandler.registerError("Failed to finish feature.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedRelease(ProjectDto project, String releaseName) {
		try {
			this.gitService.closeRelease(project.getName(), releaseName);
		} catch (final Exception ex) {
			LOGGER.error("[Git] Failed to close feature for project {} ", project.getName(), ex);
			this.errorHandler.registerError("Failed to create release.");
		}
		return NotifyResult.CONTINUE;
	}

}
