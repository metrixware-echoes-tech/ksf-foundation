package fr.echoes.labs.komea.foundation.plugins.jenkins.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.echoes.labs.komea.foundation.plugins.jenkins.JenkinsExtensionException;
import fr.echoes.labs.ksf.cc.extensions.services.project.IValidator;
import fr.echoes.labs.ksf.cc.extensions.services.project.IValidatorResult;
import fr.echoes.labs.ksf.cc.extensions.services.project.ValidatorResult;
import fr.echoes.labs.ksf.cc.extensions.services.project.ValidatorResultType;

/**
 * @author dcollard
 *
 */
@Service
public class JenkinsBuildValidator implements IValidator {

	@Autowired
	IJenkinsService jenkins;

	@Autowired
	private JenkinsConfigurationService configuration;

	private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsBuildValidator.class);

	/**
	 * Validates that the latest build for this feature was successful .
	 *
	 * @param projectName the project name.
	 * @param featureId the feature ID.
	 * @param description the feature description.
	 * @return
	 */
	@Override
	public List<IValidatorResult> validateFeature(String projectName,
			String featureId, String description) {
		List<IValidatorResult>  results = null;
		try {
			final JenkinsBuildInfo buildInfo = this.jenkins.getFeatureStatus(projectName, featureId, description);
			if (buildInfo == null || !"SUCCESS".equals(buildInfo.getResult())) {
				results = new ArrayList<IValidatorResult>(1);
				final IValidatorResult result;
				if (buildInfo == null) {
					result = new ValidatorResult(ValidatorResultType.ERROR, "JENKINS - Échec de la récupération des informations de build de la feature '" + featureId + "'." );
				} else {
					result = new ValidatorResult(ValidatorResultType.ERROR, "JENKINS - Le dernier build du job '" + buildInfo.getJobName() + "' est en échec.");
				}
				results.add(result);
			}

		} catch (final JenkinsExtensionException e) {
			LOGGER.error("Failed to retrieve Jenkins build information while validating feature '" + featureId + "'");
		}
		return results;
	}

	/**
	 * Validates that the latest build for this release was successful .
	 *
	 * @param projectName the project name.
	 * @param releaseName the release name.
	 * @return
	 */
	@Override
	public List<IValidatorResult> validateRelease(String projectName,
			String releaseName) {
		List<IValidatorResult>  results = null;
		try {
			final JenkinsBuildInfo buildInfo = this.jenkins.getReleaseStatus(projectName, releaseName);
			if (buildInfo == null || !"SUCCESS".equals(buildInfo.getResult())) {
				results = new ArrayList<IValidatorResult>(1);
				final IValidatorResult result;
				if (buildInfo == null) {
					result = new ValidatorResult(ValidatorResultType.ERROR, "JENKINS - Échec de la récupération des informations de build de la release '" + releaseName + "'." );
				} else {
					result = new ValidatorResult(ValidatorResultType.ERROR, "JENKINS - Le dernier build du job '" + buildInfo.getJobName() + "' est en échec.");
				}
				results.add(result);
			}

		} catch (final JenkinsExtensionException e) {
			LOGGER.error("Failed to retrieve Jenkins build information while validating release '" + releaseName + "'");
		}

		return results;
	}


}
