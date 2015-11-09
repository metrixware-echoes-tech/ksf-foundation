package com.tocea.corolla.ui.views.portfolio;

import fr.echoes.lab.ksf.users.security.auth.AuthUser;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.portfolio.dao.IPortfolioDAO;
import com.tocea.corolla.portfolio.domain.Portfolio;
import com.tocea.corolla.portfolio.domain.ProjectNode;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.commands.CreateProjectStatusCommand;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectStatus;
import com.tocea.corolla.revisions.services.IRevisionService;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.predicates.FindNodeByIDPredicate;
import com.tocea.corolla.trees.services.ITreeManagementService;
import com.tocea.corolla.ui.AbstractSpringTest;
import com.tocea.corolla.users.domain.Permission;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;
import javax.servlet.Filter;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class PortfolioPageControllerTest extends AbstractSpringTest {

	private static final String PORTFOLIO_URL 			= "/ui/portfolio";
	private static final String PORTFOLIO_MANAGER_URL 	= PORTFOLIO_URL+"/manager/";
	private static final String CREATE_PROJECT_URL		= PORTFOLIO_MANAGER_URL+"create";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	 
	private MockMvc mvc;
	
	@Autowired
	private Gate gate;
	
	@Autowired
	private IProjectDAO projectDAO;
	
	@Autowired
	private IPortfolioDAO portfolioDAO;
	
	@Autowired
	private ITreeManagementService treeManagementService;
	
	@Autowired
	private IRevisionService revisionService;
	
	private Role basicRole;	
	private Role managerRole;
	
	private User basicUser;
	private User managerUser;
	
	private ProjectStatus projectStatus;
	private Project existingProject;
	
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
		
		managerRole = new Role();
		managerRole.setName("BASIC");
		managerRole.setPermissions(Permission.PORTFOLIO_MANAGEMENT);
		
		managerUser = new User();
		managerUser.setLogin("manager");
		managerUser.setPassword("pass");
		managerUser.setEmail("manager@corolla.com");
		
		projectStatus = new ProjectStatus();
		projectStatus.setName("Active");
		projectStatus.setClosed(false);
		
		gate.dispatch(new CreateProjectStatusCommand(projectStatus));
		
		existingProject = new Project();
		existingProject.setKey("COROLLA_TEST");
		existingProject.setName("Corolla");
		existingProject.setStatusId(projectStatus.getId());
		
		gate.dispatch(new CreateProjectCommand(existingProject));
		
	}
	
	@After
	public void tearDown() {
		projectDAO.deleteAll();
	}
	
	@Test
	public void basicUserShouldAccessPortfolioView() throws Exception {
		
		mvc
		.perform(get(PORTFOLIO_URL).with(user(new AuthUser(basicUser, basicRole))))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessPortfolioView() throws Exception {
		
		mvc.perform(get(PORTFOLIO_URL))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void basicUserShouldAccessPortfolioManagerView() throws Exception {
		
		mvc
			.perform(
				get(PORTFOLIO_MANAGER_URL)
				.with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessPortfolioManagerView() throws Exception {
		
		mvc.perform(get(PORTFOLIO_MANAGER_URL))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void basicUserShouldAccessProjectManagementView() throws Exception {
		
		mvc
		.perform(
				get(PORTFOLIO_MANAGER_URL+existingProject.getKey())
				.with(user(new AuthUser(basicUser, basicRole))))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void shouldRedirectIfProjectDoesNotExist() throws Exception {
		 
		mvc
			.perform(
					get(PORTFOLIO_MANAGER_URL+"blblbl")
					.with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl(PORTFOLIO_MANAGER_URL));
		
	}
	
	@Test
	public void managerUserShouldCreateProject() throws Exception {
		
		String key = "the_key";
		String name = "a name";
		Integer parentID = 1;
		
		mvc
			.perform(
				post(CREATE_PROJECT_URL)
				.param("key", key)
				.param("name", name)
				.param("parentID", parentID.toString())
				.with(user(new AuthUser(managerUser, managerRole))))
			.andExpect(status().isFound());

		Project project =projectDAO.findByKey(key);
		
		assertNotNull("Project should exists", project);
		assertEquals(name, project.getName());
                final Portfolio portfolioNode = portfolioDAO.find();
		assertNotNull("Portfolio node should exists", portfolioNode);
		TreeNode node = treeManagementService.findNode(portfolioNode, new FindNodeByIDPredicate(parentID));
		
		assertEquals(1, node.getNodes().size());
		assertEquals(project.getId(), ((ProjectNode) node.getNodes().iterator().next()).getProjectId());
		
	}
	
	@Test
	public void basicUserShouldNotCreateProject() throws Exception {
		
		long nbProjects = projectDAO.count();
		
		String key = "the_key";
		String name = "a name";
		Integer parentID = 1;
		
		mvc
			.perform(
				post(CREATE_PROJECT_URL)
				.param("key", key)
				.param("name", name)
				.param("parentID", parentID.toString())
				.with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isForbidden());

		assertEquals(nbProjects, projectDAO.count());
		
	}
	
}