package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("org.apache.http.conn.ssl.*")
@PrepareForTest(JenkinsService.class)
public class JenkinsServiceTest {

	private static final String JENKINS_URL = "http://localhost:8080/jenkins";
	private static final String SCM_URL = "http://localhost:8080/scm";
	private static final String DEFAULT_TEMPLATE = "job.xml";
	
	@Mock
	private JenkinsConfigurationService configuration;
	
	@Mock
	private JenkinsTemplateService templateService;
	
	@Mock
	private JenkinsHttpClient httpClient;
	
	@Mock
	private JenkinsServer server;
	
	private JenkinsService jenkinsService;
	
	@Captor
	private ArgumentCaptor<Map<String, String>> parameters;
	
	@Before
	public void setup() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		this.jenkinsService = new JenkinsService();
		this.jenkinsService.configurationService = this.configuration;
		this.jenkinsService.templateService = this.templateService;
		this.jenkinsService.nameResolver = new JenkinsNameResolver(this.configuration);
		
		Mockito.when(this.configuration.getUrl()).thenReturn(JENKINS_URL);
		Mockito.when(this.configuration.getScmUrl()).thenReturn(SCM_URL);
		Mockito.when(this.configuration.getJobNamePattern()).thenReturn("%{projectName}-%{branchName}");
		Mockito.when(this.configuration.getJobReleasePattern()).thenReturn("%{projectName}-release-%{releaseVersion}");
		Mockito.when(this.configuration.getJobFeaturePattern()).thenReturn("%{projectName}-feat-%{featureId}-%{featureDescription}");
		Mockito.when(this.configuration.getProjectScmUrlPattern()).thenReturn("%{scmUrl}/%{projectKey}.git");
		Mockito.when(this.configuration.getGitReleaseBranchPattern()).thenReturn("release-%{releaseVersion}");
		Mockito.when(this.configuration.getGitFeatureBranchPattern()).thenReturn("feat-%{featureId}-%{featureDescription}");
		
		PowerMockito.whenNew(JenkinsHttpClient.class).withAnyArguments().thenReturn(this.httpClient);
		PowerMockito.whenNew(JenkinsServer.class).withAnyArguments().thenReturn(this.server);
	}
	
	@Test
	public void testCreateProject() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", null);
		
		// and
		final String expectedJobName = "job_name";
		final String expectedMasterJobName = "job_name-master";
		final String expectedDevelopJobName = "job_name-develop";
		
		// and
		final String jobXML = newConfigXML(null);
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = new FolderJob();
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJobXml(expectedJobName)).thenReturn(jobXML);
		Mockito.when(this.server.getJob(expectedJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(this.templateService.createConfigXml(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn("");
		
		// when
		this.jenkinsService.createProject(project);
		
		// then
		Mockito.verify(this.httpClient).post_form(Matchers.eq("/createItem?"), parameters.capture(), Matchers.eq(false));
		Assert.assertEquals(expectedJobName, parameters.getValue().get("name"));
		
		// then
		Mockito.verify(this.server).updateJob(expectedJobName, newConfigXML(project.getName()), false);
		
		// then
		Mockito.verify(this.server).createJob(folderJob, expectedMasterJobName, "", false);
		Mockito.verify(this.server).createJob(folderJob, expectedDevelopJobName, "", false);
		
		// then
		final String generatedKey = (String) project.getOtherAttributes().get(ProjectExtensionConstants.JENKINS_KEY);
		Assert.assertEquals(expectedJobName, generatedKey);
	}
	
	@Test
	public void testGetBuildInfo() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", null);

		// and
		final String expectedJobName = "job_name";
		final String expectedMasterJobName = "job_name-master";
		final String expectedDevelopJobName = "job_name-develop";
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		final Job masterJob = Mockito.mock(Job.class);
		final JobWithDetails masterJobWithDetails = Mockito.mock(JobWithDetails.class);
		final Job developJob = Mockito.mock(Job.class);
		final JobWithDetails developJobWithDetails = Mockito.mock(JobWithDetails.class);
		final Map<String, Job> jobs = ImmutableMap.of(
				expectedMasterJobName, masterJob,
				expectedDevelopJobName, developJob
		);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(expectedJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(folderJob.getJobs()).thenReturn(jobs);
		Mockito.when(masterJob.details()).thenReturn(masterJobWithDetails);
		Mockito.when(developJob.details()).thenReturn(developJobWithDetails);
		
		// when
		this.jenkinsService.getBuildInfo(project);
		
	}
	
	@Test
	public void testGetBuildInfoWithCustomName() throws IOException, JenkinsExtensionException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", "my_custom_key");

		// and
		final String expectedJobName = "my_custom_key";
		final String expectedMasterJobName = "job_name-master";
		final String expectedDevelopJobName = "job_name-develop";
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		final Job masterJob = Mockito.mock(Job.class);
		final JobWithDetails masterJobWithDetails = Mockito.mock(JobWithDetails.class);
		final Job developJob = Mockito.mock(Job.class);
		final JobWithDetails developJobWithDetails = Mockito.mock(JobWithDetails.class);
		final Map<String, Job> jobs = ImmutableMap.of(
				expectedMasterJobName, masterJob,
				expectedDevelopJobName, developJob
		);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(expectedJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(folderJob.getJobs()).thenReturn(jobs);
		Mockito.when(masterJob.details()).thenReturn(masterJobWithDetails);
		Mockito.when(developJob.details()).thenReturn(developJobWithDetails);
		
		// when
		this.jenkinsService.getBuildInfo(project);
		
	}
	
	@Test
	public void testDeleteProject() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", null);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
				
		// when
		this.jenkinsService.deleteProject(project);
		
		// then
		Mockito.verify(this.httpClient).post("/job/job_name/doDelete", false);		
	}
	
	@Test
	public void testDeleteProjectWithCustomName() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", "my_custom_key");
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
				
		// when
		this.jenkinsService.deleteProject(project);
		
		// then
		Mockito.verify(this.httpClient).post("/job/my_custom_key/doDelete", false);		
	}
	
	@Test
	public void testCreateRelease() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = new ProjectDto();
		project.setName("Job Name");
		
		// and
		final String releaseVersion = "4.1";
		final String folderJobName = "job_name";
		final String releaseJobName = buildReleaseJobName(folderJobName, releaseVersion);
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(folderJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(this.templateService.createConfigXml(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn("");
		
		// when
		this.jenkinsService.createRelease(project, releaseVersion);
		
		// then
		Mockito.verify(this.server).createJob(folderJob, releaseJobName, "", false);		
	}
	
	@Test
	public void testCreateReleaseWithCustomName() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", "my_custom_key");
		
		// and
		final String releaseVersion = "4.1";
		final String folderJobName = "my_custom_key";
		final String releaseJobName = buildReleaseJobName(folderJobName, releaseVersion);
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(folderJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(this.templateService.createConfigXml(Matchers.anyString(), Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn("");
		
		// when
		this.jenkinsService.createRelease(project, releaseVersion);
		
		// then
		Mockito.verify(this.server).createJob(folderJob, releaseJobName, "", false);		
	}
	
	@Test
	public void testCreateFeature() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", null);
		
		// and
		final String featureId = "20965";
		final String featureSubject = "my_feature";
		final String folderJobName = "job_name";
		final String featureJobName = buildFeatureJobName(folderJobName, featureId, featureSubject); // "job_name-feat-20965-my_feature";	
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(folderJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		
		// when
		this.jenkinsService.createFeature(project, featureId, featureSubject);
		
		// then
		Mockito.verify(this.templateService).createConfigXml(
				DEFAULT_TEMPLATE, 
				buildFeatureJobName(project.getName(), featureId, featureSubject), 
				buildScmURL(folderJobName), 
				buildFeatureBranchName(featureId, featureSubject)
		);
		
		// then
		Mockito.verify(this.server).createJob(folderJob, featureJobName, null, false);
	}
	
	@Test
	public void testCreateFeatureWithCustomName() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", "my_custom_key");
		
		// and
		final String featureId = "20965";
		final String featureSubject = "my_feature";
		final String folderJobName = "my_custom_key";
		final String featureJobName = buildFeatureJobName(folderJobName, featureId, featureSubject); // "job_name-feat-20965-my_feature";	
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(folderJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		
		// when
		this.jenkinsService.createFeature(project, featureId, featureSubject);
		
		// then
		Mockito.verify(this.templateService).createConfigXml(
				DEFAULT_TEMPLATE, 
				buildFeatureJobName(project.getName(), featureId, featureSubject), 
				buildScmURL("job_name"), 
				buildFeatureBranchName(featureId, featureSubject)
		);
		
		// then
		Mockito.verify(this.server).createJob(folderJob, featureJobName, null, false);
	}
	
	@Test
	public void testGetReleaseStatus() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", null);
		final String releaseVersion = "4.1";
		final String folderJobName = "job_name";
		final String releaseJobName = buildReleaseJobName(folderJobName, releaseVersion);
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		final JobWithDetails releaseJob = Mockito.mock(JobWithDetails.class);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(folderJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(this.server.getJob(folderJob, releaseJobName)).thenReturn(releaseJob);
		
		// when
		final JenkinsBuildInfo result = this.jenkinsService.getReleaseStatus(project, releaseVersion);
		
		// then
		Mockito.verify(this.server).getJob(folderJob, releaseJobName);
		
	}
	
	@Test
	public void testGetReleaseStatusWithCustomName() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", "my_custom_key");
		final String releaseVersion = "4.1";
		final String folderJobName = "my_custom_key";
		final String releaseJobName = buildReleaseJobName(folderJobName, releaseVersion);
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		final JobWithDetails releaseJob = Mockito.mock(JobWithDetails.class);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(folderJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(this.server.getJob(folderJob, releaseJobName)).thenReturn(releaseJob);
		
		// when
		final JenkinsBuildInfo result = this.jenkinsService.getReleaseStatus(project, releaseVersion);
		
		// then
		Mockito.verify(this.server).getJob(folderJob, releaseJobName);
		
	}
	
	@Test
	public void testGetFeatureStatus() throws IOException, JenkinsExtensionException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", null);
		final String featureId = "20991";
		final String featureSubject = "my_feature";
		final String folderJobName = "job_name";
		final String featureJobName = buildFeatureJobName(folderJobName, featureId, featureSubject);
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		final JobWithDetails featureJob = Mockito.mock(JobWithDetails.class);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(folderJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(this.server.getJob(folderJob, featureJobName)).thenReturn(featureJob);
		
		// when
		final JenkinsBuildInfo result = this.jenkinsService.getFeatureStatus(project, featureId, featureSubject);
		
		// then
		Mockito.verify(this.server).getJob(folderJob, featureJobName);		
	}
	
	@Test
	public void testGetFeatureStatusWithCustomName() throws IOException, JenkinsExtensionException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", "my_custom_key");
		final String featureId = "20991";
		final String featureSubject = "my_feature";
		final String folderJobName = "my_custom_key";
		final String featureJobName = buildFeatureJobName(folderJobName, featureId, featureSubject);
		
		// and
		final JobWithDetails folderJobWithDetails = new JobWithDetails();
		final FolderJob folderJob = Mockito.mock(FolderJob.class);
		final JobWithDetails featureJob = Mockito.mock(JobWithDetails.class);
		
		// mock
		Mockito.when(this.configuration.useFolders()).thenReturn(true);
		Mockito.when(this.server.getJob(folderJobName)).thenReturn(folderJobWithDetails);
		Mockito.when(this.server.getFolderJob(folderJobWithDetails)).thenReturn(Optional.of(folderJob));
		Mockito.when(this.server.getJob(folderJob, featureJobName)).thenReturn(featureJob);
		
		// when
		final JenkinsBuildInfo result = this.jenkinsService.getFeatureStatus(project, featureId, featureSubject);
		
		// then
		Mockito.verify(this.server).getJob(folderJob, featureJobName);		
	}
	
	@Test
	public void testDeleteFeatureJob() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", null);
		final String featureId = "20991";
		final String featureSubject = "my_feature";
		final String folderJobName = "job_name";
		final String jobName = buildFeatureJobName(folderJobName, featureId, featureSubject);
		
		// when
		this.jenkinsService.deleteFeatureJob(project, featureId, featureSubject);
		
		// then
		Mockito.verify(this.httpClient).post("/job/"+folderJobName+"/job/"+jobName+"/doDelete", false);
	}
	
	@Test
	public void testDeleteFeatureJobWithCustomName() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", "my_custom_key");
		final String featureId = "20991";
		final String featureSubject = "my_feature";
		final String folderJobName = "my_custom_key";
		final String jobName = buildFeatureJobName(folderJobName, featureId, featureSubject);
		
		// when
		this.jenkinsService.deleteFeatureJob(project, featureId, featureSubject);
		
		// then
		Mockito.verify(this.httpClient).post("/job/"+folderJobName+"/job/"+jobName+"/doDelete", false);
	}
	
	@Test
	public void testDeleteRelease() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", null);
		final String releaseVersion = "4.1";
		final String folderJobName = "job_name";
		final String jobName = buildReleaseJobName(folderJobName, releaseVersion);
		
		// when
		this.jenkinsService.deleteReleaseJob(project, releaseVersion);
		
		// then
		Mockito.verify(this.httpClient).post("/job/"+folderJobName+"/job/"+jobName+"/doDelete", false);
	}
	
	@Test
	public void testDeleteReleaseWithCustomName() throws JenkinsExtensionException, IOException {
		
		// given
		final ProjectDto project = newProjectDto("Job Name", "my_custom_key");
		final String releaseVersion = "4.1";
		final String folderJobName = "my_custom_key";
		final String jobName = buildReleaseJobName(folderJobName, releaseVersion);
		
		// when
		this.jenkinsService.deleteReleaseJob(project, releaseVersion);
		
		// then
		Mockito.verify(this.httpClient).post("/job/"+folderJobName+"/job/"+jobName+"/doDelete", false);
	}

	private static ProjectDto newProjectDto(final String name, final String customName) {
		
		final ProjectDto project = new ProjectDto();
		project.setName(name);
		project.getOtherAttributes().put(ProjectExtensionConstants.JOB_TEMPLATE, DEFAULT_TEMPLATE);

		if (customName != null) {
			project.getOtherAttributes().put(ProjectExtensionConstants.JENKINS_KEY, customName);
		}
		
		return project;
	}
	
	private static String newConfigXML(final String displayName) {
		
		final StringBuilder sb = new StringBuilder("<job>");
		
		if (displayName != null) {
			sb.append("<displayName>").append(displayName).append("</displayName>");
		}
		
		sb.append("<properties></properties>");
		sb.append("</job>");
		
		return sb.toString();
	}
	
	private static String buildScmURL(final String projectName) {
		
		return new StringBuilder(SCM_URL).append('/').append(projectName).append(".git").toString();
	}
	
	private static String buildFeatureJobName(final String projectName, final String featureId, final String featureSubject) {
		
		return new StringBuilder(projectName).append('-').append(buildFeatureBranchName(featureId, featureSubject)).toString();
	}
	
	private static String buildReleaseJobName(final String folderJobName, final String releaseVersion) {
		
		return new StringBuilder(folderJobName).append("-release-").append(ProjectUtils.createIdentifier(releaseVersion)).toString();
	}
	
	private static String buildReleaseBranchName(final String releaseVersion) {
		
		return new StringBuilder("release-").append(releaseVersion).toString();
	}
	
	private static String buildFeatureBranchName(final String featureId, final String featureSubject) {
		
		return new StringBuilder("feat-").append(featureId).append('-').append(featureSubject).toString();
	}
	
}
