package com.tocea.corolla.products.rest;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.CreateProjectBranchCommand;
import com.tocea.corolla.products.commands.CreateProjectCategoryCommand;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.commands.CreateProjectStatusCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.domain.ProjectCategory;
import com.tocea.corolla.products.domain.ProjectStatus;
import com.tocea.corolla.products.dto.ProjectFilterDTO;
import com.tocea.corolla.tests.utils.AuthUserUtils;
import com.tocea.corolla.ui.AbstractSpringTest;
import java.util.List;
import javax.servlet.Filter;
import org.apache.commons.lang3.StringUtils;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ProjectRestControllerTest extends AbstractSpringTest {

	private static final String PROJECTS_URL 		= "/rest/projects/";
	private static final String PROJECTS_ALL_URL	= PROJECTS_URL+"all";
	private static final String TAGS_URL			= PROJECTS_URL+"/tags";
	private static final String FILTER_URL			= PROJECTS_URL+"/filter";
	private static final String FILTER_IDS_URL		= FILTER_URL+"/keys";
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	private MockMvc mvc;
	
	@Autowired
	private IProjectDAO projectDAO;
	
	@Autowired
	private IProjectBranchDAO branchDAO;
	
	@Autowired
	private Gate gate;
	
	private List<ProjectStatus> statuses;
	private List<ProjectCategory> categories;
	
	private Project existingProject;
	private Project otherProject;
	
	private ProjectBranch masterBranch;
	private ProjectBranch devBranch;
	
	private List<String> userIds;
		
	@Before
	public void setUp() {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilters(springSecurityFilterChain)
				.build();
		
		userIds = Lists.newArrayList("user1", "user2", "user3");
		
		ProjectStatus status1 = new ProjectStatus();
		status1.setName("Active");
		status1.setClosed(false);
		
		ProjectStatus status2 = new ProjectStatus();
		status2.setName("Closed");
		status2.setClosed(true);
		
		ProjectStatus status3 = new ProjectStatus();
		status3.setName("Unknown");
		status3.setClosed(false);
		
		statuses = Lists.newArrayList(status1, status2, status3);
		
		for(ProjectStatus status : statuses) {
			gate.dispatch(new CreateProjectStatusCommand(status));
		}	
		
		ProjectCategory cat1 = new ProjectCategory();
		cat1.setName("cat1");
		ProjectCategory cat2 = new ProjectCategory();
		cat2.setName("cat2");
		ProjectCategory cat3 = new ProjectCategory();
		cat3.setName("cat3");
		
		categories = Lists.newArrayList(cat1, cat2, cat3);
		
		for(ProjectCategory cat : categories) {
			gate.dispatch(new CreateProjectCategoryCommand(cat));
		}
		
		existingProject = new Project();
		existingProject.setKey("COROLLA_TEST");
		existingProject.setName("Corolla");
		existingProject.setStatusId(status1.getId());
		existingProject.setTags(Lists.newArrayList("tag1", "tag2"));
		existingProject.setCategoryId(cat1.getId());
		
		gate.dispatch(new CreateProjectCommand(existingProject));
		
		masterBranch = branchDAO.findDefaultBranch(existingProject.getId());	
		devBranch = gate.dispatch(new CreateProjectBranchCommand("Dev", existingProject));
		
		otherProject = new Project();
		otherProject.setKey("OTHER_PROJECT");
		otherProject.setName("Another project");
		otherProject.setStatusId(status2.getId());
		otherProject.setCategoryId(cat2.getId());
		otherProject.setTags(Lists.newArrayList("tag2", "tag3"));
		otherProject.setOwnerId(userIds.get(0));
		
		gate.dispatch(new CreateProjectCommand(otherProject));
		
	}
	
	private String buildProjectURL(Project project) {
		
		return 
				new StringBuilder(PROJECTS_URL)
				.append(project.getKey())
				.toString();
	}
	
	private String buildDeleteBranchURL(Project project, ProjectBranch branch) {
		
		return
				new StringBuilder(buildProjectURL(project))
				.append("/branches/")
				.append(branch.getName())
				.append("/delete")
				.toString();
	}
	
	private String buildProjectTagsURL(Project project) {
		
		return
				new StringBuilder(buildProjectURL(project))
				.append("/tags")
				.toString();
	}
	
	private String buildProjectTagsPushURL(Project project) {
		
		return
				new StringBuilder(buildProjectTagsURL(project))
				.append("/push")
				.toString();
	}
	
	@Test
	public void basicUserShouldListAllProjects() throws Exception {
			
		mvc
			.perform(
					get(PROJECTS_ALL_URL)
					.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void anonymousUserShouldNotListAllProjects() throws Exception {
		
		mvc
			.perform(
				get(PROJECTS_ALL_URL)
			)
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void basicUserShouldListAllProjectsFromCollectionOfIDs() throws Exception {
		
		List<String> ids = Lists.newArrayList(existingProject.getId());
		Gson gson = new Gson();
		
		mvc
			.perform(
				post(PROJECTS_ALL_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(ids))
				.with(user(AuthUserUtils.basicUser()))
		)
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void projectManagerUserShouldDeleteBranch() throws Exception {
		
		mvc
			.perform(
					get(buildDeleteBranchURL(existingProject, devBranch)).
				with(user(AuthUserUtils.projectManager()))
			)
			.andExpect(status().isOk());
			
		assertNull(branchDAO.findOne(devBranch.getId()));
		
	}
	
	@Test
	public void basicUserShouldNotDeleteBranch() throws Exception {
		
		mvc
			.perform(
					get(buildDeleteBranchURL(existingProject, devBranch)).
				with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isForbidden());
			
		assertNotNull(branchDAO.findOne(devBranch.getId()));
		
	}
	
	@Test
	public void shouldNotDeleteDefaultBranch() throws Exception {
		
		mvc
			.perform(
				get(buildDeleteBranchURL(existingProject, masterBranch))
				.with(user(AuthUserUtils.projectManager()))
			)
			.andExpect(status().isNotAcceptable());
		
		assertNotNull(branchDAO.findOne(masterBranch.getId()));
		
	}
	
	@Test
	public void shouldRetrieveAllTags() throws Exception {
		
		Project project2 = new Project();
		project2.setKey("P2");
		project2.setName("Project 2");
		project2.setTags(Lists.newArrayList("tag2", "tag3"));
		
		gate.dispatch(new CreateProjectCommand(project2));
		
		mvc
			.perform(
					get(TAGS_URL)
					.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)));
		
	}
	
	@Test
	public void anonymousUserShouldNotRetrieveAllTags() throws Exception {
		
		mvc
			.perform(
					get(TAGS_URL)
			)
			.andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	public void shouldRetrieveProjectTags() throws Exception {
		
		mvc
			.perform(
					get(buildProjectTagsURL(existingProject))
					.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(existingProject.getTags().size())));
		
	}
	
	@Test
	public void shouldReturnAnEmptyListOfTagsOnInvalidProjectKey() throws Exception {
		
		Project invalidProject = new Project();
		invalidProject.setKey("invalid_key");
		
		mvc
			.perform(
					get(buildProjectTagsURL(invalidProject))
					.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
		
	}
	
	@Test
	public void anonymousUserShouldNotRetrieveProjectTags() throws Exception {
		
		mvc
			.perform(
				get(buildProjectTagsURL(existingProject))
			)
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void managerUserShouldPushProjectTags() throws Exception {
		
		List<String> newTags = Lists.newArrayList("newTags_1", "newTags_2", "newTags_3");
		
		mvc
			.perform(
				post(buildProjectTagsPushURL(existingProject))
				.content("tags="+StringUtils.join(newTags, ","))
				.with(user(AuthUserUtils.projectManager()))
			)
			.andExpect(status().isOk());

		existingProject = projectDAO.findOne(existingProject.getId());
		
		assertEquals(newTags.size(), existingProject.getTags().size());
		
		for(String newTag : newTags) {
			assert existingProject.getTags().contains(newTag);
		}
		
	}
	
	@Test
	public void basicUserShouldNotPushProjectTags() throws Exception {
		
		List<String> newTags = Lists.newArrayList("newTags_1", "newTags_2", "newTags_3");
		List<String> originTags = Lists.newArrayList(existingProject.getTags());
		
		mvc
			.perform(
				post(buildProjectTagsPushURL(existingProject))
				.content("tags="+StringUtils.join(newTags, ","))
				.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isForbidden());

		existingProject = projectDAO.findOne(existingProject.getId());
		
		assertEquals(originTags, existingProject.getTags());
		
	}
	
	@Test
	public void basicUserShouldFilterProjectListWithEmptyFilter() throws Exception {
		
		ProjectFilterDTO filter = new ProjectFilterDTO();

		mvc
			.perform(
				post(FILTER_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
				.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));
		
	}
	
	@Test
	public void basicUserShouldFilterProjectListByTags() throws Exception {
		
		ProjectFilterDTO filter = new ProjectFilterDTO();
		filter.setTags(Lists.newArrayList("tag1", "not_existing_tag"));
		
		mvc
			.perform(
				post(FILTER_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
				.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)));
		
	}
	
	@Test()
        @Ignore("Problem with json")
	public void basicUserShouldFilterProjectListByCategories() throws Exception {		
		
		ProjectFilterDTO filter = new ProjectFilterDTO();
		filter.setCategoryIds(Lists.newArrayList(
				categories.get(0).getId(), 
				categories.get(2).getId()
		));

		mvc
			.perform(
				post(FILTER_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
				.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)));
		
	}
	
	@Test
	public void basicUserShouldFilterProjectListByStatuses() throws Exception {
		
		ProjectFilterDTO filter = new ProjectFilterDTO();
		filter.setStatusIds(Lists.newArrayList(
				statuses.get(0).getId(), 
				statuses.get(2).getId()
		));

		mvc
			.perform(
				post(FILTER_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
				.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)));
		
	}
	
	@Test
	public void basicUserShouldFilterProjectListByOwners() throws Exception {
		
		ProjectFilterDTO filter = new ProjectFilterDTO();
		filter.setOwnerIds(Lists.newArrayList(userIds.get(0), userIds.get(1)));

		mvc
			.perform(
				post(FILTER_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
				.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
                        .andDo(print())
			.andExpect(jsonPath("$", hasSize(1)));
		
	}
	
	@Test
	public void basicUserShouldFilterProjectsOnMultipleCriteria() throws Exception {
		
		Project project = new Project();
		project.setKey("NEW_PROJECT");
		project.setName("Another project");
		project.setStatusId(statuses.get(0).getId());
		project.setTags(Lists.newArrayList("newTag"));
		project.setOwnerId(userIds.get(0));
		
		gate.dispatch(new CreateProjectCommand(project));
		
		ProjectFilterDTO filter = new ProjectFilterDTO();
		filter.setTags(Lists.newArrayList("newTag", "not_existing_tag"));
		filter.setOwnerIds(Lists.newArrayList(otherProject.getOwnerId()));

		mvc
			.perform(
				post(FILTER_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
				.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));
		
	}
	
	@Test
	public void anonymousUserShouldNotFilterProjects() throws Exception {
		
		ProjectFilterDTO filter = new ProjectFilterDTO();

		mvc
			.perform(
				post(FILTER_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
			)
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
	@Test
	public void basicUserShouldFilterProjectsAndRetrieveOnlyIDs() throws Exception {
		
		ProjectFilterDTO filter = new ProjectFilterDTO();

		mvc
			.perform(
				post(FILTER_IDS_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
				.with(user(AuthUserUtils.basicUser()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));
		
	}
	
	@Test
	public void anonymousUserShouldNotFilterProjectsAndRetrieveOnlyIDs() throws Exception {
		
		ProjectFilterDTO filter = new ProjectFilterDTO();

		mvc
			.perform(
				post(FILTER_IDS_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content((new Gson()).toJson(filter))
			)
			.andExpect(redirectedUrlPattern("**/login"));
		
	}
	
}