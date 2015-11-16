package fr.echoes.lab.ksf.cc.plugins.foreman.extensions;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.echoes.lab.foremanclient.ForemanHelper;
import fr.echoes.lab.ksf.cc.plugins.foreman.services.ForemanConfigurationService;
import fr.echoes.lab.ksf.extensions.annotations.Extension;
import fr.echoes.lab.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.lab.ksf.extensions.projects.ProjectDto;

@Extension
public class ForemanProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanProjectLifeCycleExtension.class);

	@Value("${ksf.foreman.url}")
	private String url;

	@Value("${ksf.foreman.username}")
	private String username;

	@Value("${ksf.foreman.password}")
	private String password;

	@Autowired
	private ForemanConfigurationService configurationService;

	@Override
	public void notifyCreatedProject(ProjectDto _project) {

		LOGGER.info("[foreman] project creation detected : "+_project.getKey());

		final String logginName = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			ForemanHelper.createProject(this.url, this.username, this.password, _project.getName(), logginName);
		} catch (KeyManagementException | NoSuchAlgorithmException
				| KeyStoreException e) {
			LOGGER.error("[foreman] project creation failed", e);
		}
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
