package fr.echoes.labs.ksf.cc.extensions.services.project;

import java.util.List;

/**
 * @author dcollard
 *
 */
public interface IProjectFeatues {

	/**
	 * Returns the list of feature for the given project.
	 *
	 * @param projectName the project name.
	 * @return a list of featues.
	 */
	public List<IProjectFeature> getFeatures(String projectName);
	
}
