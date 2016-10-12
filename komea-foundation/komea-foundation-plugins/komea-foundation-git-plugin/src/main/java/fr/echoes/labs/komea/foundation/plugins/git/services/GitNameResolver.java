package fr.echoes.labs.komea.foundation.plugins.git.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@Service
public class GitNameResolver {

	@Autowired
	private GitConfigurationService configuration;
	
	public String getRepositoryKey(final ProjectDto project) {
		if (project.getOtherAttributes().containsKey(ProjectExtensionConstants.GIT_REPOSITORY_KEY)) {
			return (String) project.getOtherAttributes().get(ProjectExtensionConstants.GIT_REPOSITORY_KEY);
		}
		return ProjectUtils.createIdentifier(project.getName());
	}
	
	public String getProjectScmUrl(final ProjectDto project) {
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("scmUrl", this.configuration.getScmUrl());
        variables.put("projectKey", getRepositoryKey(project));
        return replaceVariables(this.configuration.getProjectScmUrlPattern(), variables);
    }
	
	public String getProjectScmUrl(final ProjectDto project, final String username) {
        final Map<String, String> variables = new HashMap<String, String>(4);
        variables.put("scmUrl", this.configuration.getScmUrl());
        variables.put("projectName", project.getName());
        variables.put("userLogin", username);
        variables.put("projectKey", getRepositoryKey(project));
        return replaceVariables(this.configuration.getDisplayedUri(), variables);
    }
	
	public String getReleaseBranchName(String releaseVersion) {
        final Map<String, String> variables = new HashMap<String, String>(1);
        variables.put("releaseVersion", ProjectUtils.createIdentifier(releaseVersion));
        return replaceVariables(this.configuration.getBranchReleasePattern(), variables);
    }

    public String getFeatureBranchName(String featureId, String featureDescription) {
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("featureId", ProjectUtils.createIdentifier(featureId));
        variables.put("featureDescription", ProjectUtils.createIdentifier(featureDescription));
        return replaceVariables(this.configuration.getBranchFeaturePattern(), variables);
    }
	
	private static String replaceVariables(String str, Map<String, String> variables) {
        final StrSubstitutor sub = new StrSubstitutor(variables);
        sub.setVariablePrefix("%{");
        return sub.replace(str);
    }
	
}
