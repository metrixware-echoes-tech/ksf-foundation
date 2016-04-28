package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.extensions.services.project.IProjectFeatues;
import fr.echoes.labs.ksf.cc.extensions.services.project.IProjectVersions;
import fr.echoes.labs.ksf.cc.extensions.services.project.IProjectFeature;

/**
 * This service allows to obtain project information returned by the plug-ins.
 *
 *
 * @author dcollard
 *
 */
@Service("projectInformationManager")
public class ProjectInformationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectInformationManager.class);

	@Autowired(required = false)
	private IProjectVersions[] versions;

	@Autowired(required = false)
	private IProjectFeatues[] features;	
	
	/**
	 * Returns the versions for this project.
	 *
	 * @param projectName the project name.
	 * @return a list of versions.
	 */
	public List<String> getVersions(String projectName) {

		if (this.versions == null) {
			LOGGER.info("getVersions : no versions.");
			return Lists.newArrayList();
		}

		final List<String> result = new ArrayList<String>();

		for (IProjectVersions projectRelease : this.versions) {
			List<String> releases = projectRelease.getVersions(projectName);
			if (releases != null) {
				result.addAll(releases);
			}
		}
		LOGGER.info("getVersions : the project \"{}\" has {} versions.", projectName, result.size());
		return result;
	}
	
	/**
	 * Returns the features for this project.
	 *
	 * @param projectName the project name.
	 * @return a list of features.
	 */
	public List<IProjectFeature> getFeatures(String projectName) {

		if (this.features == null) {
			LOGGER.info("getFeatures : no features.");
			return Lists.newArrayList();
		}

		final List<IProjectFeature> result = new ArrayList<IProjectFeature>();

		for (IProjectFeatues projectFeature : this.features) {
			List<IProjectFeature> features = projectFeature.getFeatures(projectName);
			if (features != null) {
				result.addAll(features);
			}
		}
		LOGGER.info("getVersions : the project \"{}\" has {} features.", projectName, result.size());
		return result;
	}	

}
