package fr.echoes.labs.komea.foundation.plugins.dashboard.extensions;

import java.text.Normalizer;

import org.apache.commons.lang3.StringUtils;
import org.komea.organization.model.EntityType;
import org.komea.organization.storage.client.OrganizationStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.ksf.cc.plugins.dashboard.services.DashboardConfigurationService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
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
			LOGGER.error("[Dashboard] No user found. Aborting project creation in Git module");
			return;
		}

		LOGGER.info("[Dashboard] project {} creation detected [demanded by: {}]", project.getKey(), logginName);


		final String projectName = project.getName();
		final String projectKey = createIdentifier(projectName);

		final String url = this.configurationService.getUrl() + "/organization";

		final OrganizationStorageClient organizationStorageClient = new OrganizationStorageClient(url);
		final EntityType projectEntity = new EntityType().setKey(projectKey).setName(projectName);
		organizationStorageClient.createEntityTypesIfNotExist(projectEntity);
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


	}

	@Override
	public void notifyCreatedFeature(ProjectDto project, String featureId,
			String featureSubject) {

	}

	private String createIdentifier(String projectName) {
		return  Normalizer.normalize(projectName, Normalizer.Form.NFD).replaceAll("[^\\dA-Za-z\\-]", "").replaceAll("\\s+","-" ).toLowerCase();
	}


	@Override
	public void notifyFinishedRelease(ProjectDto project, String releaseName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyFinishedFeature(ProjectDto projectDto, String featureId,
			String featureSubject) {
		// TODO Auto-generated method stub

	}
}
