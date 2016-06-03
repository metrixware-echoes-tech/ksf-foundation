package fr.echoes.labs.komea.foundation.plugins.dashboard.extensions;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.komea.organization.model.Entity;
import org.komea.organization.storage.client.OrganizationStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.cc.plugins.dashboard.services.DashboardClientFactory;
import fr.echoes.labs.ksf.cc.plugins.dashboard.services.DashboardConfigurationService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

/**
 * @author dcollard
 *
 */
@Order(value=10)
@Extension
public class DashboardProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardProjectLifeCycleExtension.class);

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private DashboardConfigurationService configurationService;

	@Autowired
	private DashboardClientFactory clientFactory;

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
			LOGGER.error("[Dashboard] No user found. Aborting project creation in Git module");
			return NotifyResult.CONTINUE;
		}

		LOGGER.info("[Dashboard] project {} creation detected [demanded by: {}]", project.getKey(), logginName);

		final List<Entity> entities = getProjectEntities(project);

		final OrganizationStorageClient organizationStorageClient = this.clientFactory.organizationStorageClient();
		organizationStorageClient.addOrUpdatePartialEntities(entities);

		return NotifyResult.CONTINUE;
	}

	public List<Entity> getProjectEntities(final ProjectDto project) {

		final List<Entity> entities = Lists.newArrayList();

		final String projectType = this.configurationService.getProjectType();
		final String projectName = project.getName();
		final String projectKey = ProjectUtils.createIdentifier(projectName);
		final String projectKeyTag = this.configurationService.getProjectKeyTag();

		final Entity projectEntity = new Entity()
			.setKey(projectKey)
			.setName(projectName)
			.setType(projectType);

		entities.add(projectEntity);

		final String jobType = this.configurationService.getJobType();
		final List<String> jobNames = (List<String>) project.getOtherAttributes().get(ProjectExtensionConstants.CI_JOBS_KEY);

		if (jobNames != null && !jobNames.isEmpty()) {
			for (final String jobName : jobNames) {
				final Entity jobEntity = new Entity()
					.setKey(jobName)
					.setName(jobName)
					.setType(jobType)
					.addAttribute(projectKeyTag, projectKey);
				entities.add(jobEntity);
			}
		}

		return entities;
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
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedFeature(ProjectDto project, String featureId,
			String featureSubject) {
		return NotifyResult.CONTINUE;
	}


	@Override
	public NotifyResult notifyFinishedRelease(ProjectDto project, String releaseName) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedFeature(ProjectDto projectDto, String featureId,
			String featureSubject) {
		 return NotifyResult.CONTINUE;
	}
}
