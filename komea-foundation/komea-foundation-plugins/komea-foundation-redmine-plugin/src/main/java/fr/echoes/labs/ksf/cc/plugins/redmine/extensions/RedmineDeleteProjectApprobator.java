package fr.echoes.labs.ksf.cc.plugins.redmine.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taskadapter.redmineapi.RedmineException;

import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.IRedmineService;
import fr.echoes.labs.ksf.extensions.exceptions.PermissionDeniedException;
import fr.echoes.labs.ksf.extensions.projects.IDeleteProjectApprobator;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@Component
public class RedmineDeleteProjectApprobator implements IDeleteProjectApprobator {

	@Autowired
	private IRedmineService redmineService;
	
	@Override
	public void approve(final ProjectDto project) throws PermissionDeniedException {
		
		try {
			if (!this.redmineService.isAdmin()) {
				throw new PermissionDeniedException("Cannot delete project "+project.getKey()
								+". Only Redmine administrators can delete projects.");
			}
		} catch (RedmineExtensionException | RedmineException ex) {
			throw new PermissionDeniedException("Cannot access Redmine REST API with current user.", ex);
		}
		
	}

}
