package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.extensions.services.project.IValidator;
import fr.echoes.labs.ksf.cc.extensions.services.project.IValidatorResult;
import fr.echoes.labs.ksf.cc.extensions.services.project.ValidatorResult;
import fr.echoes.labs.ksf.cc.extensions.services.project.ValidatorResultType;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery.Builder;

public class RedmineTicketValidator implements IValidator {

	@Autowired
	IRedmineService redmine;

	@Autowired
	private RedmineConfigurationService configurationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineTicketValidator.class);

	@Override
	public List<IValidatorResult> validateFeature(String projectName,
			String featureId, String description) {
		return null;
	}

	@Override
	public List<IValidatorResult> validateRelease(String projectName,
			String releaseName) {
		final Builder redmineQuerryBuilder = new RedmineQuery.Builder();

		redmineQuerryBuilder.projectName(projectName)
		                    .trackerId(this.configurationService.getFeatureTrackerId());

		final List<IValidatorResult> result = new ArrayList<IValidatorResult>();

		try {

			final List<RedmineIssue> newIssues = getIssues(releaseName, this.configurationService.getFeatureStatusNewId());
			final List<RedmineIssue> assignedIssues = getIssues(releaseName, this.configurationService.getFeatureStatusAssignedId());

			for (final RedmineIssue issue : newIssues) {
				final IValidatorResult validatorResult = new ValidatorResult(ValidatorResultType.ERROR, '#' + issue.getId() + " " + issue.getSubject());
				result.add(validatorResult);
			}

			for (final RedmineIssue issue : assignedIssues) {
				final IValidatorResult validatorResult = new ValidatorResult(ValidatorResultType.ERROR, '#' + issue.getId() + " " + issue.getSubject());
				result.add(validatorResult);
			}

		} catch (final RedmineExtensionException e) {
			LOGGER.error("Failed to retrieve tickets while validating release '" + releaseName + "'");
		}


		return result;
	}

	private List<RedmineIssue> getIssues(String releaseName, int issueStatusId) throws RedmineExtensionException {
		final Builder requestBuilder = new RedmineQuery.Builder();
		requestBuilder
			.trackerId(this.configurationService.getFeatureTrackerId())
			.statusId(issueStatusId)
			.setTargetVersion(releaseName);

		final RedmineQuery query = requestBuilder.build();
		final List<RedmineIssue> issues = this.redmine.queryIssues(query);
		LOGGER.info("Validating release : {} tickets with status ID '{}'", issues.size());
		return issues;
	}



}
