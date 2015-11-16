package com.tocea.corolla.products.events.handler;

import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventNewProjectCreated;
import com.tocea.corolla.products.events.EventProjectDeleted;
import com.tocea.corolla.products.events.EventProjectUpdated;

import fr.echoes.lab.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.lab.ksf.extensions.projects.ProjectDto;

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
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(_event.getCreatedProject());
		for (final IProjectLifecycleExtension extension : extensions) {

			extension.notifyCreatedProject(projectDto);
		}

	}

	@Subscribe
	public void notifyCreation(final EventProjectDeleted _event) {
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(_event.getProject());
		for (final IProjectLifecycleExtension extension : extensions) {
			extension.notifyDeletedProject(projectDto);
		}

	}

	@Subscribe
	public void notifyCreation(final EventProjectUpdated _event) {
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = newProjectDto(_event.getProject());
		for (final IProjectLifecycleExtension extension : extensions) {
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
