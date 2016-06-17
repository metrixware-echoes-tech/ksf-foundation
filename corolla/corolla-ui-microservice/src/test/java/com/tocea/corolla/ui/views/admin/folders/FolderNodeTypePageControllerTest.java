package com.tocea.corolla.ui.views.admin.folders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.trees.commands.CreateFolderNodeTypeCommand;
import com.tocea.corolla.trees.domain.FolderNodeType;
import com.tocea.corolla.ui.AbstractSpringTest;
import com.tocea.corolla.users.domain.Permission;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;

import fr.echoes.labs.ksf.users.security.auth.AuthUser;

public class FolderNodeTypePageControllerTest extends AbstractSpringTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	 
	private MockMvc mvc;
	
	@Autowired
	private Gate gate;
	
	private Role adminRole;
	private Role basicRole;
	
	private User admin;
	private User basicUser;
	
	private FolderNodeType existingType;
	
	@Before
	public void setUp() {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		
		adminRole = new Role();
		adminRole.setName("ADMIN");
		adminRole.setPermissions(Permission.ADMIN);
		
		basicRole = new Role();
		basicRole.setName("BASIC");
		basicRole.setPermissions("");
		
		admin = new User();
		admin.setLogin("admin");
		admin.setPassword("pass");
		admin.setRole(adminRole);
		admin.setEmail("admin@corolla.com");
		
		basicUser = new User();
		basicUser.setLogin("simple");
		basicUser.setPassword("pass");
		basicUser.setEmail("simple@corolla.com");
		
		existingType = gate.dispatch(new CreateFolderNodeTypeCommand(new FolderNodeType("blblbl", "http://awesome-icon.com/image.png")));
		
	}
	
	@Test
	public void adminShouldAccessListView() throws Exception {
		
		mvc
		.perform(get("/ui/admin/folder-node-types").with(user(new AuthUser(admin, adminRole))))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void basicUserShouldNotAccessListView() throws Exception {
		
		mvc
		.perform(get("/ui/admin/folder-node-types").with(user(new AuthUser(basicUser, basicRole))))
		.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void adminShouldAccessAddFormView() throws Exception {
		
		mvc
		.perform(get("/ui/admin/folder-node-types/add").with(user(new AuthUser(admin, adminRole))))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void basicUserShouldNotAccessAddFormView() throws Exception {
		
		mvc
		.perform(get("/ui/admin/folder-node-types/add").with(user(new AuthUser(basicUser, basicRole))))
		.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void adminShouldAddNewFolderType() throws Exception {
		
		mvc
		.perform(
				post("/ui/admin/folder-node-types/add")
				.param("name", "blblbl")
				.param("icon", "http://awesome-icon.com/image.png")
				.with(user(new AuthUser(admin, adminRole))))
		.andExpect(status().isFound());
		
	}
	
	@Test
	public void adminShouldAccessEditFormView() throws Exception {
		
		mvc
		.perform(get("/ui/admin/folder-node-types/edit/"+existingType.getId()).with(user(new AuthUser(admin, adminRole))))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void basicUserShouldNotAccessEditFormView() throws Exception {
		
		mvc
		.perform(get("/ui/admin/folder-node-types/edit/"+existingType.getId()).with(user(new AuthUser(basicUser, basicRole))))
		.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void adminShouldEditFolderType() throws Exception {
		
		mvc
		.perform(
				post("/ui/admin/folder-node-types/edit/"+existingType.getId())
				.param("id", existingType.getId())
				.param("name", "blblbl")
				.param("icon", "http://awesome-icon.com/image.png")
				.with(user(new AuthUser(admin, adminRole))))
		.andExpect(status().isFound());
		
	}
	
}
