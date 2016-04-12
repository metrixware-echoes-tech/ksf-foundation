package fr.echoes.labs.ksf.cc.extensions.services.project;

import java.util.List;

/**
 * @author dcollard
 *
 */
public interface IProjectVersions {

	/**
	 * Returns the list of versions for the given project.
	 *
	 * @param projectName the project name.
	 * @return a list of versions.
	 */
	public List<String> getVersions(String projectName);
}
