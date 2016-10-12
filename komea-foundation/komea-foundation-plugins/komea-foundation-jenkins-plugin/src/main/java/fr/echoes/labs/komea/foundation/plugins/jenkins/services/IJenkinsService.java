package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.util.List;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;


/**
 * Service for managing Jenkins jobs.
 * @author dcollard
 */
public interface IJenkinsService {

	/**
	 * Creates a new project.
	 *
	 * @param projectName the project name.
	 * @return
	 */
	public void createProject(ProjectDto project) throws JenkinsExtensionException;

	/**
	 * Deletes the given project.
	 *
	 * @param projectName the project key.
	 * @return
	 */
	public void deleteProject(ProjectDto project) throws JenkinsExtensionException;

	/**
	 * Returns the build history.
	 *
	 * @param projectName the project name.
	 * @return a list of {@link JenkinsBuildInfo}. Is never {@code null}.
	 * @throws JenkinsExtensionException
	 */
	public List<JenkinsBuildInfo> getBuildInfo(ProjectDto project) throws JenkinsExtensionException;

	/**
	 * Creates a new Jenkins job for the given release.
	 *
	 * @param projectName the project name
	 * @param releaseVersion the release version
	 * @throws JenkinsExtensionException
	 */
	public void createRelease(ProjectDto project, String releaseVersion) throws JenkinsExtensionException;

	/**
	 * Creates a new Jenkins job for the given feature.
	 *
	 * @param projectName the project name
	 * @param featureId the feature ID
	 * @param featureSubject the feature subject
	 * @throws JenkinsExtensionException
	 */
	public void createFeature(ProjectDto project, String featureId, String featureSubject) throws JenkinsExtensionException;

	/**
	 * Returns the Jenkins job ID.
	 *
	 * @param projectName the project name.
	 * @return the job ID or {@code null} if the given project cannot be found.
	 * @throws JenkinsExtensionException
	 */
	public String getJobId(String projectName) throws JenkinsExtensionException;

	/**
	 * Returns the feature job status.
	 *
	 * @param projectName the project name.
	 * @param featureId the feature ID.
	 * @return
	 * @throws JenkinsExtensionException
	 */
	public JenkinsBuildInfo getFeatureStatus(ProjectDto project, String featureId, String description) throws JenkinsExtensionException;

	/**
	 * Returns the release job status.
	 *
	 * @param projectName the project name.
	 * @param realeaseVersion the release version.
	 * @return
	 * @throws JenkinsExtensionException
	 */
	public JenkinsBuildInfo getReleaseStatus(ProjectDto project, String realeaseVersion) throws JenkinsExtensionException;

	/**
	 * Deletes the feature job
	 *
	 * @param projectName the project name
	 * @param featureId the feature ID
	 * @param featureSubject the feature subject
	 * @throws JenkinsExtensionException
	 */
	public void deleteFeatureJob(ProjectDto project, String featureId, String featureSubject) throws JenkinsExtensionException;

	/**
	 * Deletes the release job
	 *
	 * @param projectName the project name
	 * @param featureId the feature ID
	 * @param featureSubject the feature subject
	 * @throws JenkinsExtensionException
	 */
	public void deleteReleaseJob(ProjectDto project, String releaseName) throws JenkinsExtensionException;

}
