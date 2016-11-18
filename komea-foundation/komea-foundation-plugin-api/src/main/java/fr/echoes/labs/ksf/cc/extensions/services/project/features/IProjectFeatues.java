package fr.echoes.labs.ksf.cc.extensions.services.project.features;

import java.util.List;

import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.pluginfwk.api.extension.Extension;

/**
 * @author dcollard
 *
 */
public interface IProjectFeatues extends Extension {

	/**
	 * Returns the list of feature for the given project.
	 *
	 * @param ksfProject the project
	 * @return a list of features.
	 */
	public List<IProjectFeature> getFeatures(ProjectDto ksfProject);
	
}
