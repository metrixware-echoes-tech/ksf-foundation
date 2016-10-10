package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.client.util.EncodingUtils;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.komea.foundation.plugins.jenkins.utils.JenkinsUtils;
import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Spring Service for working with the foreman API.
 *
 * @author dcollard
 *
 */
@Service
public class JenkinsService implements IJenkinsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsService.class);

    private static final String DEVELOP = "develop";
    private static final String MASTER = "master";

    private static String getTemplateName(final ProjectDto projectDTO) {
        if (projectDTO.getOtherAttributes().containsKey(ProjectExtensionConstants.JOB_TEMPLATE)) {
            return (String) projectDTO.getOtherAttributes().get(ProjectExtensionConstants.JOB_TEMPLATE);
        }
        return null;
    }

    @Autowired
    private JenkinsConfigurationService configurationService;

    @Autowired
    private JenkinsTemplateService templateService;

    @Override
    public void createProject(ProjectDto projectDTO) throws JenkinsExtensionException {
        try {

            final String projectName = projectDTO.getName();
            final String masterJobName = this.getJobName(projectName, MASTER);
            final String developJobName = this.getJobName(projectName, DEVELOP);
            final String templateName = getTemplateName(projectDTO);

            final List<String> jobs = Lists.newArrayList();

            if (this.useFolder()) {
                this.createFolder(projectName);
                this.updateFolderDisplayName(projectName);
                jobs.add(this.getFolderJobName(projectName));
            }

            this.createJob(templateName, projectName, masterJobName, MASTER);
            this.createJob(templateName, projectName, developJobName, DEVELOP);

            jobs.add(masterJobName);
            jobs.add(developJobName);
            projectDTO.getOtherAttributes().put(ProjectExtensionConstants.CI_JOBS_KEY, jobs);

        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to create Jenkins job", e);
        }
    }

    private void createFolder(final String projectName) throws URISyntaxException, IOException {
        final String jobName = this.getFolderJobName(projectName);
        LOGGER.info("[jenkins] creating folder '{}'", jobName);
        this.createJenkinsClient().createFolder(jobName, false);
        LOGGER.info("[jenkins] folder '{}' created", jobName);
    }

    private String getFolderJobName(final String projectName) {
        return ProjectUtils.createIdentifier(projectName);
    }

    private void updateFolderDisplayName(final String projectName) throws JenkinsExtensionException {
        try {
            final JenkinsServer jenkins = this.createJenkinsClient();
            final String jobName = this.getFolderJobName(projectName);
            final String jobXml = jenkins.getJobXml(jobName);
            final int index = jobXml.indexOf("<properties");
            final String jobNewXml = jobXml.substring(0, index)
                    + "<displayName>" + projectName + "</displayName>"
                    + jobXml.substring(index);
            jenkins.updateJob(jobName, jobNewXml, false);
        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to update Jenkins job", e);
        }
    }

    private URI getJenkinsUri() throws URISyntaxException {
        return new URI(this.configurationService.getUrl());
    }

    private FolderJob getProjectFolder(final JenkinsServer jenkins, String projectName) throws IOException {
        final JobWithDetails root = jenkins.getJob(this.getFolderJobName(projectName));
        final Optional<FolderJob> projectFolder = jenkins.getFolderJob(root);
        final FolderJob folderJob = projectFolder.get();
        return folderJob;
    }

    @Override
    public void deleteProject(final String projectName) throws JenkinsExtensionException {
        try {
            if (this.useFolder()) {
                this.deleteJob(null, this.getFolderJobName(projectName));
            } else {
                // TODO
                // comment savoir quels sont tous les jobs qui ont ete crees pour ce projet ???
            }
        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to delete Jenkins job", e);
        }
    }

    @Override
    public List<JenkinsBuildInfo> getBuildInfo(String projectName)
            throws JenkinsExtensionException {
        final JenkinsServer jenkins;
        try {

            jenkins = this.createJenkinsClient();

            final boolean useFolder = this.useFolder();

            final int builsdPerJobLimit = this.configurationService.getBuilsdPerJobLimit();

            final List<Build> builds;
            if (useFolder) {
                builds = this.getFolderBuilds(jenkins, projectName, builsdPerJobLimit);
            } else {
                builds = Lists.newArrayList();
                builds.addAll(this.getJobBuilds(jenkins, projectName, this.getJobName(projectName, DEVELOP), useFolder, builsdPerJobLimit));
                builds.addAll(this.getJobBuilds(jenkins, projectName, this.getJobName(projectName, MASTER), useFolder, builsdPerJobLimit));
            }

            final List<JenkinsBuildInfo> buildsInfo = new ArrayList<>(builds.size());
            for (final Build build : builds) {

                buildsInfo.add(this.createJenkinsBuildInfo(build, projectName));
            }
            return buildsInfo;
        } catch (final JenkinsExtensionException e) {
            throw e;
        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to retrieve build history", e);
        }
    }

    private JenkinsServer createJenkinsClient() throws URISyntaxException {
        final JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(this.getJenkinsUri());
        return new JenkinsServer(jenkinsHttpClient);
    }

    private List<Build> getJobBuilds(JenkinsServer jenkins, String projectName, String jobName, boolean useFolder, int builsdPerJobLimit)
            throws IOException, JenkinsExtensionException {

        final JobWithDetails root = this.getJobWithDetails(jenkins, projectName, jobName,
                useFolder);

        final List<Build> builds = root.getBuilds();
        if (builds.size() > builsdPerJobLimit) {
            return new ArrayList<>(builds.subList(0, builsdPerJobLimit));
        }
        return builds;
    }

    private List<Build> getFolderBuilds(JenkinsServer jenkins, String projectName, int builsdPerJobLimit)
            throws IOException, JenkinsExtensionException {

        final List<Build> builds = Lists.newArrayList();
        final FolderJob projectFolder = this.getProjectFolder(jenkins, projectName);
        final Map<String, Job> jobs = projectFolder.getJobs();
        for (final Map.Entry<String, Job> entry : jobs.entrySet()) {
            final Job job = entry.getValue();
            final JobWithDetails details = job.details();
            final List<Build> jobBuilds = details.getBuilds();
            if (jobBuilds.size() > builsdPerJobLimit) {
                builds.addAll(new ArrayList<>(jobBuilds.subList(0, builsdPerJobLimit)));
            } else {
                builds.addAll(jobBuilds);
            }
        }
        return builds;
    }

    private JobWithDetails getJobWithDetails(JenkinsServer jenkins, String projectName,
            String jobName, boolean useFolder) throws IOException,
            JenkinsExtensionException {
        final JobWithDetails root;
        if (useFolder) {
            final FolderJob projectFolder = this.getProjectFolder(jenkins, projectName);
            root = jenkins.getJob(projectFolder, jobName);
        } else {
            root = jenkins.getJob(jobName);
        }

        if (root == null) {
            throw new JenkinsExtensionException("The job \"" + projectName + "\" doesn't exist");
        }
        return root;
    }

    private JenkinsBuildInfo createJenkinsBuildInfo(Build build, String jobName) throws IOException {
        final int buildNumber = build.getNumber();
        final String buildUrl = build.getUrl();
        final BuildWithDetails details = build.details();
        final long timestamp = details.getTimestamp();
        final int duration = details.getDuration();
        final String result = details.getResult().name();
        return new JenkinsBuildInfo(buildNumber, details.getFullDisplayName(), timestamp, duration, buildUrl, result);
    }

    @Override
    public void createRelease(final ProjectDto project, String releaseVersion) throws JenkinsExtensionException {
        final String projectName = project.getName();
        final String gitBranchName = this.getGitReleaseBranchName(releaseVersion);
        final String jobName = this.getReleaseJobName(projectName, releaseVersion);
        final String templateName = getTemplateName(project);
        this.createJob(templateName, projectName, jobName, gitBranchName);
        JenkinsUtils.registerJob(project, jobName);
    }

    @Override
    public void createFeature(final ProjectDto project, String featureId, String featureSubject) throws JenkinsExtensionException {
        final String projectName = project.getName();
        final String jobName = this.getFeatureJobName(projectName, featureId, featureSubject);
        final String gitBranchName = this.getGitFeatureBranchName(featureId, featureSubject);
        final String templateName = getTemplateName(project);
        this.createJob(templateName, projectName, jobName, gitBranchName);
        JenkinsUtils.registerJob(project, jobName);
    }

    private void createJob(final String templateName, String projectName, String jobName, String gitBranchName) throws JenkinsExtensionException {
        final JenkinsServer jenkins;
        try {
            jenkins = this.createJenkinsClient();

            final String scmUrl = this.getProjectScmUrl(projectName);
            final String displayName = this.getDisplayName(projectName, gitBranchName);
            final String resolvedXmlConfig = this.templateService.createConfigXml(templateName, displayName, scmUrl, gitBranchName);

            if (this.useFolder()) {
                final FolderJob projectFolder = this.getProjectFolder(jenkins, projectName);
                jenkins.createJob(projectFolder, jobName, resolvedXmlConfig, false);
            } else {
                jenkins.createJob(jobName, resolvedXmlConfig, false);
            }

        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to create Jenkins job", e);
        }
    }

    private void deleteJob(String folderJobName, String jobName) throws JenkinsExtensionException {
        String path = "";
        try {
            final JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(this.configurationService.getUrl()));
            if (folderJobName != null) {
                path += "/job/" + EncodingUtils.encode(folderJobName);
            }
            path += "/job/" + EncodingUtils.encode(jobName) + "/doDelete";
            jenkinsHttpClient.post(path, false);
        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to delete Jenkins job with path " + path, e);
        }
    }

    private boolean useFolder() {
        return this.configurationService.useFolders();
    }

    @Override
    public String getJobId(String projectName) throws JenkinsExtensionException {

        Objects.requireNonNull(projectName);
        final String jobName = this.getFolderJobName(projectName);

        try {

            final JenkinsServer jenkinsClient = this.createJenkinsClient();
            final Map<String, Job> jobs = jenkinsClient.getJobs();
            for (final Map.Entry<String, Job> entry : jobs.entrySet()) {
                final Job job = entry.getValue();
                if (jobName.equals(job.getName())) {
                    return entry.getKey();
                }
            }

        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to retrieve build history", e);
        }

        return null;
    }

    private String getJobName(String projectName, String branchName) {
        final Map<String, String> variables = new HashMap<>(2);
        variables.put("projectName", ProjectUtils.createIdentifier(projectName));
        variables.put("branchName", ProjectUtils.createIdentifier(branchName));
        return this.replaceVariables(this.configurationService.getJobNamePattern(), variables);

    }

    private String getDisplayName(final String projectName, final String branchName) {
        final Map<String, String> variables = new HashMap<>(2);
        variables.put("projectName", projectName);
        variables.put("branchName", branchName);
        return this.replaceVariables(this.configurationService.getJobNamePattern(), variables);
    }

    private String getReleaseJobName(String projectName, String releaseVersion) {
        final Map<String, String> variables = new HashMap<>(2);
        variables.put("projectName", ProjectUtils.createIdentifier(projectName));
        variables.put("releaseVersion", ProjectUtils.createIdentifier(releaseVersion));
        return this.replaceVariables(this.configurationService.getJobReleasePattern(), variables);
    }

    private String getFeatureJobName(String projectName, String featureId,
            String featureSubject) {
        final Map<String, String> variables = new HashMap<>(3);
        variables.put("projectName", ProjectUtils.createIdentifier(projectName));
        variables.put("featureId", ProjectUtils.createIdentifier(featureId));
        variables.put("featureDescription", ProjectUtils.createIdentifier(featureSubject));
        return this.replaceVariables(this.configurationService.getJobFeaturePattern(), variables);
    }

    private String getGitReleaseBranchName(String releaseVersion) {
        final Map<String, String> variables = new HashMap<>(1);
        variables.put("releaseVersion", ProjectUtils.createIdentifier(releaseVersion));
        return this.replaceVariables(this.configurationService.getGitReleaseBranchPattern(), variables);
    }

    private String getGitFeatureBranchName(String featureId, String featureDescription) {
        final Map<String, String> variables = new HashMap<>(2);
        variables.put("featureId", ProjectUtils.createIdentifier(featureId));
        variables.put("featureDescription", ProjectUtils.createIdentifier(featureDescription));
        return this.replaceVariables(this.configurationService.getGitFeatureBranchPattern(), variables);
    }

    private String getProjectScmUrl(String projectName) {
        final Map<String, String> variables = new HashMap<>(2);
        variables.put("scmUrl", this.configurationService.getScmUrl());
        variables.put("projectKey", ProjectUtils.createIdentifier(projectName));
        return this.replaceVariables(this.configurationService.getProjectScmUrlPattern(), variables);
    }

    private String replaceVariables(String str, Map<String, String> variables) {
        final StrSubstitutor sub = new StrSubstitutor(variables);
        sub.setVariablePrefix("%{");
        return sub.replace(str);
    }

    private JenkinsBuildInfo getJobLastBuildInfo(String projectName, String jobName) throws JenkinsExtensionException {
        try {

            final JenkinsServer jenkins = this.createJenkinsClient();

            final boolean useFolder = this.useFolder();

            final JobWithDetails jobWithDetails = this.getJobWithDetails(jenkins, projectName, jobName, useFolder);
            final Build lastBuild = jobWithDetails.getLastBuild();

            return lastBuild == null ? null : this.createJenkinsBuildInfo(lastBuild, projectName);

        } catch (final JenkinsExtensionException e) {
            throw e;
        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to retrieve build history", e);
        }
    }

    @Override
    public JenkinsBuildInfo getFeatureStatus(String projectName, String featureId, String featureSubject) throws JenkinsExtensionException {
        final String featureJobName = this.getFeatureJobName(projectName, featureId, featureSubject);
        return this.getJobLastBuildInfo(projectName, featureJobName);
    }

    @Override
    public JenkinsBuildInfo getReleaseStatus(String projectName, String releaseVersion) throws JenkinsExtensionException {
        final String releaseJobName = this.getReleaseJobName(projectName, releaseVersion);
        return this.getJobLastBuildInfo(projectName, releaseJobName);
    }

    @Override
    public void deleteFeatureJob(String projectName, String featureId,
            String featureSubject) throws JenkinsExtensionException {
        final String featureJobName = this.getFeatureJobName(projectName, featureId, featureSubject);
        final String folderJobName = this.useFolder() ? this.getFolderJobName(projectName) : null;
        this.deleteJob(folderJobName, featureJobName);
    }

    @Override
    public void deleteReleaseJob(String projectName, String releaseName)
            throws JenkinsExtensionException {
        final String releaseJobName = this.getReleaseJobName(projectName, releaseName);
        final String folderJobName = this.useFolder() ? this.getFolderJobName(projectName) : null;
        this.deleteJob(folderJobName, releaseJobName);
    }

}
