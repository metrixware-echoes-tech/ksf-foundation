package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.cqrs.gate.IEventBus;

import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeatures;

/**
 * The Class RedmineProjectFreature implements the trigger "GetFeatures" of KSF.
 *
 * @author dcollard
 */
@Service
public class RedmineProjectFeature implements IProjectFeatures {

	private static final Logger		LOGGER	= LoggerFactory.getLogger(RedmineProjectFeature.class);

	private final IRedmineService	redmineService;

	private final IEventBus			eventBus;

	@Autowired
	public RedmineProjectFeature(final IRedmineService redmineService, final IEventBus eventBus) {
		super();
		this.redmineService = redmineService;
		this.eventBus = eventBus;
	}

	@Override
	public List<IProjectFeature> getFeatures(final String projectName) {

		try {
			return this.redmineService.getFeatures(projectName);

		} catch (final Exception e) {
			LOGGER.error("RedmineProjectRelease.getReleases", e);
			this.eventBus.dispatchErrorEvent(e);
			return Collections.<IProjectFeature> emptyList();
		}

	}

}
