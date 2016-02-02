package fr.echoes.lab.ksf.cc.plugins.redmine.services;

import java.util.List;

import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineQuery;

/**
 * @author DCD
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
	 * Get the Redmine issues for the given query.
	 *
	 * @param query
	 * @return
	 */
	public List<RedmineIssue> queryIssues(RedmineQuery query);

}
