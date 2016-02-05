package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.ProjectFactory;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Version;

import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.lab.ksf.cc.plugins.redmine.RedmineQuery;



/**
 * Spring Service for working with the foreman API.
 *
 * @author dcollard
 *
 */
@Service
public class RedmineService implements IRedmineService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineService.class);

	@Autowired
	private RedmineConfigurationService configurationService;

	private RedmineManager createRedmineManager() {
		final String apiAccessKey = this.configurationService.getApiAccessKey();
		final String url = this.configurationService.getUrl();
		final RedmineManager mgr = RedmineManagerFactory.createWithApiKey(url, apiAccessKey);
		return mgr;
	}

	@Override
	public void createProject(String projectName) throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final RedmineManager mgr = createRedmineManager();
		final ProjectManager projectManager = mgr.getProjectManager();
		final Project project = ProjectFactory.create(projectName, projectName);

		try {
			projectManager.createProject(project);
		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to create projet " + projectName, e);
		}
	}


	@Override
	public void deleteProject(String projectName) throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final RedmineManager mgr = createRedmineManager();
		final ProjectManager projectManager = mgr.getProjectManager();
		try {
			projectManager.deleteProject(projectName);
		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to delete projet " + projectName, e);
		}

	}

	@Override
	public List<RedmineIssue> queryIssues(RedmineQuery query) throws RedmineExtensionException {
		Objects.requireNonNull(query);

		final List<RedmineIssue> redmineIssues = new ArrayList<>();

		final RedmineManager mgr = createRedmineManager();

		List<Issue> issues;
		try {
			final IssueManager issueManager = mgr.getIssueManager();
			issues = issueManager.getIssues(query.getProjectName(), null);
		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to list the issues of " + query.getProjectName(), e);
		}
		for (final Issue issue : issues) {

			final RedmineIssue redmiIssue = createRedmineIssue(issue);
			redmineIssues.add(redmiIssue);

		}
		return redmineIssues;
	}

	private RedmineIssue createRedmineIssue(final Issue issue) {
		final RedmineIssue redmiIssue = new RedmineIssue(issue.getId());

		final User user = issue.getAssignee();
		final String assigneFullName = user == null ? StringUtils.EMPTY : StringUtils.defaultString(user.getFullName());
		redmiIssue.setAssignee(assigneFullName);


		final IssueCategory category = issue.getCategory();
		final String categoryName = category == null ? StringUtils.EMPTY : StringUtils.defaultString(category.getName());

		redmiIssue.setCategory(categoryName);

		redmiIssue.setName(StringUtils.defaultString(issue.getSubject()));

		redmiIssue.setPriority(StringUtils.defaultString(issue.getPriorityText()));
		redmiIssue.setStatus(StringUtils.defaultString(issue.getStatusName()));

		final Version targetVersion = issue.getTargetVersion();
		final String targetVersionName = targetVersion == null ? StringUtils.EMPTY : StringUtils.defaultString(targetVersion.getName());
		redmiIssue.setTargetVersion(targetVersionName);

		final Tracker tracker = issue.getTracker();
		final String trackerName = tracker == null ? StringUtils.EMPTY : StringUtils.defaultString(tracker.getName());

		redmiIssue.setTracker(trackerName);
		return redmiIssue;
	}

}
