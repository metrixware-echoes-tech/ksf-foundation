package com.tocea.corolla.ui.views.projects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.portfolio.dao.IPortfolioDAO;
import com.tocea.corolla.portfolio.domain.Portfolio;
import com.tocea.corolla.portfolio.predicates.FindNodeByProjectIDPredicate;
import com.tocea.corolla.products.commands.CreateProjectBranchCommand;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.commands.CreateProjectStatusCommand;
import com.tocea.corolla.products.commands.EditProjectCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.domain.ProjectStatus;
import com.tocea.corolla.revisions.domain.ICommit;
import com.tocea.corolla.revisions.services.IRevisionService;
import com.tocea.corolla.tests.utils.AuthUserUtils;
import com.tocea.corolla.trees.services.ITreeManagementService;
import com.tocea.corolla.ui.AbstractSpringTest;

public class ProjectDetailsPageControllerTest extends AbstractSpringTest {

	private static final String PROJECTS_URL 			= "/ui/projects/";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	 
	private MockMvc mvc;
	
	@Autowired
	private Gate gate;
	
	@Autowired
	private IRevisionService revisionService;
	
	@Autowired
	private IProjectDAO projectDAO;
	
	@Autowired
	private IProjectBranchDAO branchDAO;
	
	@Autowired
	private ITreeManagementService treeManagementService;
	
	@Autowired
	private IPortfolioDAO portfolioDAO;
	
	private ProjectStatus projectStatus;
	private Project existingProject;
	private ProjectBranch existingBranch;
	
	@Before
	public void setUp() {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		
		projectStatus = new ProjectStatus();
		projectStatus.setName("Active");
		projectStatus.setClosed(false);
		
		gate.dispatch(new CreateProjectStatusCommand(projectStatus));
		
		existingProject = new Project();
		existingProject.setKey("COROLLA_TEST");
		existingProject.setName("Corolla");
		existingProject.setStatusId(projectStatus.getId());
		
		gate.dispatch(new CreateProjectCommand(existingProject));
		
		existingBranch = branchDAO.findDefaultBranch(existingProject.getId());
		
	}
	
	private String buildProjectURL(Project project) {
		
		return 
				new StringBuilder(PROJECTS_URL)
				.append(project.getKey())
				.toString();
		
	}
	
	private String buildProjectEditURL(Project project) {
		
		return
				new StringBuilder(PROJECTS_URL)
				.append(project.getId())
				.append("/edit")
				.toString();
	}
	
	private String buildRevisionPageURL(Project project, String commitID) {
		
		return 
				new StringBuilder(PROJECTS_URL)
				.append(project.getKey())
				.append("/revisions/")
				.append(commitID)
				.toString();
	}
	
	private String buildRestoreStateURL(Project project, String commitID) {
		
		return
				new StringBuilder(buildRevisionPageURL(project, commitID))
				.append("/restore")
				.toString();
		
	}
	
	private String buildCreateBranchURL(Project project, ProjectBranch origin) {
		
		return
				new StringBuilder(buildProjectURL(project))
				.append("/branches/add/")
				.append(origin.getName())
				.toString();
	}
	
	private String buildEditBranchURL(Project project, ProjectBranch branch) {
		
		return
				new StringBuilder(buildProjectURL(project))
				.append("/branches/edit/")
				.append(branch.getName())
				.toString();
	}
	
	private String buildDeleteProjectURL(Project project) {
		
		return
				new StringBuilder(buildProjectURL(project))
				.append("/delete")
				.toString();
	}
	
	@Test
	public void basicUserShouldAccessProjectDetailsView() throws Exception {
		
		mvc
		.perform(get(buildProjectURL(existingProject)).with(user(AuthUserUtils.basicUser())))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessProjectDetailsView() throws Exception {
		
		mvc.perform(get(buildProjectURL(existingProject)))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void shouldGetPageNotFoundErrorIfInvalidProjectKey() throws Exception {
		
		Project invalidProject = new Project();
		invalidProject.setKey("not_existing_key");
		
		mvc
		.perform(get(buildProjectURL(invalidProject)).with(user(AuthUserUtils.basicUser())))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void managerUserShouldEditProjectData() throws Exception {
		
		String newKey = "key_blblbl";
		String newName = "name blblbl";
		
		mvc
		.perform(
				post(buildProjectEditURL(existingProject))
				.param("id", existingProject.getId())
				.param("key", newKey)
				.param("name", newName)
				.with(user(AuthUserUtils.projectManager())))
		.andExpect(status().isFound());
		
		existingProject = projectDAO.findOne(existingProject.getId());
		
		assertEquals(newKey, existingProject.getKey());
		assertEquals(newName, existingProject.getName());
		
	}
	
	@Test
	public void anonymousUserShouldNotEditProjectData() throws Exception {
		
		String newKey = "key_blblbl";
		String newName = "name blblbl";
		
		mvc
		.perform(
				post(buildProjectEditURL(existingProject))
				.param("id", existingProject.getId())
				.param("key", newKey)
				.param("name", newName)
		).andExpect(redirectedUrlPattern("**/login"));
		
		Project project = projectDAO.findOne(existingProject.getId());
		
		assertEquals(existingProject.getKey(), project.getKey());
		assertEquals(existingProject.getName(), project.getName());
		
	}
	
	@Test
	public void shouldRedirectToEditFormIfDataIsInvalid() throws Exception {
		
		String newKey = "";
		String newName = "name blblbl";
		
		mvc
		.perform(
				post(buildProjectEditURL(existingProject))
				.param("id", existingProject.getId())
				.param("key", newKey)
				.param("name", newName)
				.param("statusId", projectStatus.getId())
				.with(user(AuthUserUtils.projectManager())))
		.andExpect(status().isOk());
		
		Project project = projectDAO.findOne(existingProject.getId());
		
		assertEquals(existingProject.getKey(), project.getKey());
		assertEquals(existingProject.getName(), project.getName());
		
	}
	
	@Test
	public void basicUserShouldAccessRequirementRevisionPage() throws Exception {
		
		Collection<ICommit> commits = revisionService.getHistory(existingProject.getId(), existingProject.getClass());
		
		String commitID = commits.iterator().next().getId();
		
		String URL = buildRevisionPageURL(existingProject, commitID);
		
		mvc
			.perform(
					get(URL).with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void shouldNotAccessRequirementRevisionPageWithInvalidProjectKey() throws Exception {
		
		Collection<ICommit> commits = revisionService.getHistory(existingProject.getId(), existingProject.getClass());
		
		String commitID = commits.iterator().next().getId();
		
		Project invalidProject = new Project();
		invalidProject.setKey("blblbl");
		
		String URL = buildRevisionPageURL(invalidProject, commitID);
		
		mvc
			.perform(
					get(URL).with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void shouldNotAccessRequirementRevisionPageWithInvalidCommitID() throws Exception {				
		
		String URL = buildRevisionPageURL(existingProject, "blblbl");
		
		mvc
			.perform(
					get(URL).with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void basicUserShouldCreateNewBranch() throws Exception {
		
		String newName = "TestBranch";

		mvc
		.perform(
				post(buildCreateBranchURL(existingProject, existingBranch))
				.param("name", newName)
				.with(user(AuthUserUtils.basicUser())))
		.andExpect(status().isFound());
		
		ProjectBranch newBranch = branchDAO.findByNameAndProjectId(newName, existingProject.getId());
		
		assertNotNull(newBranch);
	}
	
	@Test
	public void shouldNotCreateBranchWithAlreadyExistingName() throws Exception {
		
		long nbBranches = branchDAO.count();
		
		mvc
			.perform(
				post(buildCreateBranchURL(existingProject, existingBranch))
				.param("name", existingBranch.getName())
				.with(user(AuthUserUtils.basicUser())))
			.andExpect(status().isOk());
		
		assertEquals(nbBranches, branchDAO.count());
		
	}
	
	@Test
	public void basicUserShouldEditBranch() throws Exception {
		
		String newName = "EditedName";

		mvc
		.perform(
				post(buildEditBranchURL(existingProject, existingBranch))
				.param("id", existingBranch.getId())
				.param("name", newName)
				.param("projectId", existingBranch.getProjectId())
				.with(user(AuthUserUtils.basicUser())))
		.andExpect(status().isFound());
		
		existingBranch = branchDAO.findOne(existingBranch.getId());
		assertEquals(newName, existingBranch.getName());

	}
	
	@Test
	public void shouldNotEditBranchWithNameOfAnotherBranch() throws Exception {
		
		ProjectBranch alreadyExistingBranch = gate.dispatch(new CreateProjectBranchCommand("Dev", existingProject));

		mvc
		.perform(
				post(buildEditBranchURL(existingProject, existingBranch))
				.param("id", existingBranch.getId())
				.param("name", alreadyExistingBranch.getName())
				.param("projectId", existingBranch.getProjectId())
				.with(user(AuthUserUtils.basicUser())))
		.andExpect(status().isOk());
		
		existingBranch = branchDAO.findOne(existingBranch.getId());
		assertNotEquals(alreadyExistingBranch.getName(), existingBranch.getName());

	}
	
	@Test
	public void basicUserShouldNotDeleteProject() throws Exception {
		
		mvc
			.perform(
				get(buildDeleteProjectURL(existingProject)).
				with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isForbidden());
		
		assertNotNull(projectDAO.findOne(existingProject.getId()));
		
	}
	
	@Test
	public void managerUserShouldDeleteProject() throws Exception {
		
		mvc
			.perform(
				get(buildDeleteProjectURL(existingProject)).
				with(user(AuthUserUtils.portfolioManager()))
			)
			.andExpect(status().isFound());

		String deletedID = existingProject.getId();
		Portfolio portfolio = portfolioDAO.find();
		
		// The project should have been deleted
		assertNull(projectDAO.findOne(deletedID));
		// The project branches should have been deleted
		assertEquals(0, branchDAO.findByProjectId(deletedID).size());
		// The project node in the portfolio should have been deleted
		assertNull(treeManagementService.findNode(portfolio, new FindNodeByProjectIDPredicate(deletedID)));

	}
	
	@Test
	public void managerUserShouldRestoreProjectToPreviousState() throws Exception {
		
		String oldName = existingProject.getName();
		String newName = "newName";
		
		existingProject.setName(newName);
		gate.dispatch(new EditProjectCommand(existingProject));		
		assertEquals(newName, projectDAO.findOne(existingProject.getId()).getName());
				
		Collection<ICommit> commits = revisionService.getHistory(existingProject.getId(), existingProject.getClass());
		
		Iterator<ICommit> it = commits.iterator();
		it.next();		
		String commitID = it.next().getId();
		
		String URL = buildRestoreStateURL(existingProject, commitID);
		
		mvc
			.perform(get(URL).with(user(AuthUserUtils.projectManager())))
			.andExpect(status().isFound());
		
		assertEquals(oldName, projectDAO.findOne(existingProject.getId()).getName());
		
	}
	
	@Test
	public void basicUserShouldNotRestoreProjectToPreviousState() throws Exception {
		
		String newName = "newName";
		
		existingProject.setName(newName);
		gate.dispatch(new EditProjectCommand(existingProject));
		
		Collection<ICommit> commits = revisionService.getHistory(existingProject.getId(), existingProject.getClass());
		
		Iterator<ICommit> it = commits.iterator();
		it.next();		
		String commitID = it.next().getId();
		
		String URL = buildRestoreStateURL(existingProject, commitID);
		
		mvc
			.perform(get(URL).with(user(AuthUserUtils.basicUser())))
			.andExpect(status().isForbidden());
		
		assertEquals(newName, projectDAO.findOne(existingProject.getId()).getName());
		
	}
	
}