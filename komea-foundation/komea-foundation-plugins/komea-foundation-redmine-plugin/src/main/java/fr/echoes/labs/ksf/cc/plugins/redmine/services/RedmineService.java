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

import com.google.common.collect.Maps;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.MembershipManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.CustomFieldFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.ProjectFactory;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Version;

import fr.echoes.labs.ksf.cc.extensions.gui.ProjectExtensionConstants;
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
import fr.echoes.labs.ksf.users.security.api.CurrentUserService;

/**
 * Spring Service for working with the foreman API.
 *
 * @author dcollard
 *
 */
@Service
public class RedmineService implements IRedmineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedmineService.class);

    private final RedmineConfigurationService configuration;
    private final CurrentUserService currentUserService;
    private final RedmineNameResolver nameResolver;

    @Autowired
    public RedmineService(final RedmineConfigurationService configuration,
    		final RedmineNameResolver nameResolver,
            final CurrentUserService currentUserService) {
        this.configuration = configuration;
        this.currentUserService = currentUserService;
        this.nameResolver = nameResolver;
    }

    private RedmineManager getAdminRedmineManager() throws RedmineExtensionException {
        final String apiAccessKey = this.configuration.getApiAccessKey();
        final String url = this.configuration.getUrl();
        return RedmineManagerFactory.createWithApiKey(url, apiAccessKey);
    }

    private RedmineManager getUserRedmineManager() throws RedmineExtensionException {
        final String login = this.currentUserService.getCurrentUserLogin();
        final String apiAccessKey = this.getUserApiKey(login);
        final String url = this.configuration.getUrl();
        return RedmineManagerFactory.createWithApiKey(url, apiAccessKey);
    }
    
    @Override
    public boolean isAdmin() throws RedmineExtensionException, RedmineException {
    	
    	final RedmineManager manager = this.getUserRedmineManager();
    	final User user = manager.getUserManager().getCurrentUser();
    	
    	return user.getStatus() != null;
    }

    private User getUserByLogin(final String login, final RedmineManager redmineManager) throws RedmineException {
        final Map<String, String> params = Maps.newHashMap();
        params.put("name", login);
        final List<User> users = redmineManager.getUserManager().getUsers(params);
        User userByLogin = null;
        for (final User us : users) {
            if (us.getLogin().equals(login)) {
                userByLogin = us;
                break;
            }
        }
        return userByLogin;
    }

    private String getUserApiKey(final String username) throws RedmineExtensionException {
        String apiKey = null;
        final RedmineManager redmineManager = this.getAdminRedmineManager();
        try {
            final User user = this.getUserByLogin(username, redmineManager);
            if (user == null) {
                throw new RedmineExtensionException("Redmine user with login " + username + " does not exist.");
            }
            apiKey = redmineManager.getUserManager().getUserById(user.getId()).getApiKey();
            if (apiKey == null) {
                throw new RedmineExtensionException("Redmine api-key for user " + username + " does not exist.");
            }
        } catch (RedmineException ex) {
            throw new RedmineExtensionException("Fail to get Redmine users.", ex);
        }
        return apiKey;
    }

    @Override
    public void createProject(final ProjectDto ksfProject, String username) throws RedmineExtensionException {
       
    	Objects.requireNonNull(ksfProject);

    	final String projectKey = this.nameResolver.getProjectKey(ksfProject);
        final RedmineManager redmineManager = getAdminRedmineManager();
        final ProjectManager projectManager = redmineManager.getProjectManager();
        final Project project = ProjectFactory.create(ksfProject.getName(), projectKey);

        try {

            final Project redmineProject = projectManager.createProject(project);

            final Collection<Tracker> trackers = redmineProject.getTrackers();
            for (final Tracker t : trackers) {
                LOGGER.info(t.getId() + " name: " + t.getName());
            }

            final String adminUserName = this.configuration.getAdminUserName();
            this.addProjectMember(redmineProject, adminUserName, redmineManager);
            if (!adminUserName.equals(username)) {
                this.addProjectMember(redmineProject, username, redmineManager);
            }
            
            ksfProject.getOtherAttributes().put(ProjectExtensionConstants.REDMINE_KEY, projectKey);

        } catch (final RedmineException e) {
            throw new RedmineExtensionException("Failed to create projet " + ksfProject.getName(), e);
        }
    }

    private void addProjectMember(final Project redmineProject, String username, RedmineManager redmineManager)
            throws RedmineException, RedmineExtensionException {
        final User adminUser = findUser(username);
        if (adminUser != null) {
            final UserManager userManager = redmineManager.getUserManager();
            final MembershipManager membershipManager = redmineManager.getMembershipManager();
            membershipManager.createMembershipForUser(redmineProject.getId(), adminUser.getId(), userManager.getRoles());
        }
    }

    private User findUser(String username) throws RedmineException, RedmineExtensionException {
        User cachedUser = RedmineUserCache.INSTANCE.get(username);
        if (cachedUser != null) {
            LOGGER.info("[redmine] cached user '{}' found", username);
            return cachedUser;
        }

        final UserManager userManager = this.getAdminRedmineManager().getUserManager();
        for (final User user : userManager.getUsers()) {
            RedmineUserCache.INSTANCE.put(user.getLogin(), user);
            if (StringUtils.equalsIgnoreCase(user.getLogin(), username)) {
                LOGGER.info("[redmine] user '{}' found", username);
                cachedUser = user;
            }
        }
        if (cachedUser == null) {
            LOGGER.info("[redmine] user '{}' not found", username);
        }
        return cachedUser;
    }

    private User findUserById(Integer id) throws RedmineException, RedmineExtensionException {
        User cachedUser = RedmineUserCache.INSTANCE.getUserById(id);
        if (cachedUser != null) {
            LOGGER.info("[redmine] cached user '{}' found", id);
            return cachedUser;
        }

        final UserManager userManager = this.getAdminRedmineManager().getUserManager();
        for (final User user : userManager.getUsers()) {
            RedmineUserCache.INSTANCE.put(user.getLogin(), user);
            if (id.equals(user.getId())) {
                LOGGER.info("[redmine] user '{}' found", id);
                cachedUser = user;
            }
        }
        if (cachedUser == null) {
            LOGGER.info("[redmine] user '{}' not found", id);
        }
        return cachedUser;
    }

    @Override
    public void deleteProject(final ProjectDto ksfProject) throws RedmineExtensionException {
        
    	Objects.requireNonNull(ksfProject);

    	final String projectKey = this.nameResolver.getProjectKey(ksfProject);
        final ProjectManager projectManager = getAdminRedmineManager().getProjectManager();
        
        try {
            projectManager.deleteProject(projectKey);
        } catch (final RedmineException e) {
            throw new RedmineExtensionException("Failed to delete projet " + projectKey, e);
        }

    }

    private List<RedmineIssue> queryIssues(final RedmineQuery query, final RedmineManager manager) throws RedmineExtensionException {

        Objects.requireNonNull(query);

        List<Issue> issues;
        final List<RedmineIssue> redmineIssues;
        final String projectIdentifier = query.getProjectKey();
        final int resultItemsLimit = query.getResultItemsLimit();
        final String requestTargetVersion = query.getTargetVersion();
        final int resultNumberOfItems;
        final RedmineManager redmineManager = manager == null ? getUserRedmineManager() : manager;
        try {
            final IssueManager issueManager = redmineManager.getIssueManager();

            if (projectIdentifier == null) {
                throw new RedmineExtensionException("Cannot find project " + projectIdentifier);
            }

            issues = issueManager.getIssues(projectIdentifier, null);

            if (resultItemsLimit > 0) {
                resultNumberOfItems = Math.min(issues.size(), resultItemsLimit);
            } else {
                resultNumberOfItems = issues.size();
            }
            redmineIssues = new ArrayList<>(resultNumberOfItems);

        } catch (final RedmineException e) {
            throw new RedmineExtensionException("Failed to list the issues of " + projectIdentifier, e);
        }

        for (int i = 0; i < resultNumberOfItems; i++) {
            final Issue issue = issues.get(i);

            if (requestTargetVersion != null) {
                final Version issueTargetVersion = issue.getTargetVersion();
                if (issueTargetVersion != null && !StringUtils.equals(requestTargetVersion, issueTargetVersion.getName())) {
                    continue;
                }
            }

            if (!query.getStatusIds().isEmpty() && !query.getStatusIds().contains(issue.getStatusId())) {
                continue;
            }

            if (!query.getTrackerIds().isEmpty() && issue.getTracker() != null && !query.getTrackerIds().contains(issue.getTracker().getId())) {
                continue;
            }

            redmineIssues.add(createRedmineIssue(issue));
        }

        return redmineIssues;
    }

    @Override
    public List<RedmineIssue> queryIssues(RedmineQuery query) throws RedmineExtensionException {
        return queryIssues(query, null);
    }

    private RedmineIssue createRedmineIssue(final Issue issue) throws RedmineExtensionException {
        final RedmineIssue redmineIssue = new RedmineIssue(issue.getId());

        User assignee = issue.getAssignee();
        if (assignee != null && assignee.getLogin() == null) {
            final Integer assigneeId = assignee.getId();
            try {
                assignee = this.findUserById(assigneeId);
            } catch (final RedmineException e) {
                throw new RedmineExtensionException("Failed to find redmine user with id " + assigneeId, e);
            }
        }
        final String assigneeLogin = assignee == null ? StringUtils.EMPTY : StringUtils.defaultString(assignee.getLogin());
        redmineIssue.setAssignee(assigneeLogin);

        final IssueCategory category = issue.getCategory();
        final String categoryName = category == null ? StringUtils.EMPTY : StringUtils.defaultString(category.getName());

        redmineIssue.setCategory(categoryName);

        redmineIssue.setSubject(StringUtils.defaultString(issue.getSubject()));

        redmineIssue.setPriority(StringUtils.defaultString(issue.getPriorityText()));
        redmineIssue.setStatus(StringUtils.defaultString(issue.getStatusName()));
        redmineIssue.setStatusId(issue.getStatusId());

        final Version targetVersion = issue.getTargetVersion();
        final String targetVersionName = targetVersion == null ? StringUtils.EMPTY : StringUtils.defaultString(targetVersion.getName());
        redmineIssue.setTargetVersion(targetVersionName);

        final Tracker tracker = issue.getTracker();
        final String trackerName = tracker == null ? StringUtils.EMPTY : StringUtils.defaultString(tracker.getName());

        redmineIssue.setTracker(trackerName);
        return redmineIssue;
    }

    @Override
    public List<IProjectVersion> getVersions(final ProjectDto ksfProject) throws RedmineExtensionException {
        
    	Objects.requireNonNull(ksfProject);

        final List<IProjectVersion> result;
        final ProjectManager projectManager = getUserRedmineManager().getProjectManager();
        final String projectKey = this.nameResolver.getProjectKey(ksfProject);
        final String projectName = ksfProject.getName();
        
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
            throw new RedmineExtensionException("Failed to retrieve project \"" + projectName + "\" versions", e);
        }
        return result;
    }

    @Override
    public List<IProjectFeature> getFeatures(final ProjectDto ksfProject) throws RedmineExtensionException {
        
    	Objects.requireNonNull(ksfProject);
        final RedmineManager redmineManager = getUserRedmineManager();
        final List<IProjectFeature> features = buildFeaturesList(ksfProject, this.configuration.getFeatureIds(), redmineManager);

        return features;
    }

    private List<IProjectFeature> buildFeaturesList(final ProjectDto ksfProject, final List<Integer> trackerIds, final RedmineManager redmineManager) throws RedmineExtensionException {
        
    	final Builder redmineQuerryBuilder = new RedmineQuery.Builder();

    	final String projectKey = this.nameResolver.getProjectKey(ksfProject);
        final Builder builder = redmineQuerryBuilder.projectKey(projectKey);

        for (final Integer trackerId : trackerIds) {
            builder.addTrackerId(trackerId);
        }

        builder.addStatusId(this.configuration.getFeatureStatusNewId());
        builder.addStatusId(this.configuration.getFeatureStatusAssignedId());

        final RedmineQuery query = redmineQuerryBuilder.build();
        final List<RedmineIssue> issues = queryIssues(query, redmineManager);

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
    public void rejectIssue(String ticketId, String username) throws RedmineExtensionException {

        LOGGER.info("[redmine] Rejecting redmine issue '{}'", ticketId);
        final RedmineManager redmineManager = getUserRedmineManager();
        final IssueManager issueManager = redmineManager.getIssueManager();
        try {

            final Issue issue = this.getIssueById(Integer.valueOf(ticketId), issueManager);
            this.changeIssueAssignee(issue, username);
            issue.setStatusId(this.configuration.getFeatureStatusClosedId());
            final int resolutionFieldId = this.configuration.getResolutionFieldId();
            CustomField fieldResolution = issue.getCustomFieldById(resolutionFieldId);
            if (fieldResolution == null) {
                fieldResolution = CustomFieldFactory.create(resolutionFieldId);
                issue.addCustomField(fieldResolution);
            }
            fieldResolution.setValue(this.configuration.getFeatureResolutionRejectedValue());
            issueManager.update(issue);

            LOGGER.info("[redmine] Rejecting redmine issue - issue rejected");
        } catch (final RedmineExtensionException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (final Exception e) {
            LOGGER.error("[redmine] Failed to reject issue '" + ticketId + "'", e);
            throw new RedmineExtensionException("Failed to reject ticket.", e);
        }
    }

    @Override
    public void changeStatus(String ticketId, int statusId, String username) throws RedmineExtensionException {

        LOGGER.info("[redmine] Changing redmine issue '{}' status to status ID '{}'", ticketId, statusId);
        final RedmineManager redmineManager = getUserRedmineManager();
        final IssueManager issueManager = redmineManager.getIssueManager();
        try {
            final Issue issue = this.getIssueById(Integer.valueOf(ticketId), issueManager);
            issue.setStatusId(statusId);
            this.changeIssueAssignee(issue, username);
            issueManager.update(issue);
            LOGGER.info("[redmine] Changing redmine issue status - status updated");
        } catch (final RedmineExtensionException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (final Exception e) {
            LOGGER.error("[redmine] Failed to change issue '" + ticketId + "' status", e);
            throw new RedmineExtensionException("Failed to change ticket status.", e);
        }
    }

    private void changeIssueAssignee(final Issue issue, final String username)
            throws RedmineException, RedmineExtensionException {
        if (username != null) {
            final User user = findUser(username);
            if (user != null) {
                LOGGER.info("[redmine] Changing issue assignee to '{}'", username);
                issue.setAssignee(user);
            }
        }
    }

    private Issue getIssueById(final Integer issueId, final IssueManager issueManager) throws RedmineException, RedmineExtensionException {
        Issue issue = null;
        LOGGER.info("[redmine] property bug API is {}", this.configuration.isHackBugApi());
        if (this.configuration.isHackBugApi()) {
            final List<Issue> issues = issueManager.getIssues(null, null);
            if (issues != null) {
                for (final Issue iss : issues) {
                    if (iss.getId().equals(issueId)) {
                        issue = iss;
                        break;
                    }
                }
            }
        } else {
            issue = issueManager.getIssueById(issueId);
        }
        if (issue == null) {
            throw new RedmineExtensionException("[redmine] Issue '" + issueId + "' was not found.");
        }
        return issue;
    }

    @Override
    public String getProjectId(final ProjectDto ksfProject) {

        Objects.requireNonNull(ksfProject);

        return this.nameResolver.getProjectKey(ksfProject);
    }

    @Override
    public void createTicket(final ProjectDto foundationProject, final String releaseVersion, final String subject, final String username, final Integer trackerId) throws RedmineExtensionException {

        Objects.requireNonNull(foundationProject);
        Objects.requireNonNull(releaseVersion);

        try {
            final String projectIdentifier = this.nameResolver.getProjectKey(foundationProject);
            final RedmineManager redmineManager = getUserRedmineManager();
            final ProjectManager projectManager = redmineManager.getProjectManager();
            final IssueManager issueManager = redmineManager.getIssueManager();

            final Project redmineProject = projectManager.getProjectByKey(projectIdentifier);
            final Version version = this.findVersion(redmineProject, releaseVersion, redmineManager);

            final Issue issue = new Issue();
            issue.setProject(redmineProject);
            issue.setTargetVersion(version);
            issue.setSubject(subject);

            if (trackerId != null) {
                final Tracker tracker = findTracker(issueManager, trackerId);
                issue.setTracker(tracker);
            }

            this.changeIssueAssignee(issue, username);

            issueManager.createIssue(issue);

        } catch (final RedmineException e) {
            throw new RedmineExtensionException("Failed to create ticket", e);
        }
    }

    private Tracker findTracker(IssueManager issueManager, Integer trackerId) throws RedmineException {

        if (issueManager == null || trackerId == null) {
            return null;
        }

        final List<Tracker> trackers = issueManager.getTrackers();

        for (final Tracker tracker : trackers) {
            if (trackerId.equals(tracker.getId())) {
                return tracker;
            }
        }

        return null;
    }

    private Version findVersion(Project redmineProject, String releaseVersion, RedmineManager redmineManager) throws RedmineException {
        final List<Version> versions = redmineManager.getProjectManager().getVersions(redmineProject.getId());
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

        try {
            final String projectIdentifier = this.nameResolver.getProjectKey(foundationProject);
            final RedmineManager redmineManager = getUserRedmineManager();
            final ProjectManager projectManager = redmineManager.getProjectManager();

            final Project redmineProject = projectManager.getProjectByKey(projectIdentifier);
            final Version version = findVersion(redmineProject, releaseVersion, redmineManager);
            version.setStatus(status);
            projectManager.update(version);

        } catch (final RedmineException e) {
            throw new RedmineExtensionException("Failed to change version state", e);
        }

    }

}
