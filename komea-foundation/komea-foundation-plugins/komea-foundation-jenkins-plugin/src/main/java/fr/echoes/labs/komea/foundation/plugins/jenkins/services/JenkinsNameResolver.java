package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsConfigurationBean;
import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.plugins.utils.ConfigurationPropertiesUtils;

@Service
public class JenkinsNameResolver {
	
	private JenkinsConfigurationService configurationService;
	
	@Autowired
	public JenkinsNameResolver(final JenkinsConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public String getFolderJobName(final Project project) {
		return getFolderJobName(project.getName(), project.getOtherAttributes());
	}
	
	public String getFolderJobName(final ProjectDto project) {
		return getFolderJobName(project.getName(), project.getOtherAttributes());
	}
	
	public String getFolderJobName(final String name, final Map<String, Object> attributes) {
    	final String key = (String) attributes.get(ProjectExtensionConstants.JENKINS_KEY);
    	if (StringUtils.isEmpty(key)) {
    		return getFolderJobName(name);
    	}
    	return key;
    }
    
    private static String getFolderJobName(final String projectName) {
        return ProjectUtils.createIdentifier(projectName);
    }
    
    public String getJobName(final ProjectDto project, final String branchName) {
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("projectName", getFolderJobName(project));
        variables.put("branchName", ProjectUtils.createIdentifier(branchName));
        return ConfigurationPropertiesUtils.replaceVariables(this.configurationService.getConfigurationBean().getJobNamePattern(), variables);
    }
	
	public String getReleaseJobName(final ProjectDto project, final String releaseVersion) {
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("projectName", getFolderJobName(project));
        variables.put("releaseVersion", ProjectUtils.createIdentifier(releaseVersion));
        return ConfigurationPropertiesUtils.replaceVariables(this.configurationService.getConfigurationBean().getJobReleasePattern(), variables);
    }
	
	public String getFeatureJobName(final ProjectDto project, final String featureId,
            final String featureSubject) {
        final Map<String, String> variables = new HashMap<String, String>(3);
        variables.put("projectName", getFolderJobName(project));
        variables.put("featureId", ProjectUtils.createIdentifier(featureId));
        variables.put("featureDescription", ProjectUtils.createIdentifier(featureSubject));
        return ConfigurationPropertiesUtils.replaceVariables(this.configurationService.getConfigurationBean().getJobFeaturePattern(), variables);
    }
	
	public String getProjectScmUrl(final ProjectDto project) {
		final JenkinsConfigurationBean configuration = this.configurationService.getConfigurationBean();
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("scmUrl", configuration.getScmUrl());
        variables.put("projectKey", getScmRepositoryName(project));
        return ConfigurationPropertiesUtils.replaceVariables(configuration.getProjectScmUrlPattern(), variables);
    }
	
	public String getScmRepositoryName(final ProjectDto project) {		
		final String repoName = (String) project.getOtherAttributes().get(ProjectExtensionConstants.GIT_REPOSITORY_KEY);	
		if (!StringUtils.isEmpty(repoName)) {
			return repoName;
		}
		return ProjectUtils.createIdentifier(project.getName());
	}
	
	public String getNexusRepositoryKey(final ProjectDto project) {		
		final String repository = (String) project.getOtherAttributes().get(ProjectExtensionConstants.NEXUS_REPOSITORY_KEY);
		if (!StringUtils.isEmpty(repository)) {
			return repository;
		}
		return ProjectUtils.createIdentifier(project.getName());
	}
	
	public String getDisplayName(final String projectName, final String branchName) {
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("projectName", projectName);
        variables.put("branchName", branchName);
        return ConfigurationPropertiesUtils.replaceVariables(this.configurationService.getConfigurationBean().getJobNamePattern(), variables);
    }

    public String getGitReleaseBranchName(String releaseVersion) {
        final Map<String, String> variables = new HashMap<String, String>(1);
        variables.put("releaseVersion", ProjectUtils.createIdentifier(releaseVersion));
        return ConfigurationPropertiesUtils.replaceVariables(this.configurationService.getConfigurationBean().getGitReleaseBranchPattern(), variables);
    }

    public String getGitFeatureBranchName(String featureId, String featureDescription) {
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("featureId", ProjectUtils.createIdentifier(featureId));
        variables.put("featureDescription", ProjectUtils.createIdentifier(featureDescription));
        return ConfigurationPropertiesUtils.replaceVariables(this.configurationService.getConfigurationBean().getGitFeatureBranchPattern(), variables);
    }
	
}
