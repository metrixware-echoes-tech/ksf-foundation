package com.tocea.corolla.ui.views.requirements;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.CreateProjectBranchCommand;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.requirements.commands.CreateRequirementCommand;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.revisions.domain.ICommit;
import com.tocea.corolla.revisions.services.IRevisionService;
import com.tocea.corolla.ui.AbstractSpringTest;
import com.tocea.corolla.ui.security.AuthUser;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;

public class RequirementPageControllerTest extends AbstractSpringTest {

	private static final String REQUIREMENT_PAGE = "/ui/requirements/";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	 
	private MockMvc mvc;
	
	@Autowired
	private Gate gate;
	
	@Autowired
	private IRevisionService revisionService;
	
	private Role basicRole;	
	private User basicUser;
	
	private Project existingProject;
	private ProjectBranch existingBranch;
	private Requirement existingRequirement;
	
	@Before
	public void setUp() {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		
		basicRole = new Role();
		basicRole.setName("BASIC");
		basicRole.setPermissions("");
		
		basicUser = new User();
		basicUser.setLogin("simple");
		basicUser.setPassword("pass");
		basicUser.setEmail("simple@corolla.com");
		
		existingProject = new Project();
		existingProject.setKey("COROLLA_TEST");
		existingProject.setName("Corolla");
		
		gate.dispatch(new CreateProjectCommand(existingProject));
		
		existingBranch = new ProjectBranch();
		existingBranch.setName("test");
		existingBranch.setProjectId(existingProject.getId());
		
		gate.dispatch(new CreateProjectBranchCommand(existingBranch));
		
		existingRequirement = new Requirement();
		existingRequirement.setKey("TEST_REQUIREMENT_PAGE");
		existingRequirement.setName("Test requirement page");
		existingRequirement.setProjectBranchId(existingBranch.getId());
		
		gate.dispatch(new CreateRequirementCommand(existingRequirement));
		
	}
	
	private String buildRevisionPageURL(String projectKey, String branchName, String requirementKey, String commitID) {
		
		return 
				new StringBuffer(REQUIREMENT_PAGE)
					.append(projectKey)
					.append("/").append(branchName)
					.append("/").append(requirementKey)
					.append("/revisions/").append(commitID)
					.toString();
		
	}
	
	
	@Test
	public void basicUserShouldAccessRequirementsPage() throws Exception {
		
		mvc
			.perform(
					get(REQUIREMENT_PAGE+existingProject.getKey()).
					with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void shouldNotAccessRequirementsPageWithInvalidProjectKey() throws Exception {
		
		mvc
			.perform(
					get(REQUIREMENT_PAGE+"bblbl").
					with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessRequirementsPage() throws Exception {
			
		mvc
			.perform(get(REQUIREMENT_PAGE+existingProject.getKey()))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void basicUserShouldAccessRequirementsPageWithBranch() throws Exception {
		
		mvc
			.perform(
					get(REQUIREMENT_PAGE+existingProject.getKey()+"/"+existingBranch.getName()).
					with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void shouldNotAccessRequirementsPageWithInvalidBranchName() throws Exception {
		
		mvc
			.perform(
					get(REQUIREMENT_PAGE+existingProject.getKey()+"/blblblb").
					with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessRequirementsPageWithBranch() throws Exception {
		
		mvc
			.perform(
					get(REQUIREMENT_PAGE+existingProject.getKey()+"/"+existingBranch.getName())
			)
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void basicUserShouldAccessRequirementsPageWithBranchAndRequirementKey() throws Exception {
		
		StringBuffer url = new StringBuffer(REQUIREMENT_PAGE)
								.append(existingProject.getKey())
								.append("/")
								.append(existingBranch.getName())
								.append("/")
								.append(existingRequirement.getKey());
		
		mvc
			.perform(
					get(url.toString()).with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessRequirementsPageWithBranchAndRequirementKey() throws Exception {
		
		StringBuffer url = new StringBuffer(REQUIREMENT_PAGE)
								.append(existingProject.getKey())
								.append("/")
								.append(existingBranch.getName())
								.append("/")
								.append(existingRequirement.getKey());
		
		mvc
			.perform(get(url.toString()))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessRequirementRevisionPage() throws Exception {
		
		Collection<ICommit> commits = revisionService.getHistory(existingRequirement.getId(), existingRequirement.getClass());
		
		String commitID = commits.iterator().next().getId();
		String URL = buildRevisionPageURL(existingProject.getKey(), existingBranch.getName(), existingRequirement.getKey(), commitID);

		mvc
			.perform(get(URL))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void basicUserShouldAccessRequirementRevisionPage() throws Exception {
		
		Collection<ICommit> commits = revisionService.getHistory(existingRequirement.getId(), existingRequirement.getClass());
		
		String commitID = commits.iterator().next().getId();
		String URL = buildRevisionPageURL(existingProject.getKey(), existingBranch.getName(), existingRequirement.getKey(), commitID);

		mvc
			.perform(
					get(URL).with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void shouldNotAccessRequirementRevisionPageWithInvalidCommitID() throws Exception {
		
		String URL = buildRevisionPageURL(existingProject.getKey(), existingBranch.getName(), existingRequirement.getKey(), "blblbl");

		mvc
			.perform(
					get(URL).with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void shouldNotAccessRequirementRevisionPageWithInvalidRequirementKey() throws Exception {
		
		Collection<ICommit> commits = revisionService.getHistory(existingRequirement.getId(), existingRequirement.getClass());
		
		String commitID = commits.iterator().next().getId();
		String URL = buildRevisionPageURL(existingProject.getKey(), existingBranch.getName(), "blblbl", commitID);		

		mvc
			.perform(
					get(URL).with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void shouldNotAccessRequirementRevisionPageWithInvalidBranchName() throws Exception {
		
		Collection<ICommit> commits = revisionService.getHistory(existingRequirement.getId(), existingRequirement.getClass());
		
		String commitID = commits.iterator().next().getId();
		String URL = buildRevisionPageURL(existingProject.getKey(), "blblbl", existingRequirement.getKey(), commitID);		

		mvc
			.perform(
					get(URL).with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void shouldNotAccessRequirementRevisionPageWithInvalidProjectKey() throws Exception {
		
		Collection<ICommit> commits = revisionService.getHistory(existingRequirement.getId(), existingRequirement.getClass());
		
		String commitID = commits.iterator().next().getId();
		String URL = buildRevisionPageURL("blblbl", existingBranch.getName(), existingRequirement.getKey(), commitID);		

		mvc
			.perform(
					get(URL).with(user(new AuthUser(basicUser, basicRole)))
			)
			.andExpect(status().isNotFound());
		
	}
	
}