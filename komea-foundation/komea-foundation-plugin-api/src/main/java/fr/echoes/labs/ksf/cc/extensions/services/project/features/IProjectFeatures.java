package fr.echoes.labs.ksf.cc.extensions.services.project.features;

import java.util.List;

/**
 * @author dcollard
 *
 */
public interface IProjectFeatures {

	/**
	 * Returns the list of feature for the given project.
	 *
	 * @param projectName the project name.
	 * @return a list of featues.
	 */
	public List<IProjectFeature> getFeatures(String projectName);
	
}
