package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import fr.echoes.labs.ksf.cc.extensions.services.project.IValidator;
import fr.echoes.labs.ksf.cc.extensions.services.project.IValidatorResult;
import fr.echoes.labs.ksf.cc.extensions.services.project.ValidatorResult;
import fr.echoes.labs.ksf.cc.extensions.services.project.ValidatorResultType;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery.Builder;

/**
 * @author dcollard
 *
 */
@Service
public class RedmineTicketValidator implements IValidator {

	@Autowired
	IRedmineService redmine;

	@Autowired
	private RedmineConfigurationService configurationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineTicketValidator.class);

	@Autowired
	private MessageSource messageResource;

	@Override
	public List<IValidatorResult> validateFeature(String projectName,
			String featureId, String description) {
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
	public List<IValidatorResult> validateRelease(String projectName,
			String releaseName) {
		final Builder redmineQuerryBuilder = new RedmineQuery.Builder();

		redmineQuerryBuilder.projectName(projectName)
		                    .trackerId(this.configurationService.getFeatureTrackerId());

		final List<IValidatorResult> result = new ArrayList<IValidatorResult>();

		try {

			final List<RedmineIssue> issues = new ArrayList<RedmineIssue>();

			addIssuesToList(projectName, releaseName, issues, this.configurationService.getFeatureStatusNewId());
			addIssuesToList(projectName, releaseName, issues, this.configurationService.getFeatureStatusAssignedId());

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

	private void addIssuesToList(String projectName, String releaseName, final List<RedmineIssue> issues, int issueStatusId) throws RedmineExtensionException {
		final List<RedmineIssue> newIssues = getIssues(projectName, releaseName, issueStatusId);
		if (newIssues != null ) {
			issues.addAll(newIssues);
		}
	}

	private List<RedmineIssue> getIssues(String projectName, String releaseName, int issueStatusId) throws RedmineExtensionException {
		final Builder requestBuilder = new RedmineQuery.Builder();
		requestBuilder
			.trackerId(this.configurationService.getFeatureTrackerId())
			.statusId(issueStatusId)
			.setTargetVersion(releaseName);

		final RedmineQuery query = requestBuilder.projectName(projectName).build();
		final List<RedmineIssue> issues = this.redmine.queryIssues(query);
		LOGGER.info("Validating release : {} tickets with status ID '{}'", issues.size());
		return issues;
	}



}
