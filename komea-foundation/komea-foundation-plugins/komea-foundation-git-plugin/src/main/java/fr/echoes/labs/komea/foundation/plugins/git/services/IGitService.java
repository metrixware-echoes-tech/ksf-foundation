package fr.echoes.labs.komea.foundation.plugins.git.services;

import fr.echoes.labs.komea.foundation.plugins.git.exceptions.GitExtensionException;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

/**
 * @author dcollard
 *
 */
public interface IGitService {

    /**
     * Creates a new project.
     *
     * @param projectName the project name.
     * @return
     */
    public void createProject(ProjectDto project) throws GitExtensionException;

    /**
     * Deletes the given project.
     *
     * @param project the project.
     * @return
     */
    public void deleteProject(ProjectDto project) throws GitExtensionException;

    /**
     * Creates a new branch for the given release.
     *
     * @param project the project
     * @param releaseVersion the release version
     * @throws GitExtensionException
     */
    public void createRelease(ProjectDto project, String releaseVersion) throws GitExtensionException;

    /**
     * Creates a new branch for the given feature.
     *
     * @param project the project
     * @param featureId the feature ID
     * @param featureSubject the feature subject
     */
    public void createFeature(ProjectDto project, String featureId, String featureSubject) throws GitExtensionException;

    /**
     * Closes the feature.
     *
     * @param project the project
     * @param featureId the feature ID
     * @param featureSubject the feature subject
     * @throws GitExtensionException
     */
    public void closeFeature(ProjectDto project, String featureId, String featureSubject) throws GitExtensionException;

    /**
     * Cancels the feature.
     *
     * @param project the project
     * @param featureId the feature ID
     * @param featureSubject the feature subject
     * @throws GitExtensionException
     */
    public void cancelFeature(ProjectDto project, String featureId, String featureSubject) throws GitExtensionException;

    /**
     * Closes the release.
     *
     * @param project the project
     * @param releaseName the feature name
     * @throws GitExtensionException
     */
    public void closeRelease(ProjectDto project, String releaseName) throws GitExtensionException;

}
