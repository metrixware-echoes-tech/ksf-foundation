package fr.echoes.labs.komea.foundation.plugins.jenkins.extensions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.EditProjectBranchCommand;
import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.IJenkinsService;
import fr.echoes.labs.komea.foundation.plugins.jenkins.services.JenkinsErrorHandlingService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
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
	public NotifyResult notifyCreatedProject(ProjectDto project) {

		init();

		final String logginName = this.currentUserService.getCurrentUserLogin();

		if (StringUtils.isEmpty(logginName)) {
			LOGGER.error("[Jenkins] No user found. Aborting project creation in Jenkins module");
			return NotifyResult.CONTINUE;
		}

		LOGGER.info("[Jenkins] project {} creation detected [demanded by: {}]", project.getKey(), logginName);
		try {

			this.jenkinsService.createProject(project);

		} catch (final Exception ex) {
			LOGGER.error("[Jenkins] Failed to create project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to create Jenkins project.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDeletedProject(ProjectDto project) {

		try {

			this.jenkinsService.deleteProject(project.getName());

		} catch (final Exception ex) {
			LOGGER.error("[Jenkins] Failed to delete project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to delete Jenkins project.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDuplicatedProject(ProjectDto _project) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyUpdatedProject(ProjectDto _project) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedRelease(ProjectDto project, String releaseVersion) {
		try {
			this.jenkinsService.createRelease(project, releaseVersion);
		} catch (final Exception ex) {
			LOGGER.error("[Jenkins] Failed to create release for project {} ", project.getName(), ex);
			this.errorHandler.registerError("Failed to create release.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedFeature(ProjectDto project, String featureId, String featureSubject) {
		try {
			this.jenkinsService.createFeature(project, featureId, featureSubject);
		} catch (final Exception ex) {
			LOGGER.error("[Jenkins] Failed to create release for project {} ", project.getName(), ex);
			this.errorHandler.registerError("Failed to create release.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedRelease(ProjectDto project, String releaseName) {
		try {

			this.jenkinsService.deleteReleaseJob(project.getName(), releaseName);
		} catch (final Exception ex) {
			LOGGER.error("[Jenkins] Failed to delete the release job", ex);
			this.errorHandler.registerError("Failed to delete the release job.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedFeature(ProjectDto projectDto, String featureId,
			String featureSubject) {
		try {
			this.jenkinsService.deleteFeatureJob(projectDto.getName(), featureId, featureSubject);
		} catch (final JenkinsExtensionException e) {
			LOGGER.error("[Jenkins] Failed to delete the feature job", e);
			this.errorHandler.registerError("Failed to delete the feature job.");
		}
		return NotifyResult.CONTINUE;

	}

}
