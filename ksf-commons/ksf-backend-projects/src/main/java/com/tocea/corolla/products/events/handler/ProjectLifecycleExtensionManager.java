package com.tocea.corolla.products.events.handler;

import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventFeatureCreated;
import com.tocea.corolla.products.events.EventFeatureFinished;
import com.tocea.corolla.products.events.EventNewProjectCreated;
import com.tocea.corolla.products.events.EventProjectDeleted;
import com.tocea.corolla.products.events.EventProjectUpdated;
import com.tocea.corolla.products.events.EventReleaseCreated;
import com.tocea.corolla.products.events.EventReleaseFinished;

import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

/**
 * The Class ProjectLifecycleExtensionManager defines an handler that manages
 * events associated to the creation/deletion/update of the projects.
 */
@EventHandler
public class ProjectLifecycleExtensionManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectLifecycleExtensionManager.class);

	@Autowired(required = false)
	private IProjectLifecycleExtension[] extensions;

	@Subscribe
	public void notifyCreation(final EventNewProjectCreated _event) {
		if (this.extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(_event.getCreatedProject());
		for (final IProjectLifecycleExtension extension : this.extensions) {

			extension.notifyCreatedProject(projectDto);
		}

	}

	@Subscribe
	public void notifyCreation(final EventProjectDeleted _event) {
		if (this.extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(_event.getProject());
		for (final IProjectLifecycleExtension extension : this.extensions) {
			extension.notifyDeletedProject(projectDto);
		}

	}

	@Subscribe
	public void notifyCreation(final EventReleaseCreated event) {
		if (this.extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(event.getProject());
		for (final IProjectLifecycleExtension extension : this.extensions) {
			extension.notifyCreatedRelease(projectDto, event.getReleaseVersion());
		}

	}
	
	@Subscribe
	public void notifyCreation(final EventReleaseFinished event) {
		if (this.extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(event.getProject());
		for (final IProjectLifecycleExtension extension : this.extensions) {
			extension.notifyCreatedRelease(projectDto, event.getReleaseVersion());
		}

	}	

	@Subscribe
	public void notifyCreation(final EventFeatureCreated event) {
		if (this.extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(event.getProject());
		for (final IProjectLifecycleExtension extension : this.extensions) {
			extension.notifyCreatedFeature(projectDto, event.getFeatureId(), event.getFeatureSubject());
		}

	}
	
	@Subscribe
	public void notifyCreation(final EventFeatureFinished event) {
		if (this.extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(event.getProject());
		for (final IProjectLifecycleExtension extension : this.extensions) {
			extension.notifyFinishedFeature(projectDto, event.getFeatureId(), event.getFeatureSubject());
		}

	}		
	

	@Subscribe
	public void notifyCreation(final EventProjectUpdated _event) {
		if (this.extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(_event.getProject());
		for (final IProjectLifecycleExtension extension : this.extensions) {
			extension.notifyUpdatedProject(projectDto);
		}

	}

	private ProjectDto newProjectDto(final Project _createdProject) {
		try {
			final ProjectDto projectDto = new ProjectDto();
			final BeanMap projectMap = new BeanMap(_createdProject);
			final BeanMap dtoMap = new BeanMap(projectDto);
			dtoMap.putAllWriteable(projectMap);
			return (ProjectDto) dtoMap.getBean();

		} catch (final Exception e) {
			LOGGER.error("Could not convert project to Project DTO", e);

		}
		return null;
	}

	// TODO:/Duplicated projects

}
