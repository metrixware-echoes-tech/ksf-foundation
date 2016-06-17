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
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeatures;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersions;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.ProjectVersion;
import fr.echoes.labs.ksf.cc.releases.dao.IReleaseDAO;
import fr.echoes.labs.ksf.cc.releases.model.Release;
import fr.echoes.labs.ksf.cc.releases.model.ReleaseState;

/**
 * This service allows to obtain project information returned by the plug-ins.
 *
 *
 * @author dcollard
 *
 */
@Service("projectInformationManager")
public class ProjectInformationExtensionManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectInformationExtensionManager.class);

	public static boolean isStartedRelease(final IProjectVersion release, final List<Release> startedReleases) {
		for (final Release startedRelease : startedReleases) {
			if (StringUtils.equals(startedRelease.getReleaseId(), release.getId())) {
				return startedRelease.getState() != ReleaseState.FINISHED;
			}
		}
		return false;
	}

	@Autowired(required = false)
	private IProjectVersions[]	versions;

	@Autowired(required = false)
	private IProjectFeatures[]	features;

	@Autowired
	private IReleaseDAO			releaseDao;

	/**
	 * Returns the features for this project.
	 *
	 * @param projectName
	 *            the project name.
	 * @return a list of features.
	 */
	public List<IProjectFeature> getFeatures(final Project project) {

		if (this.features == null) {
			LOGGER.info("getFeatures : no features.");
			return Lists.newArrayList();
		}

		final String projectName = project.getName();

		final List<IProjectFeature> result = new ArrayList<>();

		for (final IProjectFeatures projectFeature : this.features) {
			final List<IProjectFeature> features = projectFeature.getFeatures(projectName);
			if (features != null) {
				result.addAll(features);
			}
		}
		LOGGER.info("getVersions : the project \"{}\" has {} features.", projectName, result.size());
		return result;
	}

	/**
	 * Returns the versions for this project.
	 *
	 * @param projectName
	 *            the project name.
	 * @return a list of versions.
	 */
	public List<IProjectVersion> getVersions(final Project project) {

		if (this.versions == null) {
			LOGGER.info("getVersions : no versions.");
			return Lists.newArrayList();
		}

		final String projectName = project.getName();

		final List<IProjectVersion> listOfReleases = new ArrayList<>();
		final List<Release> startedReleases = this.releaseDao.findAll();

		for (final IProjectVersions allProjectVersions : this.versions) {
			final List<IProjectVersion> projectVersions = allProjectVersions.getVersions(projectName);
			if (projectVersions != null) {
				for (final IProjectVersion projectVersion : projectVersions) {
					for (final Release startedRelease : startedReleases) {
						if (StringUtils.equals(startedRelease.getReleaseId(), projectVersion.getId())) {
							switch (startedRelease.getState()) {
							case FINISHED:
								((ProjectVersion) projectVersion).setStatus(TicketStatus.FINISHED);
								break;
							case STARTED:
								((ProjectVersion) projectVersion).setStatus(TicketStatus.CREATED);
								break;
							}
						}
					}
				}
				listOfReleases.addAll(projectVersions);
			}
		}
		LOGGER.info("getVersions : the project \"{}\" has {} versions.", projectName, listOfReleases.size());
		return listOfReleases;
	}

	public void setFeatures(final IProjectFeatures[] features) {
		this.features = features;
	}

	public void setReleaseDao(final IReleaseDAO releaseDao) {
		this.releaseDao = releaseDao;
	}

	public void setVersions(final IProjectVersions[] versions) {
		this.versions = versions;
	}

}
