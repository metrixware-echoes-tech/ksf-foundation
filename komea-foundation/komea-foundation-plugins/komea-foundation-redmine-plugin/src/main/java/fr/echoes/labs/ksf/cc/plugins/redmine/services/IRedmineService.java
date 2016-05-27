package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.List;

import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;


/**
 * @author dcollard
 *
 */
public interface IRedmineService {

	/**
	 * Creates a new project.
	 *
	 * @param projectName the project name.
	 * @return
	 */
	public void createProject(String projectName) throws RedmineExtensionException;

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
	 * @return the project ID or {@code null} if the given project cannot be found.
	 * @throws RedmineExtensionException
	 */
	public String getProjectId(String projectName) throws RedmineExtensionException;

	/**
	 * Changes the ticket status.
	 *
	 * @param ticketId the ticket ID.
	 * @param statusId the new status.
	 * @throws RedmineExtensionException
	 */
	public void changeStatus(String ticketId, int statusId) throws RedmineExtensionException;

	/**
	 * Creates a Redmine ticket.
	 *
	 * @param project the project
	 * @param releaseVersion the release version
	 * @param string the ticket title
	 * @throws RedmineExtensionException
	 */
	public void createTicket(ProjectDto project, String releaseVersion, String title) throws RedmineExtensionException;

}
