package fr.echoes.labs.ksf.cc.ui.views.projects;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.plugins.utils.ConfigurationPropertiesUtils;

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

    public String getGitReleaseBranchName(String releaseVersion) {
        final Map<String, String> variables = new HashMap<String, String>(1);
        variables.put("releaseVersion", ProjectUtils.createIdentifier(releaseVersion));
        return ConfigurationPropertiesUtils.replaceVariables(gitReleaseBranchPattern, variables);
    }

    public String getGitFeatureBranchName(String featureId, String featureDescription) {
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("featureId", ProjectUtils.createIdentifier(featureId));
        variables.put("featureDescription", ProjectUtils.createIdentifier(featureDescription));
        return ConfigurationPropertiesUtils.replaceVariables(gitFeatureBranchPattern, variables);
    }

    public String getFeatureJobName(String projectName, String featureId,
            String featureSubject) {
        final Map<String, String> variables = new HashMap<String, String>(3);
        variables.put("projectName", ProjectUtils.createIdentifier(projectName));
        variables.put("featureId", ProjectUtils.createIdentifier(featureId));
        variables.put("featureDescription", ProjectUtils.createIdentifier(featureSubject));
        return ConfigurationPropertiesUtils.replaceVariables(jobFeaturePattern, variables);
    }

    public String getReleaseJobName(String projectName, String releaseVersion) {
        final Map<String, String> variables = new HashMap<String, String>(2);
        variables.put("projectName", ProjectUtils.createIdentifier(projectName));
        variables.put("releaseVersion", ProjectUtils.createIdentifier(releaseVersion));
        return ConfigurationPropertiesUtils.replaceVariables(jobReleasePattern, variables);
    }

}
