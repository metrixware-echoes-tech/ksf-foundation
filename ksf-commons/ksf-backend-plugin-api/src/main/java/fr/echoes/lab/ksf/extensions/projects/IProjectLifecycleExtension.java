package fr.echoes.lab.ksf.extensions.projects;

/**
 * This interface provides to the plugin a way to handle events associated to
 * the projects.
 *
 * @author sleroy
 *
 */
public interface IProjectLifecycleExtension {
	void notifyCreatedProject(ProjectDto _project);

	void notifyDeletedProject(ProjectDto _project);

	void notifyDuplicatedProject(ProjectDto _project);

	void notifyUpdatedProject(ProjectDto _project);
}
