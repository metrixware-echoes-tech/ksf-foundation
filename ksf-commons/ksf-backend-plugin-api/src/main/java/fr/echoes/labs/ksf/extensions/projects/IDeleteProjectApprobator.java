package fr.echoes.labs.ksf.extensions.projects;

import fr.echoes.labs.ksf.extensions.exceptions.PermissionDeniedException;

public interface IDeleteProjectApprobator {

	public void approve(final ProjectDto project) throws PermissionDeniedException;
	
}
