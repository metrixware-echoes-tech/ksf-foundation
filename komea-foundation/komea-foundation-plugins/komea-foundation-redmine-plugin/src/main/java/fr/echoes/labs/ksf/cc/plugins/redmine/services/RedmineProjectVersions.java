package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.cqrs.gate.IEventBus;

import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersions;

/**
 * The Class RedmineProjectRelease implements the KSF Trigger "GetReleases" of a Project.
 *
 * @author dcollard
 */
@Service
public class RedmineProjectVersions implements IProjectVersions {

	/** The Constant LOGGER. */
	private static final Logger		LOGGER	= LoggerFactory.getLogger(RedmineProjectVersions.class);
	private final IRedmineService	redmineService;
	private final IEventBus			eventBus;

	@Autowired
	public RedmineProjectVersions(final IRedmineService redmineService, final IEventBus eventBus) {
		super();
		this.redmineService = redmineService;
		this.eventBus = eventBus;
	}

	/*
	 * (non-Javadoc)
	 * @see fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersions#getVersions(java.lang.String)
	 */
	@Override
	public List<IProjectVersion> getVersions(final String projectName) {

		try {
			return this.redmineService.getVersions(projectName);

		} catch (final Exception e) {
			LOGGER.error("RedmineProjectRelease.getReleases", e);
			this.eventBus.dispatchErrorEvent(e);
			return Collections.<IProjectVersion> emptyList();
		}

	}
}
