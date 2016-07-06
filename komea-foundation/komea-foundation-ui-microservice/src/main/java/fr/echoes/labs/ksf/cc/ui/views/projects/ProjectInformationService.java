package fr.echoes.labs.ksf.cc.ui.views.projects;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.pluginfwk.api.propertystorage.PluginPropertyStorage;

@Service("projectInformationService")
public class ProjectInformationService {

	public static class GitProperties {

		private final String	branchReleasePattern	= "release-%{releaseVersion}";

		private final String	branchFeaturePattern	= "feat-%{featureId}-%{featureDescription}";

		private final String	projectScmUrlPattern	= "%{scmUrl}/%{projectKey}.git";

	}

	public static class JenkinsProperties {

		private final String	jobNamePattern		= "%{projectName}-%{branchName}";

		private final String	jobReleasePattern	= "%{projectName}-release-%{releaseVersion}";

		private final String	jobFeaturePattern	= "%{projectName}-feat-%{featureId}-%{featureDescription}";

	}

	private static final String		GIT_PLUGIN		= "git-plugin";

	public static final String		JENKINS_PLUGIN	= "Jenkins-plugin";

	@Autowired
	private PluginPropertyStorage	pluginPropertiesService;

	public String getFeatureJobName(final String projectName, final String featureId, final String featureSubject) {
		final Map<String, String> variables = new HashMap<>(3);
		variables.put("projectName", projectName);
		variables.put("featureId", featureId);
		variables.put("featureDescription", featureSubject);
		return this.replaceVariables(this.getJobFeaturePattern(), variables);
	}

	public String getGitFeatureBranchName(final String featureId, final String featureDescription) {
		final Map<String, String> variables = new HashMap<>(2);
		variables.put("featureId", featureId);
		variables.put("featureDescription", featureDescription);
		return ProjectUtils.createIdentifier(this.replaceVariables(this.getGitFeatureBranchPattern(), variables));
	}

	public String getGitFeatureBranchPattern() {
		return this.pluginPropertiesService.readPluginProperties(GIT_PLUGIN, GitProperties.class).branchFeaturePattern;
	}

	public String getGitReleaseBranchName(final String releaseVersion) {
		final Map<String, String> variables = new HashMap<>(1);
		variables.put("releaseVersion", releaseVersion);
		return ProjectUtils.createIdentifier(this.replaceVariables(this.getGitReleaseBranchPattern(), variables));
	}

	public String getGitReleaseBranchPattern() {
		return this.pluginPropertiesService.readPluginProperties(GIT_PLUGIN, GitProperties.class).branchReleasePattern;
	}

	public String getJobFeaturePattern() {
		return this.pluginPropertiesService.readPluginProperties(JENKINS_PLUGIN, JenkinsProperties.class).jobFeaturePattern;
	}

	public String getJobReleasePattern() {
		return this.pluginPropertiesService.readPluginProperties(JENKINS_PLUGIN, JenkinsProperties.class).jobReleasePattern;
	}

	public PluginPropertyStorage getPluginPropertiesService() {
		return this.pluginPropertiesService;
	}

	public String getReleaseJobName(final String projectName, final String releaseVersion) {
		final Map<String, String> variables = new HashMap<>(2);
		variables.put("projectName", projectName);
		variables.put("releaseVersion", releaseVersion);
		return this.replaceVariables(this.getJobReleasePattern(), variables);
	}

	public void setPluginPropertiesService(final PluginPropertyStorage pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	private String replaceVariables(final String str, final Map<String, String> variables) {
		final StrSubstitutor sub = new StrSubstitutor(variables);
		sub.setVariablePrefix("%{");
		return sub.replace(str);
	}

}
