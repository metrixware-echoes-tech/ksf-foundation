package fr.echoes.labs.ksf.extensions.projects;

import fr.echoes.labs.ksf.extensions.api.IExtension;

/**
 * This interface provides to the plugin a way to handle events associated to
 * the projects.
 *
 * @author sleroy
 *
 */
public interface IProjectLifecycleExtension extends IExtension{
	void notifyCreatedProject(ProjectDto project);

	void notifyDeletedProject(ProjectDto project);

	void notifyDuplicatedProject(ProjectDto project);

	void notifyUpdatedProject(ProjectDto project);

	void notifyCreatedRelease(ProjectDto project);
}
