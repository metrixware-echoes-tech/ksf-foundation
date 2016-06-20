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

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Version;
import com.tocea.corolla.products.dao.IProjectDAO;

import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.IRedmineService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineConfigurationService;
import fr.echoes.labs.ksf.cc.plugins.redmine.services.RedmineErrorHandlingService;
import fr.echoes.labs.ksf.extensions.projects.IProjectLifecycleExtension;
import fr.echoes.labs.ksf.extensions.projects.NotifyResult;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService;
import fr.echoes.labs.pluginfwk.api.plugin.Plugin;

/**
 * @author dcollard
 *
 */
@Order(value = 1)
@Plugin
public class RedmineProjectLifeCycleExtension implements IProjectLifecycleExtension {

	private static final Logger			LOGGER	= LoggerFactory.getLogger(RedmineProjectLifeCycleExtension.class);

	@Autowired
	private RedmineErrorHandlingService	errorHandler;

	@Autowired
	private IRedmineService				redmineService;

	@Autowired
	private IProjectDAO					projectDAO;

	@Autowired
	private ApplicationContext			applicationContext;

	@Autowired
	private RedmineConfigurationService	configurationService;

	private ICurrentUserService			currentUserService;

	public void init() {

		if (this.currentUserService == null) {
			this.currentUserService = this.applicationContext.getBean(ICurrentUserService.class);
		}
	}

	@Override
	public NotifyResult notifyCreatedFeature(final ProjectDto project, final String ticketId, final String featureSubject, final String username) {

		try {
			this.redmineService.changeStatus(ticketId, this.configurationService.getFeatureStatusAssignedId(), username);

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to change ticket #{} status", ticketId, ex);
			this.errorHandler.registerErrorMessage("Failed to change Redmine ticket status.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedProject(final ProjectDto project) {

		this.init();

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
			this.errorHandler.registerErrorMessage("Unable to create Redmine project.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyCreatedRelease(final ProjectDto project, final String releaseVersion, final String username) {
		try {

			final String releaseTicketSubject = this.createReleaseTicketSubject(project, releaseVersion);

			this.redmineService.createTicket(project, releaseVersion, releaseTicketSubject, username);
		} catch (final RedmineExtensionException e) {
			LOGGER.error("[Redmine] Failed to create a ticket for the release {} of the project {}", releaseVersion, project.getName());
			this.errorHandler.registerErrorMessage("Failed to create a Redmine ticket for the release.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDeletedProject(final ProjectDto project) {

		try {

			this.redmineService.deleteProject(project.getName());

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to delete project {} ", project.getName(), ex);
			this.errorHandler.registerErrorMessage("Unable to delete Redmine project.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyDuplicatedProject(final ProjectDto _project) {
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedFeature(final ProjectDto projectDto, final String ticketId, final String featureSubject) {
		try {
			this.redmineService.changeStatus(ticketId, this.configurationService.getFeatureStatusClosedId(), null);

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to change ticket #{} status", ticketId, ex);
			this.errorHandler.registerErrorMessage("Failed to change Redmine ticket status.");
		}
		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyFinishedRelease(final ProjectDto project, final String releaseName) {

		try {
			this.redmineService.changeVersionStatus(project, releaseName, Version.STATUS_CLOSED);

		} catch (final Exception ex) {
			LOGGER.error("[Redmine] Failed to change version '{}' state status", releaseName, ex);
			this.errorHandler.registerError(new RedmineException("Failed to change Redmine version state."));
		}

		return NotifyResult.CONTINUE;
	}

	@Override
	public NotifyResult notifyUpdatedProject(final ProjectDto _project) {
		return NotifyResult.CONTINUE;
	}

	private String createReleaseTicketSubject(final ProjectDto project, final String releaseVersion) {
		final Map<String, String> variables = new HashMap<>(2);
		variables.put("projectName", project.getName());
		variables.put("releaseVersion", releaseVersion);
		return this.replaceVariables(this.configurationService.getReleaseTicketMessagePattern(), variables);
	}

	private String replaceVariables(final String str, final Map<String, String> variables) {
		final StrSubstitutor sub = new StrSubstitutor(variables);
		sub.setVariablePrefix("%{");
		return sub.replace(str);
	}

}
