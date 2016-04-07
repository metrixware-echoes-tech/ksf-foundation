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
	 *
	 *
	 * @param projectName
	 * @throws GitExtensionException
	 */
	public void createRelease(String projectName) throws GitExtensionException;


}
