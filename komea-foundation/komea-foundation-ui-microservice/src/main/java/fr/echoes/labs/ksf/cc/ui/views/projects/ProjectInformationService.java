package fr.echoes.labs.ksf.cc.ui.views.projects;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;


@Service("projectInformationService")
public class ProjectInformationService {

	@Value("${ksf.git.branch.releasePattern}")
	private String gitReleaseBranchPattern;

	@Value("${ksf.git.branch.featurePattern}")
	private String gitFeatureBranchPattern;

	@Value("${ksf.jenkins.job.featurePattern}")
	private String jobFeaturePattern;

	@Value("${ksf.jenkins.job.releasePattern}")
	private String jobReleasePattern;

	public String getGitReleaseBranchPattern() {
		return this.gitReleaseBranchPattern;
	}

	public String getGitFeatureBranchPattern() {
		return this.gitFeatureBranchPattern;
	}

	public String getJobFeaturePattern() {
		return this.jobFeaturePattern;
	}

	public String getJobReleasePattern() {
		return this.jobReleasePattern;
	}

	public String getGitReleaseBranchName(String releaseVersion) {
		final Map<String, String> variables = new HashMap<String, String>(1);
		variables.put("releaseVersion", releaseVersion);
		return ProjectUtils.createIdentifier(replaceVariables(getGitReleaseBranchPattern(), variables));
	}

	public String getGitFeatureBranchName(String featureId, String featureDescription) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("featureId", featureId);
		variables.put("featureDescription", featureDescription);
		return ProjectUtils.createIdentifier(replaceVariables(getGitFeatureBranchPattern(), variables));
	}

	public String getFeatureJobName(String projectName, String featureId,
			String featureSubject) {
		final Map<String, String> variables = new HashMap<String, String>(3);
		variables.put("projectName", projectName);
		variables.put("featureId", featureId);
		variables.put("featureDescription", featureSubject);
		return replaceVariables(getJobFeaturePattern(), variables);
	}

	public String getReleaseJobName(String projectName, String releaseVersion) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("projectName", projectName);
		variables.put("releaseVersion", releaseVersion);
		return replaceVariables(getJobReleasePattern(), variables);
	}

	private String replaceVariables(String str, Map<String, String> variables) {
		final StrSubstitutor sub = new StrSubstitutor(variables);
		sub.setVariablePrefix("%{");
		return sub.replace(str);
	}

}
