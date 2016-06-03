package fr.echoes.labs.ksf.cc.plugins.redmine.extensions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.IRedmineService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineErrorHandlingService;
import fr.echoes.labs.ksf.extensions.annotations.Extension;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;

/**
 * @author dcollard
 *
 */
@Order(value=1)
@Extension
public class RedmineProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineProjectLifeCycleExtension.class);

	@Autowired
	private RedmineErrorHandlingService errorHandler;

	@Autowired
	private IRedmineService redmineService;

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RedmineConfigurationService configurationService;

	private ICurrentUserService currentUserService;

	public void init() {

		if (this.currentUserService == null) {
			this.currentUserService = this.applicationContext.getBean(ICurrentUserService.class);
		}
	}

	@Override
	public NotifyResult notifyCreatedProject(ProjectDto project) {

		init();

		final String logginName = this.currentUserService.getCurrentUserLogin();

		if (StringUtils.isEmpty(logginName)) {
			LOGGER.error("[Redmine] No user found. Aborting project creation in Redmine module");
			return NotifyResult.CONTINUE;
		}

		LOGGER.info("[Redmine] project {} creation detected [demanded by: {}]", project.getKey(), logginName);
		try {

			this.redmineService.createProject(project.getName(), logginName);

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to create project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to create Redmine project.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDeletedProject(ProjectDto project) {

		try {

			this.redmineService.deleteProject(project.getName());

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to delete project {} ", project.getName(), ex);
			this.errorHandler.registerError("Unable to delete Redmine project.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDuplicatedProject(ProjectDto _project) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyUpdatedProject(ProjectDto _project) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedRelease(ProjectDto project, String releaseVersion) {
		try {

			final String releaseTicketSubject = createReleaseTicketSubject(project, releaseVersion);

			this.redmineService.createTicket(project, releaseVersion, releaseTicketSubject);
		} catch (final RedmineExtensionException e) {
			LOGGER.error("[Redmine] Failed to create a ticket for the release {} of the project {}", releaseVersion, project.getName());
			this.errorHandler.registerError("Failed to create a Redmine ticket for the release.");
		}
		return NotifyResult.CONTINUE;
	}

	private String createReleaseTicketSubject(ProjectDto project, String releaseVersion) {
		final Map<String, String> variables = new HashMap<String, String>(2);
		variables.put("projectName", project.getName());
		variables.put("releaseVersion", releaseVersion);
		return replaceVariables(this.configurationService.getReleaseTicketMessagePattern(), variables);
	}

	@Override
	public NotifyResult notifyCreatedFeature(ProjectDto project, String ticketId,
			String featureSubject) {

		try {
			this.redmineService.changeStatus(ticketId, this.configurationService.getFeatureStatusAssignedId());

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to change ticket #{} status", ticketId, ex);
			this.errorHandler.registerError("Failed to change Redmine ticket status.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedFeature(ProjectDto projectDto, String ticketId,
			String featureSubject) {
		try {
			this.redmineService.changeStatus(ticketId, this.configurationService.getFeatureStatusClosedId());

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to change ticket #{} status", ticketId, ex);
			this.errorHandler.registerError("Failed to change Redmine ticket status.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedRelease(ProjectDto project, String releaseName) {
		return NotifyResult.CONTINUE;
	}

	private String replaceVariables(String str, Map<String, String> variables) {
		final StrSubstitutor sub = new StrSubstitutor(variables);
		sub.setVariablePrefix("%{");
		return sub.replace(str);
	}

}
