package fr.echoes.labs.ksf.cc.plugins.redmine.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Tracker;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Version;

import fr.echoes.labs.ksf.cc.plugins.redmine.RedmineExtensionException;
import fr.echoes.labs.ksf.extensions.projects.ProjectDto;
import fr.echoes.labs.ksf.users.security.api.CurrentUserService;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("org.apache.http.conn.ssl.*")
@PrepareForTest(RedmineManagerFactory.class)
public class RedmineServiceTest {
	
	private static final String REDMINE_URL = "http://localhost/redmine";
	private static final String REDMINE_API_KEY = "api-key";
	private static final String REDMINE_ADMIN_USER = "ksfuser";
	
	@Mock
	private RedmineConfigurationService configuration;
	
	@Mock
	private CurrentUserService currentUserService;
	
	@Mock
	private RedmineManager redmineManager;
	
	@Mock
	private ProjectManager projectManager;
	
	@Mock
	private UserManager userManager;
	
	@Mock
	private IssueManager issueManager;
	
	@Captor
	private ArgumentCaptor<Project> projectCaptor;
	
	@Captor
	private ArgumentCaptor<Issue> issueCaptor;
	
	private RedmineNameResolver nameResolver;
	
	private RedmineService redmineService;
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
		this.nameResolver = new RedmineNameResolver();
		this.redmineService = new RedmineService(configuration, nameResolver, currentUserService);
		
		Mockito.when(this.configuration.getUrl()).thenReturn(REDMINE_URL);
		Mockito.when(this.configuration.getApiAccessKey()).thenReturn(REDMINE_API_KEY);
		Mockito.when(this.configuration.getAdminUserName()).thenReturn(REDMINE_ADMIN_USER);
		
		Mockito.when(this.redmineManager.getProjectManager()).thenReturn(this.projectManager);
		Mockito.when(this.redmineManager.getUserManager()).thenReturn(this.userManager);
		Mockito.when(this.redmineManager.getIssueManager()).thenReturn(this.issueManager);
		
		Mockito.when(this.currentUserService.getCurrentUserLogin()).thenReturn(REDMINE_ADMIN_USER);
		
		PowerMockito.mockStatic(RedmineManagerFactory.class);
		PowerMockito.when(RedmineManagerFactory.createWithApiKey(Matchers.eq(REDMINE_URL), Matchers.anyString())).thenReturn(this.redmineManager);

	}

	@Test
	public void testCreateProject() throws RedmineExtensionException, RedmineException {
		
		// given
		final String username = "jsnow";
		
		// and
		final ProjectDto project = new ProjectDto();
		project.setName("my project");
		
		// mock
		final Project createdProject = Mockito.mock(Project.class);
		Mockito.when(this.projectManager.createProject(Matchers.any(Project.class))).thenReturn(createdProject);
		
		// when
		this.redmineService.createProject(project, username);
		
		// then
		Mockito.verify(this.projectManager).createProject(this.projectCaptor.capture());
		
		// then
		final Project redmineProject = this.projectCaptor.getValue();
		Assert.assertEquals("my_project", redmineProject.getIdentifier());
		Assert.assertEquals(project.getName(), redmineProject.getName());
		
	}
	
	@Test
	public void testDeleteProject() throws RedmineExtensionException, RedmineException {
		
		// given
		final ProjectDto project = new ProjectDto();
		project.setName("my project");
		
		// when
		this.redmineService.deleteProject(project);
		
		// then
		Mockito.verify(this.projectManager).deleteProject("my_project");
		
	}
	
	@Test
	public void testCreateTicket() throws RedmineExtensionException, RedmineException {
		
		// given
		final ProjectDto project = new ProjectDto();
		project.setName("my project");
		
		// and
		final String projectKey = "my_project";
		final String releaseVersion = "4.1";
		final String subject = "Write unit tests";
		final String username = "ksfuser";
		final Integer trackerId = 35;
		
		// and
		final Project redmineProject = Mockito.mock(Project.class);
		final Integer redmineProjectId = 98;
		final Version version = Mockito.mock(Version.class);
		final List<Version> versions = Lists.newArrayList(
				Mockito.mock(Version.class),
				version,
				Mockito.mock(Version.class)
		);
		final List<Tracker> trackers = Lists.newArrayList(Mockito.mock(Tracker.class));
		
		// and
		final List<User> users = Lists.newArrayList(mockUser(1, username, "userApiKey"));
		
		// mock
		Mockito.when(this.projectManager.getProjectByKey(projectKey)).thenReturn(redmineProject);
		Mockito.when(redmineProject.getId()).thenReturn(redmineProjectId);
		Mockito.when(this.projectManager.getVersions(redmineProjectId)).thenReturn(versions);
		Mockito.when(version.getName()).thenReturn(releaseVersion);
		Mockito.when(this.issueManager.getTrackers()).thenReturn(trackers);
		Mockito.when(trackers.get(0).getId()).thenReturn(trackerId);
		Mockito.when(this.userManager.getUsers(Matchers.anyMapOf(String.class, String.class))).thenReturn(users);
		
		// when
		this.redmineService.createTicket(project, releaseVersion, subject, username, trackerId);
		
		// then
		Mockito.verify(this.issueManager).createIssue(this.issueCaptor.capture());
		
		// then
		final Issue issue = this.issueCaptor.getValue();
		Assert.assertNotNull(issue);
		Assert.assertEquals(redmineProject, issue.getProject());
		Assert.assertEquals(version, issue.getTargetVersion());
		Assert.assertEquals(subject, issue.getSubject());
		Assert.assertEquals(trackers.get(0), issue.getTracker());
		
	}
	
	@Test
	public void testIsAdmin() throws RedmineExtensionException, RedmineException {
		
		// given
		final String username = "ksfuser";
		final List<User> users = Lists.newArrayList(mockUser(1, username, "userApiKey"));
		
		// mock
		Mockito.when(this.userManager.getUsers(Matchers.anyMapOf(String.class, String.class))).thenReturn(users);
		Mockito.when(this.userManager.getCurrentUser()).thenReturn(users.get(0));
		
		//when
		final Boolean isAdmin = this.redmineService.isAdmin();
		
		// then
		Assert.assertTrue(isAdmin);		
	}
	
	@Test
	public void testIsNotAdmin() throws RedmineException, RedmineExtensionException {
		
		// given
		final String username = "ksfuser";
		final User user = mockUser(1, username, "userApiKey");
		user.setStatus(null);
		final List<User> users = Lists.newArrayList(mockUser(1, username, "userApiKey"));
		
		// mock
		Mockito.when(this.userManager.getUsers(Matchers.anyMapOf(String.class, String.class))).thenReturn(users);
		Mockito.when(this.userManager.getCurrentUser()).thenReturn(users.get(0));
		
		//when
		final Boolean isAdmin = this.redmineService.isAdmin();
		
		// then
		Assert.assertTrue(isAdmin);	
	}
	
	private User mockUser(final Integer id, final String login, final String apiKey) throws RedmineException {
		
		final User user = Mockito.mock(User.class);
		
		Mockito.when(user.getId()).thenReturn(id);
		Mockito.when(user.getLogin()).thenReturn(login);
		Mockito.when(user.getApiKey()).thenReturn(apiKey);
		
		Mockito.when(this.userManager.getUserById(id)).thenReturn(user);
		
		return user;
	}
	
}
