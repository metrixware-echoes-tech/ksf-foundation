package fr.echoes.labs.ksf.cc.extensions.services.project.features;

import java.util.List;

import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

/**
 * @author dcollard
 *
 */
public interface IProjectFeatues {

	/**
	 * Returns the list of feature for the given project.
	 *
	 * @param ksfProject the project
	 * @return a list of features.
	 */
	public List<IProjectFeature> getFeatures(ProjectDto ksfProject);
	
}
