package fr.echoes.labs.ksf.cc.plugins.nexus.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

@Service
public class NexusNameResolver {
	
	public String getRepositoryKey(final Project project) {
		
		return getRepositoryKey(project.getName(), project.getOtherAttributes());
	}

	public String getRepositoryKey(final ProjectDto project) {
		
		return getRepositoryKey(project.getName(), project.getOtherAttributes());
	}
	
	private String getRepositoryKey(final String name, final Map<String, Object> attributes) {
		
		if (attributes.containsKey(ProjectExtensionConstants.NEXUS_REPOSITORY_KEY)) {
			return (String) attributes.get(ProjectExtensionConstants.NEXUS_REPOSITORY_KEY);
		}
		
		return ProjectUtils.createIdentifier(name);
	}
	
}
