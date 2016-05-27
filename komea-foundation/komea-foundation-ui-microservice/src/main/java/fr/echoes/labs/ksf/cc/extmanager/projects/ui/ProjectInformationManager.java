package fr.echoes.labs.ksf.cc.extmanager.projects.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.cc.extensions.services.project.TicketStatus;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeatues;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersions;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.ProjectVersion;
import fr.echoes.labs.ksf.cc.releases.dao.IReleaseDAO;
import fr.echoes.labs.ksf.cc.releases.model.Release;

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

	@Autowired
	IReleaseDAO releaseDao;

	/**
	 * Returns the versions for this project.
	 *
	 * @param projectName the project name.
	 * @return a list of versions.
	 */
	public List<IProjectVersion> getVersions(Project project) {

		if (this.versions == null) {
			LOGGER.info("getVersions : no versions.");
			return Lists.newArrayList();
		}

		final String projectName = project.getName();

		final List<IProjectVersion> result = new ArrayList<IProjectVersion>();
		final List<Release> startedReleases = this.releaseDao.findAll();

		for (final IProjectVersions projectRelease : this.versions) {
			final List<IProjectVersion> releases = projectRelease.getVersions(projectName);
			if (releases != null) {
				for (final IProjectVersion r : releases) {
					if (isStartedRelease(r, startedReleases)) {
						((ProjectVersion) r).setStatus(TicketStatus.CREATED);
					}
				}
				result.addAll(releases);
			}
		}
		LOGGER.info("getVersions : the project \"{}\" has {} versions.", projectName, result.size());
		return result;
	}

	private boolean isStartedRelease(IProjectVersion release, List<Release> startedReleases) {
		for (final Release startedRelease : startedReleases) {
			if (StringUtils.equals(startedRelease.getReleaseId(), release.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the features for this project.
	 *
	 * @param projectName the project name.
	 * @return a list of features.
	 */
	public List<IProjectFeature> getFeatures(Project project) {

		if (this.features == null) {
			LOGGER.info("getFeatures : no features.");
			return Lists.newArrayList();
		}

		final String projectName = project.getName();

		final List<IProjectFeature> result = new ArrayList<IProjectFeature>();

		for (final IProjectFeatues projectFeature : this.features) {
			final List<IProjectFeature> features = projectFeature.getFeatures(projectName);
			if (features != null) {
				result.addAll(features);
			}
		}
		LOGGER.info("getVersions : the project \"{}\" has {} features.", projectName, result.size());
		return result;
	}

}
