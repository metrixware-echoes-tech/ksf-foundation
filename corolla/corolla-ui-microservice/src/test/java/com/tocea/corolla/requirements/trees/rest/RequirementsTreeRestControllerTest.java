package com.tocea.corolla.requirements.trees.rest;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.requirements.commands.CreateRequirementCommand;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.requirements.trees.commands.CreateRequirementFolderNodeCommand;
import com.tocea.corolla.requirements.trees.dao.IRequirementsTreeDAO;
import com.tocea.corolla.requirements.trees.domain.RequirementNode;
import com.tocea.corolla.requirements.trees.domain.RequirementsTree;
import com.tocea.corolla.trees.commands.CreateFolderNodeTypeCommand;
import com.tocea.corolla.trees.domain.FolderNode;
import com.tocea.corolla.trees.domain.FolderNodeType;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.predicates.FindNodeByIDPredicate;
import com.tocea.corolla.trees.services.ITreeManagementService;
import com.tocea.corolla.ui.AbstractSpringTest;
import com.tocea.corolla.ui.security.AuthUser;
import com.tocea.corolla.users.domain.Permission;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;

public class RequirementsTreeRestControllerTest extends AbstractSpringTest {

	private static final String TREE_URL 				= "/rest/requirements/tree/";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	 
	private MockMvc mvc;
	
	@Autowired
	private ITreeManagementService treeManagementService;
	
	@Autowired
	private IProjectBranchDAO branchDAO;
	
	@Autowired
	private IRequirementsTreeDAO requirementsTreeDAO;
	
	@Autowired
	private Gate gate;
	
	private Role basicRole;
	private Role managerRole;
	
	private User basicUser;
	private User managerUser;
	
	private Project existingProject;
	private ProjectBranch masterBranch;
	
	private Requirement req1;
	
	private FolderNodeType folderNodeType;
	private FolderNodeType otherFolderNodeType;
	private FolderNode folder1;
	private RequirementNode nodeReq1;
	
	@Before
	public void setUp() {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		
		basicRole = new Role();
		basicRole.setName("BASIC");
		basicRole.setPermissions("");
		
		managerRole = new Role();
		managerRole.setName("Manager");
		managerRole.setPermissions(Permission.PROJECT_WRITE);
		
		basicUser = new User();
		basicUser.setLogin("simple");
		basicUser.setPassword("pass");
		basicUser.setEmail("simple@corolla.com");
		
		managerUser = new User();
		managerUser.setLogin("manager");
		managerUser.setPassword("pass");
		managerUser.setEmail("manager@corolla.com");
		
		existingProject = new Project();
		existingProject.setKey("COROLLA_TEST");
		existingProject.setName("Corolla");
		
		gate.dispatch(new CreateProjectCommand(existingProject));
		
		masterBranch = branchDAO.findDefaultBranch(existingProject.getId());
		
		req1 = new Requirement();
		req1.setKey("REQ1");
		req1.setName("Requirement #1");
		req1.setProjectBranchId(masterBranch.getId());

		gate.dispatch(new CreateRequirementCommand(req1));
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		nodeReq1 = (RequirementNode) tree.getNodes().iterator().next();
		
		folderNodeType = new FolderNodeType("Tests", "http://awesome-icons.com/image.png");
		gate.dispatch(new CreateFolderNodeTypeCommand(folderNodeType));
		
		otherFolderNodeType = new FolderNodeType("Others", "http://awesome-icons.com/image.png");
		gate.dispatch(new CreateFolderNodeTypeCommand(otherFolderNodeType));
		
		folder1 = gate .dispatch(new CreateRequirementFolderNodeCommand(masterBranch, null, "folder1", folderNodeType.getId()));
				
	}
	
	private String getTreeURL(Project project, ProjectBranch branch) {
		
		return TREE_URL+project.getKey()+"/"+branch.getName()+"/";
	}
	
	private String getJsTreeURL(Project project, ProjectBranch branch) {
		
		return getTreeURL(project, branch)+"jstree";
	}
	
	private String getAddURL(Project project, ProjectBranch branch, TreeNode parentNode, FolderNodeType type) {
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
					.append("folders/add/");
		
		if (parentNode != null) {
			url.append(parentNode.getId()).append("/");
		}
					
		return url.append(type.getId()).toString();
	}
	
	@Test
	public void basicUserShouldAccessRequirementsTree() throws Exception {
		
		String URL = getTreeURL(existingProject, masterBranch);
		
		mvc
			.perform(get(URL).with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessRequirementsTree() throws Exception {
		
		String URL = getTreeURL(existingProject, masterBranch);
		
		mvc
			.perform(get(URL))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void basicUserShouldAccessRequirementsJsTree() throws Exception {
		
		String URL = getJsTreeURL(existingProject, masterBranch);
		
		mvc
			.perform(get(URL).with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void anonymousUserShouldNotAccessRequirementsJsTree() throws Exception {
		
		String URL = getJsTreeURL(existingProject, masterBranch);
		
		mvc
			.perform(get(URL))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void managerUserShouldMoveNode() throws Exception {
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		
		assertEquals(2, tree.getNodes().size());
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
				.append("move/")
				.append(nodeReq1.getId())
				.append("/")
				.append(folder1.getId());
		
		mvc
			.perform(get(url.toString()).with(user(new AuthUser(managerUser, managerRole))))
			.andExpect(status().isOk())
			.andReturn();
		
		tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		
		assertEquals(1, tree.getNodes().size());
		assertEquals(1, tree.getNodes().iterator().next().getNodes().size());
		
	}
	
	@Test
	public void basicUserShouldNotMoveNode() throws Exception {
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
				.append("move/")
				.append(nodeReq1.getId())
				.append("/")
				.append(folder1.getId());
		
		mvc
			.perform(get(url.toString()).with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void managerUserShouldRemoveNode() throws Exception {
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		
		assertEquals(2, tree.getNodes().size());
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
				.append("remove/")
				.append(folder1.getId());
		
		mvc
			.perform(get(url.toString()).with(user(new AuthUser(managerUser, managerRole))))
			.andExpect(status().isOk())
			.andReturn();
		
		tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		
		assertEquals(1, tree.getNodes().size());
		
	}
	
	@Test
	public void basicUserShouldNotRemoveNode() throws Exception {
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
				.append("remove/")
				.append(folder1.getId());
		
		mvc
			.perform(get(url.toString()).with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void managerUserShouldEditFolderNode() throws Exception {
		
		String newText = "NEW_TEXT";
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
					.append("folders/edit/")
					.append(folder1.getId());
		
		mvc
			.perform(
					post(url.toString())
					.content(newText)
					.contentType(MediaType.TEXT_PLAIN)
					.with(user(new AuthUser(managerUser, managerRole))))
			.andExpect(status().isOk())
			.andReturn();
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		folder1 = (FolderNode) treeManagementService.findNode(tree, new FindNodeByIDPredicate(folder1.getId()));
		
		assertEquals(newText, folder1.getText());
		
	}
	
	@Test
	public void basicUserShouldNotEditFolderNode() throws Exception {
		
		String newText = "NEW_TEXT";
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
					.append("folders/edit/")
					.append(folder1.getId());
		
		mvc
			.perform(
					post(url.toString())
					.content(newText)
					.contentType(MediaType.TEXT_PLAIN)
					.with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isForbidden())
			.andReturn();
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		folder1 = (FolderNode) treeManagementService.findNode(tree, new FindNodeByIDPredicate(folder1.getId()));
		
		assert !folder1.getText().equals(newText);
		
	}
	
	@Test
	public void managerUserShouldAddPortfolioFolderNode() throws Exception {
		
		String newText = "NEW_TEXT";
		
		String URL = getAddURL(existingProject, masterBranch, folder1, folderNodeType);
		
		mvc
			.perform(
					post(URL)
					.content(newText)
					.contentType(MediaType.TEXT_PLAIN)
					.with(user(new AuthUser(managerUser, managerRole))))
			.andExpect(status().isOk())
			.andReturn();
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		folder1 = (FolderNode) treeManagementService.findNode(tree, new FindNodeByIDPredicate(folder1.getId()));
		
		assertEquals(1, folder1.getNodes().size());
		assertEquals(newText, ((FolderNode) folder1.getNodes().iterator().next()).getText());
		
	}
	
	@Test
	public void basicUserShouldNotAddFolderNode() throws Exception {
		
		String newText = "NEW_TEXT";
		
		String URL = getAddURL(existingProject, masterBranch, folder1, folderNodeType);
		
		mvc
			.perform(
					post(URL)
					.content(newText)
					.contentType(MediaType.TEXT_PLAIN)
					.with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isForbidden())
			.andReturn();
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		folder1 = (FolderNode) treeManagementService.findNode(tree, new FindNodeByIDPredicate(folder1.getId()));
		
		assertEquals(0, folder1.getNodes().size());
		
	}
	
	@Test
	public void managerUserShouldAddFolderNodeAtTreeRoot() throws Exception {
		
		String newText = "NEW_TEXT";
		
		String URL = getAddURL(existingProject, masterBranch, null, folderNodeType);
		
		mvc
			.perform(
					post(URL)
					.content(newText)
					.contentType(MediaType.TEXT_PLAIN)
					.with(user(new AuthUser(managerUser, managerRole))))
			.andExpect(status().isOk())
			.andReturn();
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		
		assertEquals(3, tree.getNodes().size());
		
	}
	
	@Test
	public void basicUserShouldNotAddFolderNodeAtTreeRoot() throws Exception {
		
		String newText = "NEW_TEXT";
		
		String URL = getAddURL(existingProject, masterBranch, null, folderNodeType);
		
		mvc
			.perform(
					post(URL)
					.content(newText)
					.contentType(MediaType.TEXT_PLAIN)
					.with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isForbidden())
			.andReturn();
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		
		assertEquals(2, tree.getNodes().size());
		
	}
	
	@Test
	public void managerUserShouldChangeFolderNodeType() throws Exception {
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
				.append("folders/edit/type/")
				.append(folder1.getId())
				.append("/")
				.append(otherFolderNodeType.getId());
		
		mvc
			.perform(
					get(url.toString())
					.with(user(new AuthUser(managerUser, managerRole))))
			.andExpect(status().isOk())
			.andReturn();
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(masterBranch.getId());
		folder1 = (FolderNode) treeManagementService.findNode(tree, new FindNodeByIDPredicate(folder1.getId()));
		
		assertEquals(otherFolderNodeType.getId(), folder1.getTypeID());
		
	}
	
	@Test
	public void basicUserShouldNotChangePortfolioFolderNodeType() throws Exception {
		
		StringBuilder url = 
				new StringBuilder(getTreeURL(existingProject, masterBranch))
				.append("folders/edit/type/")
				.append(folder1.getId())
				.append("/")
				.append(otherFolderNodeType.getId());
		
		mvc
			.perform(
					get(url.toString())
					.with(user(new AuthUser(basicUser, basicRole))))
			.andExpect(status().isForbidden())
			.andReturn();
		
	}
	
}