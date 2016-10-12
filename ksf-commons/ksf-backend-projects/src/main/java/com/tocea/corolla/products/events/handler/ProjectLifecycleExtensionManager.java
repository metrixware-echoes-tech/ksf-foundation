package com.tocea.corolla.products.events.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.EditProjectCommand;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventFeatureCanceled;
import com.tocea.corolla.products.events.EventFeatureCreated;
import com.tocea.corolla.products.events.EventFeatureFinished;
import com.tocea.corolla.products.events.EventNewProjectCreated;
import com.tocea.corolla.products.events.EventProjectDeleted;
import com.tocea.corolla.products.events.EventProjectUpdated;
import com.tocea.corolla.products.events.EventReleaseCreated;
import com.tocea.corolla.products.events.EventReleaseFinished;
import com.tocea.corolla.products.utils.ProjectDtoFactory;

import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
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

    @Autowired
    private Gate gate;

    @Subscribe
    public void notifyNewProjectCreated(final EventNewProjectCreated _event) {
        if (this.extensions == null) {
            return;
        }
        final ProjectDto projectDto = ProjectDtoFactory.convert(_event.getCreatedProject());
        for (final IProjectLifecycleExtension extension : this.extensions) {
            final String extensionName = extension.getName();
            LOGGER.info("Extension " + extensionName + " is notified that a project has been created.");
            final NotifyResult result = extension.notifyCreatedProject(projectDto);
            if (result != NotifyResult.CONTINUE) {
                LOGGER.warn("Extension " + extensionName + " has failed to handle the creation of a project.");
            }
        }

        saveAttributes(_event.getCreatedProject(), projectDto);
    }

    @Subscribe
    public void notifyProjectDeleted(final EventProjectDeleted _event) {
        if (this.extensions == null) {
            return;
        }
        final ProjectDto projectDto = ProjectDtoFactory.convert(_event.getProject());
        for (final IProjectLifecycleExtension extension : this.extensions) {
            final String extensionName = extension.getName();
            LOGGER.info("Extension " + extensionName + " is notified that a project has been deleted.");
            final NotifyResult result = extension.notifyDeletedProject(projectDto);
            if (result != NotifyResult.CONTINUE) {
                LOGGER.warn("Extension " + extensionName + " has failed to handle the deletion of a project.");
            }
        }

    }

    @Subscribe
    public void notifyReleaseCreated(final EventReleaseCreated event) {
        if (this.extensions == null) {
            return;
        }
        final ProjectDto projectDto = ProjectDtoFactory.convert(event.getProject());
        for (final IProjectLifecycleExtension extension : this.extensions) {
            final String extensionName = extension.getName();
            LOGGER.info("Extension " + extensionName + " is notified that a release bas been created.");
            final NotifyResult result = extension.notifyCreatedRelease(projectDto, event.getReleaseVersion(), event.getUsername());
            if (result != NotifyResult.CONTINUE) {
                LOGGER.warn("Extension " + extensionName + " has failed to handle the creation of a release.");
            }
        }

        saveAttributes(event.getProject(), projectDto);
    }

    @Subscribe
    public void notifyReleaseFinished(final EventReleaseFinished event) {
        if (this.extensions == null) {
            return;
        }
        final ProjectDto projectDto = ProjectDtoFactory.convert(event.getProject());
        for (final IProjectLifecycleExtension extension : this.extensions) {
            final String extensionName = extension.getName();
            LOGGER.info("Extension " + extensionName + " is notified that a release has been finished.");
            final NotifyResult result = extension.notifyFinishedRelease(projectDto, event.getReleaseVersion());
            if (result != NotifyResult.CONTINUE) {
                LOGGER.warn("Extension " + extensionName + " has failed to handle the finish of a release.");
            }
        }

    }

    @Subscribe
    public void notifyFeatureCreated(final EventFeatureCreated event) {
        if (this.extensions == null) {
            return;
        }
        final ProjectDto projectDto = ProjectDtoFactory.convert(event.getProject());
        for (final IProjectLifecycleExtension extension : this.extensions) {
            final String extensionName = extension.getName();
            LOGGER.info("Extension " + extensionName + " is notified that a feature has been created.");
            final NotifyResult result = extension.notifyCreatedFeature(projectDto, event.getFeatureId(), event.getFeatureSubject(), event.getUsername());
            if (result != NotifyResult.CONTINUE) {
                LOGGER.warn("Extension " + extensionName + " has failed to handle the creation of a feature.");
            }
        }

        saveAttributes(event.getProject(), projectDto);
    }

    @Subscribe
    public void notifyFeatureFinished(final EventFeatureFinished event) {
        if (this.extensions == null) {
            return;
        }
        final ProjectDto projectDto = ProjectDtoFactory.convert(event.getProject());
        for (final IProjectLifecycleExtension extension : this.extensions) {
            final String extensionName = extension.getName();
            LOGGER.info("Extension " + extensionName + " is notified that a feature has been finished.");
            final NotifyResult result = extension.notifyFinishedFeature(projectDto, event.getFeatureId(), event.getFeatureSubject());
            if (result != NotifyResult.CONTINUE) {
                LOGGER.warn("Extension " + extensionName + " has failed to handle the finish of a feature.");
            }
        }

    }

    @Subscribe
    public void notifyFeatureCanceled(final EventFeatureCanceled event) {
        if (this.extensions == null) {
            return;
        }
        final ProjectDto projectDto = ProjectDtoFactory.convert(event.getProject());
        for (final IProjectLifecycleExtension extension : this.extensions) {
            final String extensionName = extension.getName();
            LOGGER.info("Extension " + extensionName + " is notified that a feature has been canceled.");
            final NotifyResult result = extension.notifyCanceledFeature(projectDto, event.getFeatureId(), event.getFeatureSubject());
            if (result != NotifyResult.CONTINUE) {
                LOGGER.warn("Extension " + extensionName + " has failed to handle the cancelation of a feature.");
            }
        }

    }

    @Subscribe
    public void notifyProjectUpdated(final EventProjectUpdated _event) {
        if (this.extensions == null) {
            return;
        }
        final ProjectDto projectDto = ProjectDtoFactory.convert(_event.getProject());
        for (final IProjectLifecycleExtension extension : this.extensions) {
            final String extensionName = extension.getName();
            LOGGER.info("Extension " + extensionName + " is notified that a project has been updated.");
            final NotifyResult result = extension.notifyUpdatedProject(projectDto);
            if (result != NotifyResult.CONTINUE) {
                LOGGER.warn("Extension " + extensionName + " has failed to handle the update of a project.");
            }
        }

    }

    private void saveAttributes(Project project, ProjectDto projectDto) {

        project.setOtherAttributes(projectDto.getOtherAttributes());
        this.gate.dispatch(new EditProjectCommand(project));

    }

    // TODO:/Duplicated projects
}
