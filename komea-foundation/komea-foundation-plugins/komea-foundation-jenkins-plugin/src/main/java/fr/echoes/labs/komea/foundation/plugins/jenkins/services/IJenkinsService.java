package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;


/**
 * @author dcollard
 *
 */
public interface IJenkinsService {

	/**
	 * Creates a new project.
	 *
	 * @param projectName the project name.
	 * @return
	 */
	public void createProject(String projectName) throws JenkinsExtensionException;

	/**
	 * Deletes the given project.
	 *
	 * @param projectName the project key.
	 * @return
	 */
	public void deleteProject(String projectName) throws JenkinsExtensionException;


}
