package com.tocea.corolla.ui;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.users.commands.CreateRoleCommand;
import com.tocea.corolla.users.commands.CreateUserCommand;
import com.tocea.corolla.users.commands.DeleteRoleCommand;
import com.tocea.corolla.users.commands.DeleteUserCommand;
import com.tocea.corolla.users.commands.EditUserCommand;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;

public class AuthenticationTest extends AbstractSpringTest {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	
	
	@Autowired
	private Gate gate;
	
	private MockMvc mvc;
	
	private Role basicRole;
	private User basicUser;
	
	@Before
	public void setUp() {
		
		basicRole = new Role();
		basicRole.setName("BASIC");
		basicRole.setPermissions("");
		
		gate.dispatch(new CreateRoleCommand(basicRole));
		
		basicUser = new User();
		basicUser.setLogin("jsnow");
		basicUser.setFirstName("Jon");
		basicUser.setLastName("Snow");
		basicUser.setPassword("password");
		basicUser.setEmail("simple@corolla.com");
		basicUser.setRoleId(basicRole.getId());
		
		gate.dispatch(new CreateUserCommand(basicUser));
		
		basicUser.setActive(true);
		
		gate.dispatch(new EditUserCommand(basicUser));
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		
	}
	
	@After
	public void tearDown() {
		
		gate.dispatch(new DeleteUserCommand(basicUser.getLogin()));
		
		gate.dispatch(new DeleteRoleCommand(basicRole.getId()));
		
	}
	
	@Test
	public void testAuthenticationSuccess() throws Exception {
		
		mvc
		.perform(
				post("/login")
				.param("username", "jsnow")
				.param("password", "password"))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/ui/home"))
		.andExpect(authenticated().withUsername("jsnow"));
		
	}
	
	@Test
	public void testRequireAuthentication() throws Exception {
		
		mvc.perform(get("/ui/home")).andExpect(status().isFound());
		
	}
	
}
