package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanConfigurationService;
import fr.echoes.lab.ksf.extensions.annotations.Extension;
import fr.echoes.lab.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.lab.ksf.extensions.projects.ProjectDto;

@Extension
public class ForemanProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanProjectLifeCycleExtension.class);

	@Autowired
	private ForemanConfigurationService configurationService;

	@Override
	public void notifyCreatedProject(ProjectDto _project) {

		LOGGER.info("[foreman] project creation detected : "+_project.getKey());
		//LOGGER.info("your password : "+SecurityContextHolder.getContext().getAuthentication().getCredentials());
		LOGGER.info("[foreman] url : "+ configurationService.getForemanUrl());

	}

	@Override
	public void notifyDeletedProject(ProjectDto _project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyDuplicatedProject(ProjectDto _project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyUpdatedProject(ProjectDto _project) {
		// TODO Auto-generated method stub

	}

}
