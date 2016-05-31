package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.ksf.cc.extensions.services.project.IValidator;
import fr.echoes.labs.ksf.cc.extensions.services.project.IValidatorResult;

public class JenkinsBuildValidator implements IValidator {

	@Autowired
	IJenkinsService jenkins;

	@Autowired
	private JenkinsConfigurationService configuration;

	private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsBuildValidator.class);

	@Override
	public List<IValidatorResult> validateFeature(String projectName,
			String featureId, String description) {

		try {
			final JenkinsBuildInfo buildInfo = this.jenkins.getFeatureStatus(projectName, featureId, description);
		} catch (final JenkinsExtensionException e) {
			LOGGER.error("Failed to retrieve Jenkins build information while validating feature '" + featureId + "'");
		}
		return null;
	}

	@Override
	public List<IValidatorResult> validateRelease(String projectName,
			String releaseName) {

		try {
			final JenkinsBuildInfo buildInfo = this.jenkins.getReleaseStatus(projectName, releaseName);
		} catch (final JenkinsExtensionException e) {
			LOGGER.error("Failed to retrieve Jenkins build information while validating release '" + releaseName + "'");
		}

		return null;
	}


}
