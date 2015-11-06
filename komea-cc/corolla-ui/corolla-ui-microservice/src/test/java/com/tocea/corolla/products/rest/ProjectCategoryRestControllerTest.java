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
import com.tocea.corolla.products.commands.CreateProjectCategoryCommand;
import com.tocea.corolla.products.dao.IProjectCategoryDAO;
import com.tocea.corolla.products.domain.ProjectCategory;
import com.tocea.corolla.tests.utils.AuthUserUtils;
import com.tocea.corolla.ui.AbstractSpringTest;

public class ProjectCategoryRestControllerTest extends AbstractSpringTest {

	private static final String BASE_URL 			= "/rest/projects/categories";
	private static final String CATEGORIES_ALL_URL 	= BASE_URL+"/all";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	private MockMvc mvc;
	
	@Autowired
	private IProjectCategoryDAO categoryDAO;
	
	@Autowired
	private Gate gate;
	
	private Collection<ProjectCategory> existingCategories;
	
	@Before
	public void setUp() {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		
		existingCategories = Lists.newArrayList();
		
		ProjectCategory category1 = new ProjectCategory();
		category1.setName("cat1");
		
		gate.dispatch(new CreateProjectCategoryCommand(category1));
		existingCategories.add(category1);
		
		ProjectCategory category2 = new ProjectCategory();
		category2.setName("cat2");
		
		gate.dispatch(new CreateProjectCategoryCommand(category2));
		existingCategories.add(category2);
		
	}
	
	@After
	public void tearDown() {
		
		categoryDAO.deleteAll();
	}
	
	@Test
	public void basicUserShouldAccessCategoriesList() throws Exception {
		
		mvc
			.perform(
					get(CATEGORIES_ALL_URL).
				with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk());
			
	}
	
	@Test
	public void anonymousUserShouldNotAccessCategoriesList() throws Exception {
		
		mvc
			.perform(
					get(CATEGORIES_ALL_URL)
			)
			.andExpect(redirectedUrlPattern("**/login"));
	}
	
}