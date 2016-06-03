package fr.echoes.labs.komea.foundation.plugins.git.services;

import fr.echoes.labs.komea.foundation.plugins.git.GitExtensionException;


/**
 * @author dcollard
 *
 */
public interface IGitService {


	/**
	 * Creates a new project.
	 *
	 * @param projectName the project name.
	 * @return
	 */
	public void createProject(String projectName) throws GitExtensionException;

	/**
	 * Deletes the given project.
	 *
	 * @param projectName the project key.
	 * @return
	 */
	public void deleteProject(String projectName) throws GitExtensionException;

	/**
	 * Creates a new branch for the given release.
	 *
	 * @param projectName the project name
	 * @param releaseVersion the release version
	 * @throws GitExtensionException
	 */
	public void createRelease(String projectName, String releaseVersion) throws GitExtensionException;

	/**
	 * Creates a new branch for the given feature.
	 *
	 * @param projectName the project name
	 * @param featureId the feature ID
	 * @param featureSubject the feature subject
	 */
	public void createFeature(String projectName, String featureId, String featureSubject) throws GitExtensionException;

	/**
	 * Closes the feature.
	 *
	 * @param projectName the project name
	 * @param featureId the feature ID
	 * @param featureSubject the feature subject
	 * @throws GitExtensionException
	 */
	public void closeFeature(String projectName, String featureId, String featureSubject) throws GitExtensionException;

	/**
	 * Closes the release.
	 *
	 * @param projectName the project name
	 * @param releaseName the feature name
	 * @throws GitExtensionException
	 */
	public void closeRelease(String projectName, String releaseName) throws GitExtensionException;

}
