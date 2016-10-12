package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@Service
public class RedmineNameResolver {

	public String getProjectKey(final ProjectDto project) {
		
		if (project.getOtherAttributes().containsKey(ProjectExtensionConstants.REDMINE_KEY)) {
			return (String) project.getOtherAttributes().get(ProjectExtensionConstants.REDMINE_KEY);
		}
		
		return ProjectUtils.createIdentifier(project.getName());
	}
	
}
