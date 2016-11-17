package fr.echoes.labs.komea.foundation.plugins.git.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.komea.foundation.plugins.git.GitConfigurationBean;
import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.plugins.utils.ConfigurationPropertiesUtils;

@Service
public class GitNameResolver {

	private GitConfigurationService configurationService;
	
	@Autowired
	public GitNameResolver(final GitConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	
	public String getRepositoryKey(final ProjectDto project) {
		if (project.getOtherAttributes().containsKey(ProjectExtensionConstants.GIT_REPOSITORY_KEY)) {
			return (String) project.getOtherAttributes().get(ProjectExtensionConstants.GIT_REPOSITORY_KEY);
		}
		return ProjectUtils.createIdentifier(project.getName());
	}
	
	public String getProjectScmUrl(final ProjectDto project) {
		final GitConfigurationBean configuration = this.configurationService.getConfigurationBean();
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("scmUrl", configuration.getScmUrl());
        variables.put("projectKey", getRepositoryKey(project));
        return ConfigurationPropertiesUtils.replaceVariables(configuration.getProjectScmUrlPattern(), variables);
    }
	
	public String getProjectScmUrl(final ProjectDto project, final String username) {
		final GitConfigurationBean configuration = this.configurationService.getConfigurationBean();
        final Map<String, String> variables = new HashMap<String, String>(4);
        variables.put("scmUrl", configuration.getScmUrl());
        variables.put("projectName", project.getName());
        variables.put("userLogin", username);
        variables.put("projectKey", getRepositoryKey(project));
        return ConfigurationPropertiesUtils.replaceVariables(configuration.getDisplayedUri(), variables);
    }
	
	public String getReleaseBranchName(String releaseVersion) {
		final GitConfigurationBean configuration = this.configurationService.getConfigurationBean();
        final Map<String, String> variables = new HashMap<String, String>(1);
        variables.put("releaseVersion", ProjectUtils.createIdentifier(releaseVersion));
        return ConfigurationPropertiesUtils.replaceVariables(configuration.getBranchReleasePattern(), variables);
    }

    public String getFeatureBranchName(String featureId, String featureDescription) {
    	final GitConfigurationBean configuration = this.configurationService.getConfigurationBean();
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("featureId", ProjectUtils.createIdentifier(featureId));
        variables.put("featureDescription", ProjectUtils.createIdentifier(featureDescription));
        return ConfigurationPropertiesUtils.replaceVariables(configuration.getBranchFeaturePattern(), variables);
    }
	
}
