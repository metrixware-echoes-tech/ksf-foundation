package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import java.util.List;

/**
 * @author dcollard
 *
 */
public interface IRedmineService {

    /**
     * Creates a new project.
     *
     * @param projectName the project name.
     * @param username the current username. This user will be add as a member
     * of the new project.
     * @return
     */
    public void createProject(String projectName, String username) throws RedmineExtensionException;

    /**
     * Deletes the given project.
     *
     * @param projectName the project key.
     * @return
     */
    public void deleteProject(String projectName) throws RedmineExtensionException;

    /**
     * Gets the Redmine issues for the given query.
     *
     * @param query
     * @return
     */
    public List<RedmineIssue> queryIssues(RedmineQuery query) throws RedmineExtensionException;

    /**
     * Returns the list of versions for this project.
     *
     * @param projectName the project name
     * @return a list of versions as a list of strings.
     * @throws RedmineExtensionException
     */
    public List<IProjectVersion> getVersions(String projectName) throws RedmineExtensionException;

    /**
     * Returns the list of features for this project.
     *
     * @param projectName the project name
     * @return a list of features
     * @throws RedmineExtensionException
     */
    public List<IProjectFeature> getFeatures(String projectName) throws RedmineExtensionException;

    /**
     * Returns the Redmine project ID.
     *
     * @param projectName the project name.
     * @return the project ID or {@code null} if the given project cannot be
     * found.
     * @throws RedmineExtensionException
     */
    public String getProjectId(String projectName) throws RedmineExtensionException;

    /**
     * Changes the ticket status.
     *
     * @param ticketId the ticket ID.
     * @param statusId the new status.
     * @param username the assignee username. Can be {@code null}.
     * @throws RedmineExtensionException
     */
    public void changeStatus(String ticketId, int statusId, String username) throws RedmineExtensionException;

    /**
     * Creates a Redmine ticket.
     *
     * @param project the project
     * @param releaseVersion the release version
     * @param username the assignee username. Can be {@code null}.
     * @param string the ticket title
     * @param trackerId the tracker to use for this issue. Can be {@code null}.
     * @throws RedmineExtensionException
     */
    public void createTicket(ProjectDto project, String releaseVersion, String title, String username, Integer trackerId) throws RedmineExtensionException;

    public void changeVersionStatus(ProjectDto foundationProject, String releaseName, String status) throws RedmineExtensionException;

    /**
     * Reject the ticket.
     *
     * @param ticketId the ticket ID.
     * @param username the assignee username. Can be {@code null}.
     * @throws RedmineExtensionException
     */
    void rejectIssue(String ticketId, String username) throws RedmineExtensionException;

}
