package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeatues;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeature;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;

/**
 * @author dcollard
 *
 */
@Service
public class RedmineProjectFreature implements IProjectFeatues {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineProjectFreature.class);
	
	@Autowired
	IRedmineService redmineService;	
	
	@Override
	public List<IProjectFeature> getFeatures(String projectName) {
		final List<IProjectFeature> releases;

		try {
			releases = this.redmineService.getFeatures(projectName);

		} catch (RedmineExtensionException e) {
			LOGGER.error("RedmineProjectRelease.getReleases", e);
			return Collections.<IProjectFeature>emptyList();
		}

		return releases != null ? releases : Collections.<IProjectFeature>emptyList();
	}

}
