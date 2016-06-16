package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.MembershipManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.ProjectFactory;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Version;

import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectUtils;
import fr.echoes.labs.ksf.cc.extensions.services.project.TicketStatus;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.IProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.features.ProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.IProjectVersion;
import fr.echoes.labs.ksf.cc.extensions.services.project.versions.ProjectVersion;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery.Builder;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;



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
	private RedmineConfigurationService configuration;

	private RedmineManager createRedmineManager() {
		RedmineManager mgr = null;
		try {
			final String apiAccessKey = this.configuration.getApiAccessKey();
			final String url = this.configuration.getUrl();
			mgr = RedmineManagerFactory.createWithApiKey(url, apiAccessKey);
		} catch (final Exception e) {
			LOGGER.error("Failed to create Redmine manager", e);
		}
		return mgr;
	}

	@Override
	public void createProject(String projectName, String username) throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final RedmineManager mgr = createRedmineManager();

		final ProjectManager projectManager = mgr.getProjectManager();
		final Project project = ProjectFactory.create(projectName, ProjectUtils.createIdentifier(projectName));

		try {

			final Project redmineProject = projectManager.createProject(project);

			final Collection<Tracker> trackers = redmineProject.getTrackers();
			for (final Tracker t : trackers) {
				System.out.println(t.getId() + " name: " + t.getName());
			}


			addProjectMember(mgr, redmineProject, this.configuration.getAdminUserName());
			addProjectMember(mgr, redmineProject, username);

		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to create projet " + projectName, e);
		}
	}

	private void addProjectMember(final RedmineManager mgr, final Project redmineProject, String username) throws RedmineException {
		final User adminUser = findUser(mgr, username);
		if (adminUser != null) {
			final UserManager userManager = mgr.getUserManager();
			final MembershipManager membershipManager = mgr.getMembershipManager();
			membershipManager.createMembershipForUser(redmineProject.getId(), adminUser.getId(), userManager.getRoles());
		}
	}

	private User findUser(RedmineManager mgr, String username) throws RedmineException {
		final User cachedUser = RedmineUserCache.INSTANCE.get(username);
		if (cachedUser != null) {
			LOGGER.info("[redmine] cached user '{}' found", username);
			return cachedUser;
		}

		final UserManager userManager = mgr.getUserManager();
		for (final User user : userManager.getUsers()) {
			if (StringUtils.equalsIgnoreCase(user.getLogin(), username)) {
				LOGGER.info("[redmine] user '{}' found", username);
				RedmineUserCache.INSTANCE.put(username, user);
				return user;
			}
		}

		LOGGER.info("[redmine] user '{}' not found", username);
		return null;
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


		final RedmineManager redmineManager = createRedmineManager();
		if (redmineManager == null) {
			return Collections.<RedmineIssue>emptyList();
		}

		List<Issue> issues;
		final List<RedmineIssue> redmineIssues;
		final String projectName = query.getProjectName();
		final int resultItemsLimit = query.getResultItemsLimit();
		final String requestTargetVersion = query.getTargetVersion();
		final int resultNumberOfItems;

		try {
			final IssueManager issueManager = redmineManager.getIssueManager();

			final String projectIdentifier = findProjectIdentifier(redmineManager, projectName);
			if (projectIdentifier == null) {
				throw new RedmineExtensionException("Cannot find project " + projectName);
			}

			issues = issueManager.getIssues(projectIdentifier, null);
			if (resultItemsLimit > 0) {
				resultNumberOfItems = Math.min(issues.size(), resultItemsLimit);
			} else {
				resultNumberOfItems = issues.size();
			}
			redmineIssues = new ArrayList<>(resultNumberOfItems);

		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to list the issues of " + projectName, e);
		}

		for (int i = 0; i < resultNumberOfItems; i++) {
			final Issue issue = issues.get(i);

			if (requestTargetVersion != null) {
				final Version issueTargetVersion = issue.getTargetVersion();
				if (issueTargetVersion != null && !StringUtils.equals(requestTargetVersion, issueTargetVersion.getName())) {
					continue;
				}
			}

			if (!query.getStatusIds().isEmpty()  && !query.getStatusIds().contains(issue.getStatusId())) {
				continue;
			}

			if (!query.getTrackerIds().isEmpty() && issue.getTracker() != null && !query.getTrackerIds().contains(issue.getTracker().getId())) {
				continue;
			}

			redmineIssues.add(createRedmineIssue(issue));
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

		redmiIssue.setSubject(StringUtils.defaultString(issue.getSubject()));

		redmiIssue.setPriority(StringUtils.defaultString(issue.getPriorityText()));
		redmiIssue.setStatus(StringUtils.defaultString(issue.getStatusName()));
		redmiIssue.setStatusId(issue.getStatusId());

		final Version targetVersion = issue.getTargetVersion();
		final String targetVersionName = targetVersion == null ? StringUtils.EMPTY : StringUtils.defaultString(targetVersion.getName());
		redmiIssue.setTargetVersion(targetVersionName);

		final Tracker tracker = issue.getTracker();
		final String trackerName = tracker == null ? StringUtils.EMPTY : StringUtils.defaultString(tracker.getName());

		redmiIssue.setTracker(trackerName);
		return redmiIssue;
	}


	private String findProjectIdentifier(RedmineManager redmineManager, String projectName) throws RedmineException {
		final ProjectManager projectManager = redmineManager.getProjectManager();

		for (final Project project : projectManager.getProjects()) {
			if (projectName.equals(project.getName())) {
				return project.getIdentifier();
			}
		}
		return null;
	}

	@Override
	public List<IProjectVersion> getVersions(String projectName) throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final List<IProjectVersion> result;
		final RedmineManager mgr = createRedmineManager();
		final ProjectManager projectManager = mgr.getProjectManager();
		final String projectKey =  ProjectUtils.createIdentifier(projectName);
		try {
			final Project project = projectManager.getProjectByKey(projectKey);
			final List<Version> versions = projectManager.getVersions(project.getId());



			final Map<String, List<IProjectVersion>> projectVersionMap = new HashMap<String, List<IProjectVersion>>();

			// Loop through the list of versions and build a map with the projects names as keys and lists
			// of versions as values. The version with another status than "open" are ignored.
			for (final Version version : versions) {

				if (!Version.STATUS_OPEN.equals(version.getStatus())) {
					LOGGER.info("[redmine] The {} version \"{}\" was ignored.", version.getStatus(), version.getName());
					continue;
				}

				final String versionProjectName = version.getProject().getName();

				List<IProjectVersion> versionList = projectVersionMap.get(versionProjectName);
				if (versionList == null) {
					versionList = new ArrayList<IProjectVersion>();
					projectVersionMap.put(versionProjectName, versionList);
				}
				final ProjectVersion projectVersion = new ProjectVersion();
				projectVersion.setId(String.valueOf(version.getId()));
				projectVersion.setName(version.getName());
				versionList.add(projectVersion);
			}

			if (!versions.isEmpty()) {

				// If there are versions directly related to the project (same project name) only these versions will be returned.
				// If the versions are not directly related to the project all the versions will be returned.
				if (projectVersionMap.containsKey(projectName)) {
					result = projectVersionMap.get(projectName);
				} else {
					result = new ArrayList<IProjectVersion>();
					for (final Map.Entry<String, List<IProjectVersion>> entry : projectVersionMap.entrySet()) {
						result.addAll(entry.getValue());
					}

				}

			} else {
				result = Collections.<IProjectVersion>emptyList();
			}


		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to retrieve project \"" + projectName +"\" versions", e);
		}
		return result;
	}

	@Override
	public List<IProjectFeature> getFeatures(String projectName)
			throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final List<IProjectFeature> features = buildFeaturesList(projectName, this.configuration.getFeatureIds());

		return features;
	}


	private List<IProjectFeature> buildFeaturesList(String projectName, List<Integer> trackerIds) throws RedmineExtensionException {
		final Builder redmineQuerryBuilder = new RedmineQuery.Builder();

		final Builder builder = redmineQuerryBuilder.projectName(projectName);

		for (final Integer trackerId : trackerIds) {
			builder.addTrackerId(trackerId);
		}


		builder.addStatusId(this.configuration.getFeatureStatusNewId());
		builder.addStatusId(this.configuration.getFeatureStatusAssignedId());


		final RedmineQuery query = redmineQuerryBuilder.build();
		final List<RedmineIssue> issues = queryIssues(query);

		final List<IProjectFeature> features = new ArrayList<IProjectFeature>(issues.size());
		for (final RedmineIssue issue : issues) {
			final ProjectFeature feature = new ProjectFeature();
			feature.setId(String.valueOf(issue.getId()));
			feature.setSubject(issue.getSubject());
			feature.setAssignee(issue.getAssignee());
			if (issue.getStatusId() != null) {
				if (issue.getStatusId().equals(this.configuration.getFeatureStatusNewId())) {
					feature.setStatus(TicketStatus.NEW);
				} else if (issue.getStatusId().equals(this.configuration.getFeatureStatusAssignedId())) {
					feature.setStatus(TicketStatus.CREATED);
				}
			}
			features.add(feature);
		}
		return features;
	}
	@Override
	public void changeStatus(String ticketId, int statusId, String username) throws RedmineExtensionException {

		LOGGER.info("[redmine] Changing redmine issue '{}' status to status ID '{}'", ticketId, statusId);

		final RedmineManager redmineManager = createRedmineManager();

		if (redmineManager == null) {
			return;
		}

		final IssueManager issueManager = redmineManager.getIssueManager();
		try {
			final Issue issue;
			LOGGER.info("[redmine] Changing redmine issue - property bug API is {}", this.configuration.isHackBugApi());
			if (this.configuration.isHackBugApi()) {
				issue = getIssueById(issueManager, Integer.valueOf(ticketId));
			} else {
				issue = issueManager.getIssueById(Integer.valueOf(ticketId));
			}

			issue.setStatusId(statusId);

			changeIssueAssignee(redmineManager, issue, username);

			issueManager.update(issue);
			LOGGER.info("[redmine] Changing redmine issue status - status updated");
		} catch (final Exception e) {
			LOGGER.error("[redmine] Failed to change issue '" + ticketId + "' status", e);
			throw new RedmineExtensionException("Failed to change ticket status.", e);
		}
	}

	private void changeIssueAssignee(RedmineManager redmineManager, Issue issue, String username)
			throws RedmineException {
		if (username != null) {
			final User user = findUser(redmineManager, username);
			if (user != null) {
				LOGGER.info("[redmine] Changing issue assignee to '{}'", username);
				issue.setAssignee(user);
			}
		}
	}

	private Issue getIssueById(IssueManager issueManager, Integer issueId) throws RedmineException {
		final List<Issue> issues = issueManager.getIssues(null, null);
		if (issues != null) {
			for (final Issue issue : issues) {
				if (issue.getId().equals(issueId)) {
					return issue;
				}
			}
		}
		return null;
	}

	@Override
	public String getProjectId(String projectName) throws RedmineExtensionException {

		Objects.requireNonNull(projectName);


		final RedmineManager redmineManager = createRedmineManager();
		if (redmineManager == null) {
			return null;
		}

		final String id;
		try {
			id = findProjectIdentifier(redmineManager, projectName);
		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to get project \"" + projectName + "\" ID", e);
		}
		return id;
	}

	@Override
	public void createTicket(ProjectDto foundationProject, String releaseVersion, String subject, String username) throws RedmineExtensionException {

		Objects.requireNonNull(foundationProject);
		Objects.requireNonNull(releaseVersion);

		final RedmineManager redmineManager = createRedmineManager();
		if (redmineManager == null) {
			return;
		}

		try {
			final String projectIdentifier = findProjectIdentifier(redmineManager, foundationProject.getName());
			final ProjectManager projectManager = redmineManager.getProjectManager();
			final IssueManager issueManager = redmineManager.getIssueManager();

			final Project redmineProject = projectManager.getProjectByKey(projectIdentifier);
			final Version version = findVersion(projectManager, redmineProject, releaseVersion);

			final Issue issue = new Issue();
			issue.setProject(redmineProject);
			issue.setTargetVersion(version);
			issue.setSubject(subject);
			changeIssueAssignee(redmineManager, issue, username);

			issueManager.createIssue(issue);

		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to create ticket", e);
		}
	}

	private Version findVersion(ProjectManager projectManager, Project redmineProject, String releaseVersion) throws RedmineException {
		final List<Version> versions = projectManager.getVersions(redmineProject.getId());
		for (final Version version : versions) {
			if (StringUtils.equals(releaseVersion, version.getName())) {
				return version;
			}
		}
		return null;
	}

	@Override
	public void changeVersionStatus(ProjectDto foundationProject, String releaseVersion, String status) throws RedmineExtensionException {
		Objects.requireNonNull(foundationProject);
		Objects.requireNonNull(releaseVersion);

		final RedmineManager redmineManager = createRedmineManager();
		if (redmineManager == null) {
			return;
		}

		try {
			final String projectIdentifier = findProjectIdentifier(redmineManager, foundationProject.getName());
			final ProjectManager projectManager = redmineManager.getProjectManager();

			final Project redmineProject = projectManager.getProjectByKey(projectIdentifier);
			final Version version = findVersion(projectManager, redmineProject, releaseVersion);
			version.setStatus(status);
			projectManager.update(version);

		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to change version state", e);
		}

	}


}
