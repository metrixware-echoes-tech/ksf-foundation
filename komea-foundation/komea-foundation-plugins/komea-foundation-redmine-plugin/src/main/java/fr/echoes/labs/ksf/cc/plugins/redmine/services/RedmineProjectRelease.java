package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersions;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;


/**
 * @author dcollard
 *
 */
@Service
public class RedmineProjectRelease implements IProjectVersions {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineProjectRelease.class);

	@Autowired
	IRedmineService redmineService;


	@Override
	public List<IProjectVersion> getVersions(final ProjectDto ksfProject) {
		final List<IProjectVersion> releases;

		try {
			releases = this.redmineService.getVersions(ksfProject);

		} catch (final RedmineExtensionException e) {
			LOGGER.error("RedmineProjectRelease.getReleases", e);
			return Collections.<IProjectVersion>emptyList();
		}

		return releases != null ? releases : Collections.<IProjectVersion>emptyList();
	}

}
