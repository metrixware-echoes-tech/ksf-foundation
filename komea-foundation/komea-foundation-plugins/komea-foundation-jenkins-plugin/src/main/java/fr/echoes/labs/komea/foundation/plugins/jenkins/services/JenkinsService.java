package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.Resources;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.client.util.EncodingUtils;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;



/**
 * Spring Service for working with the foreman API.
 *
 * @author dcollard
 *
 */
@Service
public class JenkinsService implements IJenkinsService {

	private static final String DEVELOP = "develop";

	private static final String MASTER = "master";

	private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsService.class);

	@Autowired
	private JenkinsConfigurationService configurationService;

	@Override
	public void createProject(String projectName)
			throws JenkinsExtensionException {
		final JenkinsServer jenkins;
		try {
			jenkins = createJenkinsClient();

			final String scmUrl = getProjectScmUrl(projectName);

			if (useFoder()) {
				jenkins.createFolder(projectName);

				final FolderJob projectFolder = getProjectParentFolder(jenkins, projectName);

				String resolvedXmlConfig = createConfigXml(projectName, scmUrl, MASTER);
				jenkins.createJob(projectFolder, getJobName(projectName, MASTER), resolvedXmlConfig, false);

				resolvedXmlConfig = createConfigXml(projectName, scmUrl, DEVELOP);
				jenkins.createJob(projectFolder, getJobName(projectName, DEVELOP), resolvedXmlConfig, false);

			} else {
				String resolvedXmlConfig = createConfigXml(projectName, scmUrl, MASTER);
				jenkins.createJob(getJobName(projectName, MASTER), resolvedXmlConfig, false);

				resolvedXmlConfig = createConfigXml(projectName, scmUrl, DEVELOP);
				jenkins.createJob(getJobName(projectName, DEVELOP), resolvedXmlConfig, false);
			}


		} catch (final Exception e) {
			throw new JenkinsExtensionException("Failed to create Jenkins job", e);
		}
	}

	private FolderJob getProjectParentFolder(final JenkinsServer jenkins, String projectName) throws IOException {
		final JobWithDetails root = jenkins.getJob(projectName);
		final Optional<FolderJob> projectFolder = jenkins.getFolderJob(root);
		return projectFolder.get();
	}

	private String getJobName(String projectName, String branchName) {
		final String string = projectName + " - " + branchName;
		return string.replace('/', ' ');
	}

	private JenkinsServer createJenkinsClient() throws URISyntaxException {
		return new JenkinsServer(new URI(this.configurationService.getUrl()));
	}

	private String createConfigXml(String projectName, String scmUrl, String branchName) throws IOException {

		final Map<String, String> variables = new HashMap<String, String>();

		variables.put("scmUrl", scmUrl);
		variables.put("branchName", branchName);
		variables.put("buildScript", this.configurationService.getBuildScript());
		variables.put("publishScript", this.configurationService.getPublishScript());

		final URL url = com.google.common.io.Resources.getResource(this.configurationService.getTemplateName());
		return substituteText(url, variables);
	}

	/**
	 * Replaces all the occurrences of variables with their matching values.
	 *
	 * @param url
	 * @param variables the map with the variables' values, can be null.
	 * @return
	 * @throws IOException
	 */
	private String substituteText(URL url, Map<String, String> variables) throws IOException {
		final String templateXml = Resources.toString(url, Charsets.UTF_8);
		final StrSubstitutor sub = new StrSubstitutor(variables);
		final String resolvedXml = sub.replace(templateXml);
		return resolvedXml;
	}

	@Override
	public void deleteProject(String projectName)
			throws JenkinsExtensionException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<JenkinsBuildInfo> getBuildInfo(String projectName)
			throws JenkinsExtensionException {
		final JenkinsServer jenkins;
		try {

			jenkins = createJenkinsClient();

			final boolean useFolder = useFoder();

			final int builsdPerJobLimit = this.configurationService.getBuilsdPerJobLimit();

			final List<Build> builds = getJobBuilds(jenkins, projectName, MASTER, useFolder, builsdPerJobLimit);
			builds.addAll(getJobBuilds(jenkins, projectName, DEVELOP, useFolder, builsdPerJobLimit));

			final List<JenkinsBuildInfo> buildsInfo = new ArrayList<JenkinsBuildInfo>(builds.size());
			for (final Build build : builds) {

				buildsInfo.add(createJenkinsBuildInfo(build, projectName));
			}
			return buildsInfo;
		} catch (final JenkinsExtensionException e) {
			throw e;
		} catch (final Exception e) {
			throw new JenkinsExtensionException("Failed to retrieve build history", e);
		}
	}

	private List<Build> getJobBuilds(JenkinsServer jenkins, String projectName, String branchName, boolean useFolder, int builsdPerJobLimit)
			throws IOException, JenkinsExtensionException {

		final JobWithDetails root = getJobWithDetails(jenkins, projectName, branchName,
				useFolder);

		final List<Build> builds = root.getBuilds();
		if (builds.size() > builsdPerJobLimit) {
			return  new ArrayList<Build>(builds.subList(0, builsdPerJobLimit));
		}
		return builds;
	}

	private JobWithDetails getJobWithDetails(JenkinsServer jenkins, String projectName,
			String branchName, boolean useFolder) throws IOException,
			JenkinsExtensionException {
		final JobWithDetails root;
		if (useFolder) {
			final FolderJob projectFolder = getProjectParentFolder(jenkins, projectName);
			root = jenkins.getJob(projectFolder, getJobName(projectName, branchName));
		} else {
			root = jenkins.getJob(getJobName(projectName, branchName));
		}

		if (root == null) {
			throw new JenkinsExtensionException("The job \"" + projectName +"\" doesn't exist");
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
		return new JenkinsBuildInfo(buildNumber, details.getFullDisplayName(), timestamp, duration,  buildUrl, result);
	}

	@Override
	public void createRelease(String projectName, String releaseVersion) throws JenkinsExtensionException {
		final String branchName = getReleaseBranchName(projectName, releaseVersion);
		final String gitBranchName = getGitReleaseBranchName(projectName, releaseVersion);
		createJob(projectName, branchName, gitBranchName);
	}

	@Override
	public void createFeature(String projectName, String featureId, String featureSubject) throws JenkinsExtensionException {
		final String branchName = getFeatureBranchName(projectName, featureId, featureSubject);
		final String gitBranchName = getGitFeatureBranchName(projectName, featureId, featureSubject);
		createJob(projectName, branchName, gitBranchName);
	}

	private void createJob(String projectName, String branchName, String gitBranchName) throws JenkinsExtensionException {
		final JenkinsServer jenkins;
		try {
			jenkins = createJenkinsClient();

			final String scmUrl = getProjectScmUrl(projectName);

			if (useFoder()) {
				final FolderJob projectFolder = getProjectParentFolder(jenkins, projectName);

				final String resolvedXmlConfig = createConfigXml(projectName, scmUrl, branchName);
				jenkins.createJob(projectFolder, getJobName(projectName, branchName), resolvedXmlConfig, false);
			} else {
				final String resolvedXmlConfig = createConfigXml(projectName, scmUrl, branchName);
				jenkins.createJob(getJobName(projectName, branchName), resolvedXmlConfig, false);
			}

		} catch (final Exception e) {
			throw new JenkinsExtensionException("Failed to create Jenkins job", e);
		}
	}

	private void deleteJob(String projectName, String branchName) throws JenkinsExtensionException {

		String jobName = EncodingUtils.encode(getJobName(projectName, branchName));
		try {
			final JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(this.configurationService.getUrl()));
			if (useFoder()) {
				jobName = EncodingUtils.encode(projectName) + "/job/" + jobName;
			}

			jenkinsHttpClient.post("/job/" + jobName + "/doDelete");

		} catch (final Exception e) {
			throw new JenkinsExtensionException("Failed to delete Jenkins job", e);
		}
	}

	private boolean useFoder() {
		return this.configurationService.useFolders();
	}

	private String getReleaseBranchName(String projectName, String releaseVersion) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("projectName", projectName);
		variables.put("releaseVersion", releaseVersion);
		return replaceVariables(this.configurationService.getJobReleasePattern(), variables);
	}

	private String getFeatureBranchName(String projectName, String featureId,
			String featureSubject) {
		final Map<String, String> variables = new HashMap<String, String>(3);
		variables.put("projectName", projectName);
		variables.put("featureId", featureId);
		variables.put("featureDescription", featureSubject);
		return replaceVariables(this.configurationService.getJobFeaturePattern(), variables);
	}

	@Override
	public String getJobId(String projectName) throws JenkinsExtensionException {

		Objects.requireNonNull(projectName);

		try {

			final JenkinsServer jenkinsClient = createJenkinsClient();
			final Map<String, Job> jobs = jenkinsClient.getJobs();
			for (final Map.Entry<String, Job> entry : jobs.entrySet()) {
				final Job job = entry.getValue();
				if (projectName.equals(job.getName())) {
					return entry.getKey();
				}
			}

		} catch (final Exception e) {
			throw new JenkinsExtensionException("Failed to retrieve build history", e);
		}

		return null;
	}

	private String getGitReleaseBranchName(String projectName, String releaseVersion) {
		final Map<String, String> variables = new HashMap<String, String>(1);
		variables.put("releaseVersion", releaseVersion);
		return replaceVariables(this.configurationService.getGitReleaseBranchPattern(), variables);
	}

	private String getGitFeatureBranchName(String projectName, String featureId, String featureDescription) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("featureId", featureId);
		variables.put("featureDescription", featureDescription);
		return replaceVariables(this.configurationService.getGitFeatureBranchPattern(), variables);
	}

	private String getProjectScmUrl(String projectName) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("scmUrl", this.configurationService.getScmUrl());
		variables.put("projectName", projectName);
		return replaceVariables(this.configurationService.getProjectScmUrlPattern(), variables);
	}

	private String replaceVariables(String str, Map<String, String> variables) {
		final StrSubstitutor sub = new StrSubstitutor(variables);
		sub.setVariablePrefix("%{");
		return sub.replace(str);
	}

	private JenkinsBuildInfo getBranchLastBuildInfo(String projectName, String branchName) throws JenkinsExtensionException {
		try {

			final JenkinsServer jenkins = createJenkinsClient();

			final boolean useFolder = useFoder();

			final JobWithDetails jobWithDetails = getJobWithDetails(jenkins, projectName, branchName, useFolder);
			final Build lastBuild = jobWithDetails.getLastBuild();

			return lastBuild == null ? null : createJenkinsBuildInfo(lastBuild, projectName);

		} catch (final JenkinsExtensionException e) {
			throw e;
		} catch (final Exception e) {
			throw new JenkinsExtensionException("Failed to retrieve build history", e);
		}
	}



	@Override
	public JenkinsBuildInfo getFeatureStatus(String projectName, String featureId, String description) throws JenkinsExtensionException {
		final String featureBranchName = getFeatureBranchName(projectName, featureId, description);
		return getBranchLastBuildInfo(projectName, featureBranchName);
	}

	@Override
	public JenkinsBuildInfo getReleaseStatus(String projectName, String releaseVersion) throws JenkinsExtensionException {
		final String releaseBranchName = getFeatureBranchName(projectName, releaseVersion, null);
		return getBranchLastBuildInfo(projectName, releaseBranchName);
	}

}
