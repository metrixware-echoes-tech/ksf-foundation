package fr.echoes.labs.komea.foundation.plugins.jenkins.services;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
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
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

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

    @Autowired
    protected JenkinsConfigurationService configurationService;
    
    @Autowired
    protected JenkinsTemplateService templateService;
    
    @Autowired
    protected JenkinsNameResolver nameResolver;

    private static String getTemplateName(final ProjectDto projectDTO) {
    	if (projectDTO.getOtherAttributes().containsKey(ProjectExtensionConstants.JOB_TEMPLATE)) {
    		return (String) projectDTO.getOtherAttributes().get(ProjectExtensionConstants.JOB_TEMPLATE);
    	}
    	return null;
    }
    
    private URI getJenkinsUri() throws URISyntaxException {
        return new URI(this.configurationService.getUrl());
    }
    
    private JenkinsHttpClient newClient() throws URISyntaxException {
    	return new JenkinsHttpClient(getJenkinsUri());
    }
    
    private JenkinsServer createJenkinsClient() throws URISyntaxException {
        return new JenkinsServer(newClient());
    }
    
    @Override
    public void createProject(ProjectDto projectDTO) throws JenkinsExtensionException {
        try {

            final String projectName = projectDTO.getName();

            final String masterJobName = this.nameResolver.getJobName(projectDTO, MASTER);
            final String developJobName = this.nameResolver.getJobName(projectDTO, DEVELOP);
            final String templateName = getTemplateName(projectDTO);
            final String folderJobName = this.nameResolver.getFolderJobName(projectDTO);
            	
            final List<String> jobs = Lists.newArrayList();

            if (useFolder()) {
                createFolder(newClient(), folderJobName);
                updateFolderDisplayName(folderJobName, projectName);
                jobs.add(folderJobName);
            }
            
            createJob(templateName, projectDTO, masterJobName, MASTER);
            createJob(templateName, projectDTO, developJobName, DEVELOP);

            jobs.add(masterJobName);
            jobs.add(developJobName);
            projectDTO.getOtherAttributes().put(ProjectExtensionConstants.CI_JOBS_KEY, jobs);
            projectDTO.getOtherAttributes().put(ProjectExtensionConstants.JENKINS_KEY, folderJobName);

        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to create Jenkins job", e);
        }
    }

    private void createFolder(JenkinsHttpClient jenkinsHttpClient,
            String folderJobName) throws IOException {
        LOGGER.info("[jenkins] creating folder '{}'", folderJobName);
        final ImmutableMap<String, String> params = ImmutableMap.of(
                "mode", "com.cloudbees.hudson.plugins.folder.Folder",
                "name", folderJobName,
                "from", "",
                "Submit", "OK");
        jenkinsHttpClient.post_form("/createItem?", params, false);
        LOGGER.info("[jenkins] folder '{}' created", folderJobName);
    }

    private void updateFolderDisplayName(final String jobName, String projectName) throws JenkinsExtensionException {
        try {
            final JenkinsServer jenkins = this.createJenkinsClient();
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

    private FolderJob getProjectFolder(final JenkinsServer jenkins, final String folderName) throws IOException {
        final JobWithDetails root = jenkins.getJob(folderName);
        final Optional<FolderJob> projectFolder = jenkins.getFolderJob(root);
        final FolderJob folderJob = projectFolder.get();
        return folderJob;
    }

    @Override
    public void deleteProject(final ProjectDto project) throws JenkinsExtensionException {
        try {
            if (this.useFolder()) {
                this.deleteJob(null, this.nameResolver.getFolderJobName(project));
            } else {
                // TODO
                // comment savoir quels sont tous les jobs qui ont ete crees pour ce projet ???
            }
        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to delete Jenkins job", e);
        }
    }

    @Override
    public List<JenkinsBuildInfo> getBuildInfo(final ProjectDto project)
            throws JenkinsExtensionException {
        final JenkinsServer jenkins;
        try {

            jenkins = this.createJenkinsClient();

            final boolean useFolder = useFolder();
            final String projectName = project.getName();
            final String rootFolder = this.nameResolver.getFolderJobName(project);

            final int builsdPerJobLimit = this.configurationService.getBuilsdPerJobLimit();

            final List<Build> builds;
            if (useFolder) {
                builds = this.getFolderBuilds(jenkins, rootFolder, builsdPerJobLimit);
            } else {
                builds = Lists.newArrayList();
                builds.addAll(getJobBuilds(jenkins, rootFolder, this.nameResolver.getJobName(project, DEVELOP), useFolder, builsdPerJobLimit));
                builds.addAll(getJobBuilds(jenkins, rootFolder, this.nameResolver.getJobName(project, MASTER), useFolder, builsdPerJobLimit));
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

   

    private List<Build> getJobBuilds(JenkinsServer jenkins, String rootFolder, String jobName, boolean useFolder, int builsdPerJobLimit)
            throws IOException, JenkinsExtensionException {

        final JobWithDetails root = this.getJobWithDetails(jenkins, rootFolder, jobName, useFolder);

        final List<Build> builds = root.getBuilds();
        if (builds.size() > builsdPerJobLimit) {
            return Lists.newArrayList(builds.subList(0, builsdPerJobLimit));
        }
        return builds;
    }

    private List<Build> getFolderBuilds(JenkinsServer jenkins, String folderName, int builsdPerJobLimit)
            throws IOException, JenkinsExtensionException {

        final List<Build> builds = Lists.newArrayList();
        final FolderJob projectFolder = getProjectFolder(jenkins, folderName);
        final Map<String, Job> jobs = projectFolder.getJobs();
        for (final Map.Entry<String, Job> entry : jobs.entrySet()) {
            final Job job = entry.getValue();
            final JobWithDetails details = job.details();
            final List<Build> jobBuilds = details.getBuilds();
            if (jobBuilds.size() > builsdPerJobLimit) {
                builds.addAll(Lists.newArrayList(jobBuilds.subList(0, builsdPerJobLimit)));
            } else {
                builds.addAll(jobBuilds);
            }
        }
        return builds;
    }

    private JobWithDetails getJobWithDetails(final JenkinsServer jenkins, final String rootFolder,
            String jobName, boolean useFolder) throws IOException, JenkinsExtensionException {
        final JobWithDetails root;
        if (useFolder) {
            final FolderJob projectFolder = this.getProjectFolder(jenkins, rootFolder);
            root = jenkins.getJob(projectFolder, jobName);
        } else {
            root = jenkins.getJob(jobName);
        }

        if (root == null) {
            throw new JenkinsExtensionException("The job \"" + jobName + "\" doesn't exist");
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
        final String gitBranchName = this.nameResolver.getGitReleaseBranchName(releaseVersion);
        final String jobName = this.nameResolver.getReleaseJobName(project, releaseVersion);
        final String templateName = getTemplateName(project);
        createJob(templateName, project, jobName, gitBranchName);
        JenkinsUtils.registerJob(project, jobName);
    }

    @Override
    public void createFeature(final ProjectDto project, String featureId, String featureSubject) throws JenkinsExtensionException {
        final String jobName = this.nameResolver.getFeatureJobName(project, featureId, featureSubject);
        final String gitBranchName = this.nameResolver.getGitFeatureBranchName(featureId, featureSubject);
        final String templateName = getTemplateName(project);
        createJob(templateName, project, jobName, gitBranchName);
        JenkinsUtils.registerJob(project, jobName);
    }

    private void createJob(final String templateName, ProjectDto project, String jobName, String gitBranchName) throws JenkinsExtensionException {
        final JenkinsServer jenkins;
        try {
            jenkins = this.createJenkinsClient();

            final String scmUrl = this.nameResolver.getProjectScmUrl(project);
            final String displayName = this.nameResolver.getDisplayName(project.getName(), gitBranchName);
            final String resolvedXmlConfig = this.templateService.createConfigXml(templateName, displayName, scmUrl, gitBranchName);

            if (useFolder()) {
                final FolderJob projectFolder = getProjectFolder(jenkins, this.nameResolver.getFolderJobName(project));
                jenkins.createJob(projectFolder, jobName, resolvedXmlConfig, false);
            } else {
                jenkins.createJob(jobName, resolvedXmlConfig, false);
            }
            
            LOGGER.info("[jenkins] job '{}' created.", jobName);

        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to create Jenkins job", e);
        }
    }

    private void deleteJob(String folderJobName, String jobName) throws JenkinsExtensionException {
        String path = "";
        try {
            final JenkinsHttpClient jenkinsHttpClient = newClient();
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
    public String getJobId(final String jobName) throws JenkinsExtensionException {

        Objects.requireNonNull(jobName);
        
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

    private JenkinsBuildInfo getJobLastBuildInfo(final String rootFolder, final String jobName) throws JenkinsExtensionException {
        try {

            final JenkinsServer jenkins = this.createJenkinsClient();

            final boolean useFolder = this.useFolder();
            final JobWithDetails jobWithDetails = getJobWithDetails(jenkins, rootFolder, jobName, useFolder);
            final Build lastBuild = jobWithDetails.getLastBuild();

            return lastBuild == null ? null : createJenkinsBuildInfo(lastBuild, jobName);

        } catch (final JenkinsExtensionException e) {
            throw e;
        } catch (final Exception e) {
            throw new JenkinsExtensionException("Failed to retrieve build history", e);
        }
    }
    
    @Override
    public JenkinsBuildInfo getReleaseStatus(final ProjectDto project, final String releaseVersion) throws JenkinsExtensionException {
    	final String folderJobName = this.nameResolver.getFolderJobName(project);
        final String releaseJobName = this.nameResolver.getReleaseJobName(project, releaseVersion);
        return getJobLastBuildInfo(folderJobName, releaseJobName);
    }
    
    @Override
    public JenkinsBuildInfo getFeatureStatus(final ProjectDto project, final String featureId, final String featureSubject) throws JenkinsExtensionException {
    	final String folderJobName = this.nameResolver.getFolderJobName(project);
        final String featureJobName = this.nameResolver.getFeatureJobName(project, featureId, featureSubject);
        return getJobLastBuildInfo(folderJobName, featureJobName);
    }

    @Override
    public void deleteFeatureJob(final ProjectDto project, final String featureId,
            final String featureSubject) throws JenkinsExtensionException {
        final String featureJobName = this.nameResolver.getFeatureJobName(project, featureId, featureSubject);
        final String folderJobName = this.nameResolver.getFolderJobName(project);
        deleteJob(folderJobName, featureJobName);
    }

    @Override
    public void deleteReleaseJob(final ProjectDto project, String releaseName) throws JenkinsExtensionException {
        final String releaseJobName = this.nameResolver.getReleaseJobName(project, releaseName);
        final String folderJobName = this.nameResolver.getFolderJobName(project);
        deleteJob(folderJobName, releaseJobName);
    }

}
