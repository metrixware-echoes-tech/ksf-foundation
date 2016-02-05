package fr.echoes.labs.ksf.extensions.projects;

import fr.echoes.lab.ksf.extensions.api.IExtension;

/**
 * This interface provides to the plugin a way to handle events associated to
 * the projects.
 *
 * @author sleroy
 *
 */
public interface IProjectLifecycleExtension extends IExtension{
	void notifyCreatedProject(ProjectDto _project);
	
	void notifyDeletedProject(ProjectDto _project);
	
	void notifyDuplicatedProject(ProjectDto _project);
	
	void notifyUpdatedProject(ProjectDto _project);
}
