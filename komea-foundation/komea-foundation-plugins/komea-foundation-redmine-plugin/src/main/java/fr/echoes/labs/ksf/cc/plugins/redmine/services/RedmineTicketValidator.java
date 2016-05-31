package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.ksf.cc.extensions.services.project.IValidator;
import fr.echoes.labs.ksf.cc.extensions.services.project.IValidatorResult;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery.Builder;

public class RedmineTicketValidator implements IValidator {

	@Autowired
	IRedmineService redmine;

	@Autowired
	private RedmineConfigurationService configurationService;

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


		final RedmineQuery query = redmineQuerryBuilder.build();
		return null;
	}



}
