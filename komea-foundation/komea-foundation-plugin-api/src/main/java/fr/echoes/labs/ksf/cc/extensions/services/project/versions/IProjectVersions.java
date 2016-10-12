package fr.echoes.labs.ksf.cc.extensions.services.project.versions;

import java.util.List;

import fr.echoes.labs.ksf.extensions.projects.ProjectDto;


/**
 * @author dcollard
 *
 */
public interface IProjectVersions {

	/**
	 * Returns the list of feature for the given project.
	 *
	 * @param projectName the project name.
	 * @return a list of featues.
	 */
	public List<IProjectVersion> getVersions(ProjectDto ksfProject);

}
