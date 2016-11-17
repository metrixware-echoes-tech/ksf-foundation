package fr.echoes.labs.ksf.cc.plugins.dashboard.services;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.komea.organization.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.cc.plugins.dashboard.DashboardConfigurationBean;
import fr.echoes.labs.ksf.cc.plugins.dashboard.entities.GitRepository;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@Service
public class DashboardEntityFactory {
	
	private DashboardConfigurationService configurationService;
	
	@Autowired
	public DashboardEntityFactory(final DashboardConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public String getProjectEntityKey(final ProjectDto project) {
		return project.getName();
	}
	
	public Entity createProjectEntity(final ProjectDto project) {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		final String projectType = configuration.getProjectType();
		final String projectName = project.getName();
		final String projectKey = getProjectEntityKey(project);
				
		final Entity projectEntity = new Entity()
			.setKey(projectKey)
			.setName(projectName)
			.setType(projectType);
		
		final String redmineProjectTag = configuration.getRedmineProjectTag();
		
		if (!StringUtils.isEmpty(redmineProjectTag)) {
			final String redmineProjectKey = ProjectUtils.createIdentifier(project.getName());			
			projectEntity.addAttribute(redmineProjectTag, redmineProjectKey);
		}
		
		return projectEntity;
	}
	
	public List<Entity> createJobEntities(final ProjectDto project) {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		List<Entity> entities = Lists.newArrayList();
		
		final String projectKeyTag = configuration.getProjectKeyTag();
		final String jobType = configuration.getJobType();
		final String projectKey = getProjectEntityKey(project);
		
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
	
	public Entity createGitEntity(final ProjectDto project, final GitRepository repository) {
		
		final DashboardConfigurationBean configuration = this.configurationService.getPluginConfigurationBean();
		
		if (repository != null) {
			Entity entity = new Entity();
			entity.setKey(repository.getName());
			entity.setName(repository.getName());
			entity.setType(configuration.getRepositoryType());
			entity.addAttribute("url", repository.getRemoteURL());
			entity.addAttribute(configuration.getProjectKeyTag(), getProjectEntityKey(project));
			return entity;
		}
		
		return null;
	}
	
}
