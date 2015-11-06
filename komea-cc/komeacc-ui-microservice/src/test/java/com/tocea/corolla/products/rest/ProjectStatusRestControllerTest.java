package com.tocea.corolla.products.rest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import javax.servlet.Filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.CreateProjectStatusCommand;
import com.tocea.corolla.products.dao.IProjectStatusDAO;
import com.tocea.corolla.products.domain.ProjectStatus;
import com.tocea.corolla.tests.utils.AuthUserUtils;
import com.tocea.corolla.ui.AbstractSpringTest;

public class ProjectStatusRestControllerTest extends AbstractSpringTest {

	private static final String BASE_URL 			= "/rest/projects/status";
	private static final String STATUS_ALL_URL 		= BASE_URL+"/all";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	private MockMvc mvc;
	
	@Autowired
	private IProjectStatusDAO statusDAO;
	
	@Autowired
	private Gate gate;
	
	private Collection<ProjectStatus> existingStatuses;
	
	@Before
	public void setUp() {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		
		existingStatuses = Lists.newArrayList();
		
		ProjectStatus status1 = new ProjectStatus();
		status1.setName("status1");
		
		gate.dispatch(new CreateProjectStatusCommand(status1));
		existingStatuses.add(status1);
		
		ProjectStatus status2 = new ProjectStatus();
		status2.setName("status2");
		
		gate.dispatch(new CreateProjectStatusCommand(status2));
		existingStatuses.add(status2);
		
	}
	
	@After
	public void tearDown() {
		
		statusDAO.deleteAll();
	}
	
	@Test
	public void basicUserShouldAccessCategoriesList() throws Exception {
		
		mvc
			.perform(
					get(STATUS_ALL_URL).
				with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk());
			
	}
	
	@Test
	public void anonymousUserShouldNotAccessCategoriesList() throws Exception {
		
		mvc
			.perform(
					get(STATUS_ALL_URL)
			)
			.andExpect(redirectedUrlPattern("**/login"));
	}
	
}