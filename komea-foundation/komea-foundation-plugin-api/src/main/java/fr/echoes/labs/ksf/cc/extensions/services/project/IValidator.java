package fr.echoes.labs.ksf.cc.extensions.services.project;

import java.util.List;

import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

/**
 * @author dcollard
 *
 */
public interface IValidator {

	/**
	 * Validates a feature.
	 *
	 * @param projectName the project name.
	 * @param featureId the feature ID.
	 * @param description the feature description.
	 * @return
	 */
	public List<IValidatorResult> validateFeature(ProjectDto project, String featureId, String description);

	/**
	 * Validates a release.
	 *
	 * @param projectName the project name.
	 * @param releaseName the release name.
	 * @return
	 */
	public List<IValidatorResult> validateRelease(ProjectDto project, String releaseName);

}
