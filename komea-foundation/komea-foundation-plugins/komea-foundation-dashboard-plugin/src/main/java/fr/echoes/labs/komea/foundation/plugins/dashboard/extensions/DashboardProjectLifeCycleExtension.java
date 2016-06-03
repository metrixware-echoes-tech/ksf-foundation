package fr.echoes.labs.komea.foundation.plugins.dashboard.extensions;

import java.text.Normalizer;

import org.apache.commons.lang3.StringUtils;
import org.komea.organization.model.Entity;
import org.komea.organization.storage.client.OrganizationStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.tocea.corolla.products.dao.IProjectDAO;

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

		final String projectName = project.getName();
		final String projectKey = createIdentifier(projectName);

		final OrganizationStorageClient organizationStorageClient = this.clientFactory.organizationStorageClient();

		final Entity projectEntity = new Entity().setKey(projectKey).setName(projectName);
		organizationStorageClient.addOrUpdatePartialEntities(projectEntity);

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
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedFeature(ProjectDto project, String featureId,
			String featureSubject) {
		return NotifyResult.CONTINUE;
	}

	private String createIdentifier(String projectName) {
		return  Normalizer.normalize(projectName, Normalizer.Form.NFD).replaceAll("[^\\dA-Za-z\\-]", "").replaceAll("\\s+","-" ).toLowerCase();
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
