package com.tocea.corolla.products.events.handler;

import java.util.List;

import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.EditProjectCommand;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventFeatureCreated;
import com.tocea.corolla.products.events.EventFeatureFinished;
import com.tocea.corolla.products.events.EventNewProjectCreated;
import com.tocea.corolla.products.events.EventProjectDeleted;
import com.tocea.corolla.products.events.EventProjectUpdated;
import com.tocea.corolla.products.events.EventReleaseCreated;
import com.tocea.corolla.products.events.EventReleaseFinished;

import fr.echoes.labs.ksf.cc.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.cc.extensions.projects.NotifyResult;
import fr.echoes.labs.ksf.cc.extensions.projects.ProjectDto;
import fr.echoes.labs.pluginfwk.api.extension.ExtensionManager;

/**
 * The Class ProjectLifecycleExtensionManager defines an handler that manages
 * events associated to the creation/deletion/update of the projects.
 */
@EventHandler
public class ProjectLifecycleExtensionManager {

	private static final Logger		LOGGER	= LoggerFactory.getLogger(ProjectLifecycleExtensionManager.class);

	private final ExtensionManager	extensionManager;

	private final Gate				gate;

	@Autowired
	public ProjectLifecycleExtensionManager(final ExtensionManager extensionManager, final Gate gate) {
		super();
		this.extensionManager = extensionManager;
		this.gate = gate;
	}

	@Subscribe
	public void notifyCreation(final EventFeatureCreated event) {
		final List<IProjectLifecycleExtension> extensions = this.extensionManager.findExtensions(IProjectLifecycleExtension.class);
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = this.newProjectDto(event.getProject());
		for (final IProjectLifecycleExtension extension : extensions) {
			final NotifyResult result = extension.notifyCreatedFeature(projectDto, event.getFeatureId(), event.getFeatureSubject(), event.getUsername());
			if (result != NotifyResult.CONTINUE) {
				break;
			}
		}

		this.saveAttributes(event.getProject(), projectDto);
	}

	@Subscribe
	public void notifyCreation(final EventFeatureFinished event) {
		final List<IProjectLifecycleExtension> extensions = this.extensionManager.findExtensions(IProjectLifecycleExtension.class);
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = this.newProjectDto(event.getProject());
		for (final IProjectLifecycleExtension extension : extensions) {
			final NotifyResult result = extension.notifyFinishedFeature(projectDto, event.getFeatureId(), event.getFeatureSubject());
			if (result != NotifyResult.CONTINUE) {
				break;
			}
		}

	}

	@Subscribe
	public void notifyCreation(final EventNewProjectCreated _event) {
		final List<IProjectLifecycleExtension> extensions = this.extensionManager.findExtensions(IProjectLifecycleExtension.class);
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = this.newProjectDto(_event.getCreatedProject());
		for (final IProjectLifecycleExtension extension : extensions) {

			extension.notifyCreatedProject(projectDto);
		}

		this.saveAttributes(_event.getCreatedProject(), projectDto);
	}

	@Subscribe
	public void notifyCreation(final EventProjectDeleted _event) {
		final List<IProjectLifecycleExtension> extensions = this.extensionManager.findExtensions(IProjectLifecycleExtension.class);
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = this.newProjectDto(_event.getProject());
		for (final IProjectLifecycleExtension extension : extensions) {
			extension.notifyDeletedProject(projectDto);
		}

	}

	@Subscribe
	public void notifyCreation(final EventProjectUpdated _event) {
		final List<IProjectLifecycleExtension> extensions = this.extensionManager.findExtensions(IProjectLifecycleExtension.class);
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = this.newProjectDto(_event.getProject());
		for (final IProjectLifecycleExtension extension : extensions) {
			extension.notifyUpdatedProject(projectDto);
		}

	}

	@Subscribe
	public void notifyCreation(final EventReleaseCreated event) {
		final List<IProjectLifecycleExtension> extensions = this.extensionManager.findExtensions(IProjectLifecycleExtension.class);
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = this.newProjectDto(event.getProject());
		for (final IProjectLifecycleExtension extension : extensions) {
			final NotifyResult result = extension.notifyCreatedRelease(projectDto, event.getReleaseVersion(), event.getUsername());
			if (result != NotifyResult.CONTINUE) {
				break;
			}
		}

		this.saveAttributes(event.getProject(), projectDto);
	}

	@Subscribe
	public void notifyCreation(final EventReleaseFinished event) {
		final List<IProjectLifecycleExtension> extensions = this.extensionManager.findExtensions(IProjectLifecycleExtension.class);
		if (extensions == null) {
			return;
		}
		final ProjectDto projectDto = this.newProjectDto(event.getProject());
		for (final IProjectLifecycleExtension extension : extensions) {
			final NotifyResult result = extension.notifyFinishedRelease(projectDto, event.getReleaseVersion());
			if (result != NotifyResult.CONTINUE) {
				break;
			}
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

	private void saveAttributes(final Project project, final ProjectDto projectDto) {

		project.setOtherAttributes(projectDto.getOtherAttributes());
		this.gate.dispatch(new EditProjectCommand(project));

	}

}
