package fr.echoes.labs.ksf.cc.extensions.services.project.versions;

import java.util.List;

import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.pluginfwk.api.extension.Extension;


/**
 * @author dcollard
 *
 */
public interface IProjectVersions extends Extension {

	/**
	 * Returns the list of feature for the given project.
	 *
	 * @param projectName the project name.
	 * @return a list of featues.
	 */
	public List<IProjectVersion> getVersions(ProjectDto ksfProject);

}
