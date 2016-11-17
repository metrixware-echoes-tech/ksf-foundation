package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.extensions.services.project.IValidator;
import fr.echoes.labs.ksf.cc.extensions.services.project.IValidatorResult;
import fr.echoes.labs.ksf.cc.extensions.services.project.ValidatorResult;
import fr.echoes.labs.ksf.cc.extensions.services.project.ValidatorResultType;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineConfigurationBean;
import fr.echoes.labs.ksf.cc.plugins.redmine.exceptions.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.model.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.utils.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.utils.RedmineQuery.Builder;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

/**
 * @author dcollard
 *
 */
@Service
public class RedmineTicketValidator implements IValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineTicketValidator.class);
	
	@Autowired
	private IRedmineService redmine;

	@Autowired
	private RedmineConfigurationService configurationService;
	
	@Autowired
	private RedmineNameResolver nameResolver;

	@Autowired
	private MessageSource messageResource;

	@Override
	public List<IValidatorResult> validateFeature(final ProjectDto project, final String featureId, String description) {
		return null;
	}

	/**
	 * Validates that all the the tickets for that release are closed.
	 *
	 * @param projectName the project name.
	 * @param releaseName the release name.
	 * @return
	 */
	@Override
	public List<IValidatorResult> validateRelease(final ProjectDto project, final String releaseName) {
		
		final Builder redmineQuerryBuilder = new RedmineQuery.Builder();
		
		final RedmineConfigurationBean configuration = this.configurationService.getConfigurationBean();
		final String projectKey = this.nameResolver.getProjectKey(project);
		
		redmineQuerryBuilder.projectKey(projectKey)
		                    .addTrackerId(configuration.getFeatureTrackerId());

		final List<IValidatorResult> result = Lists.newArrayList();

		try {

			final List<RedmineIssue> issues = Lists.newArrayList();

			addIssuesToList(projectKey, releaseName, issues, configuration.getFeatureStatusNewId());
			addIssuesToList(projectKey, releaseName, issues, configuration.getFeatureStatusAssignedId());

			final MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(this.messageResource);
			for (final RedmineIssue issue : issues) {
				final String message = messageSourceAccessor.getMessage("foundation.redmine.validator.issueNotClosed", new String[]{String.valueOf(issue.getId())});
				final IValidatorResult validatorResult = new ValidatorResult(ValidatorResultType.ERROR, message);
				result.add(validatorResult);
			}

		} catch (final RedmineExtensionException e) {
			LOGGER.error("Failed to retrieve tickets while validating release '" + releaseName + "'");
		}


		return result;
	}

	private void addIssuesToList(String projectKey, String releaseName, final List<RedmineIssue> issues, int issueStatusId) throws RedmineExtensionException {
		final List<RedmineIssue> newIssues = getIssues(projectKey, releaseName, issueStatusId);
		if (newIssues != null) {
			issues.addAll(newIssues);
		}
	}

	private List<RedmineIssue> getIssues(String projectKey, String releaseName, int issueStatusId) throws RedmineExtensionException {
		final Builder requestBuilder = new RedmineQuery.Builder();
		final RedmineConfigurationBean configuration = this.configurationService.getConfigurationBean();
		final Builder builder = requestBuilder
			.addStatusId(issueStatusId)
			.setTargetVersion(releaseName);

			for (final Integer trackerId : configuration.getFeatureIds()) {
				builder.addTrackerId(trackerId);
			}

		final RedmineQuery query = requestBuilder.projectKey(projectKey).build();
		final List<RedmineIssue> issues = this.redmine.queryIssues(query);
		LOGGER.info("Validating release : {} tickets with status ID '{}'", issues.size());
		return issues;
	}

}
