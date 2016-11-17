package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import fr.echoes.labs.ksf.cc.plugins.dashboard.DashboardConfigurationBean;
import fr.echoes.labs.ksf.cc.plugins.dashboard.entities.GitRepository;
import fr.echoes.labs.ksf.cc.plugins.dashboard.utils.DashboardUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@Service
public class DashboardService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);

	private DashboardClientFactory clientFactory;
	
	private DashboardConfigurationService configurationService;
	
	private DashboardEntityFactory entityFactory;
	
	@Autowired
	public DashboardService(final DashboardConfigurationService configurationService, final DashboardClientFactory clientFactory, final DashboardEntityFactory entityFactory) {
		this.clientFactory = clientFactory;
		this.entityFactory = entityFactory;
		this.configurationService = configurationService;
	}
	
	public void updateProjectEntities(final ProjectDto project) {
		
		final List<Entity> entities = getAllEntities(project);
		
		LOGGER.info("Updating {} entities in Komea Dashboard", entities.size());

		final OrganizationStorageClient organizationStorageClient = clientFactory.organizationStorageClient();
		organizationStorageClient.addOrUpdatePartialEntities(entities);
		
	}
	
	public void disableProjectEntities(final ProjectDto project) {
		
		final List<Entity> entities = getAllEntities(project);
		final Map<String, Set<String>> entitiesKeysByType = DashboardUtils.splitEntitiesByType(entities);
		
		final OrganizationStorageClient organizationStorageClient = clientFactory.organizationStorageClient();
		
		for (Entry<String, Set<String>> entry : entitiesKeysByType.entrySet()) {
			LOGGER.info("Disabling {} entities of type {} in Komea Dashboard", entry.getValue().size(), entry.getKey());
			organizationStorageClient.setEntitiesActivation(entry.getKey(), Lists.newArrayList(entry.getValue()), false);
		}
		
	}
	
	public List<Entity> getAllEntities(final ProjectDto project) {
		
		final List<Entity> entities = Lists.newArrayList();
		
		entities.add(entityFactory.createProjectEntity(project));
		
		final List<Entity> jobEntities = entityFactory.createJobEntities(project);
		if (!jobEntities.isEmpty()) {
			entities.addAll(jobEntities);
		}
		
		final GitRepository repository = getGitRepository(project);
		final Entity gitEntity = entityFactory.createGitEntity(project, repository);
		if (gitEntity != null) {
			entities.add(gitEntity);
		}
		
		return entities;
	}
	
	private GitRepository getGitRepository(final ProjectDto project) {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		final String gitURL = (String) project.getOtherAttributes().get(ProjectExtensionConstants.GIT_URL);
		final List<String> gitIncludedBranches = (List<String>) project.getOtherAttributes().get(ProjectExtensionConstants.ANALYZED_BRANCHES);
		
		if (!StringUtils.isEmpty(gitURL)) {
		
			final GitRepository repository = new GitRepository();
			repository.setName(project.getName());
			repository.setRemoteURL(gitURL);
			repository.setIncludedBranches(gitIncludedBranches);
			repository.setUsername(configuration.getUsername());
			repository.setPassword(configuration.getPassword());
			
			return repository;		
		}
		
		return null;
	}
	
	public void updateConnectorProperties(final ProjectDto project) {
		
		final List<ConnectorProperty> properties = Lists.newArrayList();
		final ConnectorsConfigurationClient configurationClient = clientFactory.connectorsConfigurationClient();
		
		GitRepository repository = getGitRepository(project);
			
		if (repository != null && !StringUtils.isEmpty(repository.getRemoteURL())) {
			
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
	
	public void removeConnectorProperties(final ProjectDto project) {
		
		ConnectorsConfigurationClient configurationClient = clientFactory.connectorsConfigurationClient();		
		GitRepository repository = getGitRepository(project);
		
		if (repository != null && !StringUtils.isEmpty(repository.getRemoteURL())) {
			
			ConnectorProperty property = configurationClient.getProperty(DashboardConfigurationService.GIT_REPOSITORIES_PROPERTY);
			
			if (property != null) {
				List<GitRepository> repositories = DashboardUtils.extractGitRepositories(property);
				List<GitRepository> matched = DashboardUtils.findRepositoriesByRemoteURL(repositories, repository.getRemoteURL());
				if (!matched.isEmpty()) {
					for (GitRepository match : matched) {
						repositories.remove(match);
					}
					property.setValue(repositories);
					LOGGER.info("Removing git connector properties in Komea Dashboard for project {}", project.getKey());
					configurationClient.saveProperty(property);
				}
			}
			
		}
		
	}
	
}
