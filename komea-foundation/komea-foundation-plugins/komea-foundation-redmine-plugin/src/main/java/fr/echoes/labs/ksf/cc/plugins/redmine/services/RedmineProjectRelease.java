package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.services.project.IProjectVersions;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;


@Service
public class RedmineProjectRelease implements IProjectVersions {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineProjectRelease.class);

	@Autowired
	IRedmineService redmineService;


	@Override
	public List<String> getVersions(String projectName) {
		final List<String> releases;

		try {
			releases = this.redmineService.getVersions(projectName);

		} catch (RedmineExtensionException e) {
			LOGGER.error("RedmineProjectRelease.getReleases", e);
			return Collections.<String>emptyList();
		}

		return releases != null ? releases : Collections.<String>emptyList();
	}

}
