package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.text.Normalizer;
import java.util.ArrayList;
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

import fr.echoes.labs.ksf.cc.extensions.services.project.IProjectFeature;
import fr.echoes.labs.ksf.cc.extensions.services.project.ProjectFeatue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineIssue;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery;
import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineQuery.Builder;



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
		RedmineManager mgr = null;
		try {
			final String apiAccessKey = this.configurationService.getApiAccessKey();
			final String url = this.configurationService.getUrl();
			mgr = RedmineManagerFactory.createWithApiKey(url, apiAccessKey);
		} catch (final Exception e) {
			LOGGER.error("Failed to create Redmine manager", e);
		}
		return mgr;
	}

	@Override
	public void createProject(String projectName) throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final RedmineManager mgr = createRedmineManager();
		final ProjectManager projectManager = mgr.getProjectManager();
		final Project project = ProjectFactory.create(projectName, createIdentifier(projectName));

		try {
			projectManager.createProject(project);

		} catch (final RedmineException e) {
			throw new RedmineExtensionException("Failed to create projet " + projectName, e);
		}
	}

	private String createIdentifier(String projectName) {
		return  Normalizer.normalize(projectName, Normalizer.Form.NFD).replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+","-" ).toLowerCase();
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
			if (query.getStatusId() != -1 && query.getStatusId() != issue.getStatusId()) {
				continue;
			}

			if (query.getTrackerId() != -1 && issue.getTracker() != null && query.getTrackerId() != issue.getTracker().getId()) {
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

		for (Project project : projectManager.getProjects()) {
			if (projectName.equals(project.getName())) {
				return project.getIdentifier();
			}
		}
		return null;
	}

	@Override
	public List<String> getVersions(String projectName) throws RedmineExtensionException {
		Objects.requireNonNull(projectName);

		final List<String> result;
		final RedmineManager mgr = createRedmineManager();
		final ProjectManager projectManager = mgr.getProjectManager();
		final String projectKey =  createIdentifier(projectName);
		try {
			Project project = projectManager.getProjectByKey(projectKey);
			final List<Version> versions = projectManager.getVersions(project.getId());

			final Map<String, List<String>> projectVersionMap = new HashMap<String, List<String>>();

			// Loop through the list of versions and build a map with the projects names as keys and lists
			// of versions as values. The version with another status than "open" are ignored.
			for (Version version : versions) {

				if (!Version.STATUS_OPEN.equals(version.getStatus())) {
					LOGGER.info("[Redmine] The {} version \"{}\" was ignored.", version.getStatus(), version.getName());
					continue;
				}

				final String versionProjectName = version.getProject().getName();

				List<String> versionList = projectVersionMap.get(versionProjectName);
				if (versionList == null) {
					versionList = new ArrayList<String>();
					projectVersionMap.put(versionProjectName, versionList);
				}
				versionList.add(version.getName());
			}

			if (!versions.isEmpty()) {

				// If there are versions directly related to the project (same project name) only these versions will be returned.
				// If the versions are not directly related to the project all the versions will be returned.
				if (projectVersionMap.containsKey(projectName)) {
					result = projectVersionMap.get(projectName);
				} else {
					result = new ArrayList<String>();
					for (Map.Entry<String, List<String>> entry : projectVersionMap.entrySet()) {
						result.addAll(entry.getValue());
					}

				}

			} else {
				result = Collections.<String>emptyList();
			}


		} catch (RedmineException e) {
			throw new RedmineExtensionException("Failed to retrieve project \"" + projectName +"\" versions", e);
		}
		return result;
	}

	@Override
	public List<IProjectFeature> getFeatures(String projectName)
			throws RedmineExtensionException {
		Objects.requireNonNull(projectName);
		final Builder redmineQuerryBuilder = new RedmineQuery.Builder();

		redmineQuerryBuilder.projectName(projectName)
		                    .trackerId(configurationService.getFeatureTrackerId())
		                    .statusId(configurationService.getFeatureStatusNewId());

		
		final RedmineQuery query = redmineQuerryBuilder.build();
		final List<RedmineIssue> issues = queryIssues(query);
		
		final List<IProjectFeature> features = new ArrayList<IProjectFeature>(issues.size());
		for (RedmineIssue issue : issues) {
			ProjectFeatue feature = new ProjectFeatue();
			feature.setId(String.valueOf(issue.getId()));
			feature.setSubject(issue.getSubject());
			feature.setAssignee(issue.getAssignee());
			features.add(feature);
		}
		return features;
	}

}
