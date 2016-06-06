package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.komea.connectors.configuration.client.ConnectorsConfigurationClient;
import org.komea.connectors.configuration.model.ConnectorProperty;
import org.komea.organization.model.Entity;
import org.komea.organization.storage.client.OrganizationStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.cc.plugins.dashboard.entities.GitRepository;
import fr.echoes.labs.ksf.cc.plugins.dashboard.utils.DashboardUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@Service
public class DashboardService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);

	@Autowired
	private DashboardClientFactory clientFactory;
	
	@Autowired
	private DashboardConfigurationService configurationService;
	
	public void updateProjectEntities(final ProjectDto project) {
		
		final List<Entity> entities = getAllEntities(project);
		
		LOGGER.info("Updating {} entities in Komea Dashboard", entities.size());

		final OrganizationStorageClient organizationStorageClient = clientFactory.organizationStorageClient();
		organizationStorageClient.addOrUpdatePartialEntities(entities);
		
	}
	
	public List<Entity> getAllEntities(final ProjectDto project) {
		
		List<Entity> entities = Lists.newArrayList();
		
		entities.add(getProjectEntity(project));
		
		List<Entity> jobEntities = getJobEntities(project);
		if (!jobEntities.isEmpty()) {
			entities.addAll(jobEntities);
		}
		
		Entity gitEntity = getGitEntity(project);
		if (gitEntity != null) {
			entities.add(gitEntity);
		}
		
		return entities;
	}
	
	public Entity getProjectEntity(final ProjectDto project) {
		
		final String projectType = configurationService.getProjectType();
		final String projectName = project.getName();
		final String projectKey = ProjectUtils.createIdentifier(projectName);
				
		final Entity projectEntity = new Entity()
			.setKey(projectKey)
			.setName(projectName)
			.setType(projectType);
		
		return projectEntity;
	}
	
	public List<Entity> getJobEntities(final ProjectDto project) {
		
		List<Entity> entities = Lists.newArrayList();
		
		final String projectKeyTag = configurationService.getProjectKeyTag();
		final String jobType = configurationService.getJobType();
		final String projectKey = DashboardUtils.createIdentifier(project.getName());
		
		List<String> jobNames = (List<String>) project.getOtherAttributes().get(ProjectExtensionConstants.CI_JOBS_KEY);
		
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
	
	public Entity getGitEntity(final ProjectDto project) {
		
		final GitRepository repository = getGitRepository(project);
		
		if (repository != null) {
			Entity entity = new Entity();
			entity.setKey(repository.getName());
			entity.setName(repository.getName());
			entity.setType(configurationService.getRepositoryType());
			entity.addAttribute("url", repository.getUsername());
			entity.addAttribute(configurationService.getProjectKeyTag(), project.getKey());
			return entity;
		}
		
		return null;
	}
	
	private GitRepository getGitRepository(final ProjectDto project) {
		
		String gitURL = (String) project.getOtherAttributes().get(ProjectExtensionConstants.GIT_URL);
		List<String> gitIncludedBranches = (List<String>) project.getOtherAttributes().get(ProjectExtensionConstants.ANALYZED_BRANCHES);
		
		if (!StringUtils.isEmpty(gitURL)) {
		
			GitRepository repository = new GitRepository();
			repository.setName(project.getKey());
			repository.setRemoteURL(gitURL);
			repository.setIncludedBranches(gitIncludedBranches);
			repository.setUsername(configurationService.getUsername());
			repository.setPassword(configurationService.getPassword());
			
			return repository;		
		}
		
		return null;
	}
	
	public void updateConnectorProperties(final ProjectDto project) {
		
		List<ConnectorProperty> properties = Lists.newArrayList();
		ConnectorsConfigurationClient configurationClient = clientFactory.connectorsConfigurationClient();
		
		GitRepository repository = new GitRepository();
			
		if (repository != null) {
			
			ConnectorProperty property = configurationClient.getProperty(DashboardConfigurationService.GIT_REPOSITORIES_PROPERTY);
			
			if (property != null) {
				List<Object> repositories = (List<Object>) property.getValue();
				repositories.add(repository);
			}else{
				List<Object> repositories = Lists.newArrayList();
				repositories.add(repository);
				property = new ConnectorProperty(DashboardConfigurationService.GIT_REPOSITORIES_PROPERTY, repositories);
			}
			
			properties.add(property);
			
		}
		
		if (!properties.isEmpty()) {
			LOGGER.info("Saving {} connector properties in Komea Dashboard", properties.size());
			configurationClient.saveProperties(properties);
		}
		
	}
	
}
