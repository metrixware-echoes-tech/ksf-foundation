package com.tocea.corolla.ui.views.projects

import groovy.util.logging.Slf4j

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView

import com.tocea.corolla.cqrs.gate.CommandExecutionException
import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.portfolio.commands.RemovePortfolioNodeCommand
import com.tocea.corolla.portfolio.dao.IPortfolioDAO
import com.tocea.corolla.portfolio.predicates.FindNodeByProjectIDPredicate
import com.tocea.corolla.products.commands.CreateProjectBranchCommand
import com.tocea.corolla.products.commands.EditProjectBranchCommand
import com.tocea.corolla.products.commands.EditProjectCommand
import com.tocea.corolla.products.commands.RestoreProjectStateCommand
import com.tocea.corolla.products.dao.IProjectBranchDAO
import com.tocea.corolla.products.dao.IProjectCategoryDAO
import com.tocea.corolla.products.dao.IProjectDAO
import com.tocea.corolla.products.dao.IProjectStatusDAO
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.products.domain.ProjectBranch
import com.tocea.corolla.products.exceptions.ProjectBranchAlreadyExistException
import com.tocea.corolla.products.exceptions.ProjectBranchNotFoundException
import com.tocea.corolla.products.exceptions.ProjectNotFoundException
import com.tocea.corolla.products.utils.ProjectChangeFormatter
import com.tocea.corolla.revisions.exceptions.InvalidCommitInformationException
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO
import com.tocea.corolla.trees.services.ITreeManagementService
import com.tocea.corolla.users.dao.IUserDAO
import com.tocea.corolla.users.domain.Permission
import com.tocea.corolla.users.dto.UserDto

@Controller
@Slf4j
public class ProjectDetailsPageController {

	private static final String DETAILS_VIEW 		= "project/details"
	private static final String REVISION_VIEW 		= "project/revision"
	private static final String BRANCH_FORM_VIEW	= "project/branch_form"

	@Autowired
	private IProjectDAO projectDAO

	@Autowired
	private IFolderNodeTypeDAO folderNodeTypeDAO

	@Autowired
	private IProjectBranchDAO branchDAO

	@Autowired
	private IProjectStatusDAO statusDAO

	@Autowired
	private IProjectCategoryDAO projectCategoryDAO

	@Autowired
	private IUserDAO userDAO

	@Autowired
	private IPortfolioDAO portfolioDAO

	@Autowired
	private IRevisionService revisionService

	@Autowired
	private ITreeManagementService treeManagementService

	@Autowired
	private ProjectChangeFormatter changeFormatter

	@Autowired
	private Gate gate

	@ModelAttribute("menu")
	public String setMenu() {
		return "projectDetails"
	}

	@RequestMapping(value="/ui/projects/{projectKey}")
	public ModelAndView getProjectDetails(@PathVariable projectKey) {

		def project = projectDAO.findByKey(projectKey)

		if (project == null) {
			throw new ProjectNotFoundException()
		}

		def model = buildManagerViewData(new ModelAndView(DETAILS_VIEW), project)

		return model
	}

	@RequestMapping(value = "/ui/projects/{projectID}/edit", method = RequestMethod.POST)
	@Secured([
		Permission.PROJECT_MANAGEMENT
	])
	public ModelAndView editProject(@PathVariable String projectID, @Valid @ModelAttribute("project") Project project, BindingResult _result) {

		project = _result.model.get("project")
		def original = projectDAO.findOne(project.id)

		if (project == null || original == null) {
			throw new ProjectNotFoundException()
		}

		if (_result.hasErrors()) {
			def model = buildManagerViewData(new ModelAndView(DETAILS_VIEW), project)
			model.addObject "selectedTab", "edit"
			return model
		}

		project.tags = original.tags

		gate.dispatch new EditProjectCommand(project)

		return new ModelAndView("redirect:/ui/projects/"+project.key)
	}

	@RequestMapping("/ui/projects/{projectKey}/revisions/{commitID}")
	public ModelAndView getRevisionPage(@PathVariable projectKey, @PathVariable commitID) {

		def project = findProjectOrFail(projectKey)

		def commit = revisionService.findCommitByID(project.id, project.class, commitID)

		if (commit == null) {
			throw new InvalidCommitInformationException("No commit associated to this ID")
		}

		def previousCommit = revisionService.getPreviousCommit(project.id, project.class, commitID)

		def version = revisionService.getSnapshot(commit)
		def oldVersion = previousCommit != null ? revisionService.getSnapshot(previousCommit) : new Project(id: project.id)

		def changes = revisionService.compare oldVersion, version

		def model = new ModelAndView(REVISION_VIEW)
		model.addObject "project", project
		model.addObject "commit", commit
		model.addObject "previousCommit", previousCommit
		model.addObject "changes", changes
		model.addObject "changeFormatter", changeFormatter

		return model
	}

	@RequestMapping("/ui/projects/{projectKey}/revisions/{commitID}/restore")
	@Secured([
		Permission.PROJECT_MANAGEMENT
	])
	public ModelAndView restoreProjectState(@PathVariable projectKey, @PathVariable commitID) {

		def project = findProjectOrFail(projectKey)

		gate.dispatch new RestoreProjectStateCommand(project, commitID)

		return new ModelAndView("redirect:/ui/projects/"+project.key+"#revisions")
	}

	@RequestMapping("/ui/projects/{projectKey}/branches/add/{originBranchName}")
	public ModelAndView getAddBranchPage(@PathVariable projectKey, @PathVariable originBranchName) {

		def project = findProjectOrFail(projectKey)

		return buildBranchFormData(project, new ProjectBranch(), originBranchName)
	}

	@RequestMapping(value = "/ui/projects/{projectKey}/branches/add/{originBranchName}", method = RequestMethod.POST)
	public ModelAndView addBranch(@PathVariable projectKey, @PathVariable originBranchName, @Valid @ModelAttribute("branch") ProjectBranch branch, BindingResult _result) {

		branch = _result.model.get("branch")

		def project = findProjectOrFail(projectKey)

		if (_result.hasErrors()) {
			return buildBranchFormData(project, branch, originBranchName)
		}

		def origin = branchDAO.findByNameAndProjectId(originBranchName, project.id)

		try {

			gate.dispatch new CreateProjectBranchCommand(branch.name, origin)
		}catch(Exception ex) {

			if (ex.cause instanceof ProjectBranchAlreadyExistException) {
				_result.rejectValue("name", "error.name", "This name is already used by another project branch")
				return buildBranchFormData(project, branch, originBranchName)
			}else{
				throw ex
			}
		}

		return new ModelAndView("redirect:/ui/projects/"+project.key+"#branches")
	}

	@RequestMapping(value = "/ui/projects/{projectKey}/branches/edit/{branchName}")
	public ModelAndView getEditBranchPage(@PathVariable projectKey, @PathVariable branchName) {

		def project = findProjectOrFail(projectKey)
		def branch = findProjectBranchOrFail(branchName, project)

		return buildBranchFormData(project, branch, "")
	}

	@RequestMapping(value = "/ui/projects/{projectKey}/branches/edit/{branchName}", method = RequestMethod.POST)
	public ModelAndView editBranch(@PathVariable projectKey, @PathVariable branchName, @Valid @ModelAttribute("branch") ProjectBranch branch, BindingResult _result) {

		branch = _result.model.get("branch")

		def project = findProjectOrFail(projectKey)

		if (_result.hasErrors()) {
			return buildBranchFormData(project, branch, "")
		}

		try {

			branch = gate.dispatch new EditProjectBranchCommand(branch)
		}catch(CommandExecutionException ex) {

			if (ex.cause instanceof ProjectBranchAlreadyExistException) {
				_result.rejectValue("name", "error.name", "This name is already used by another project branch")
				return buildBranchFormData(project, branch, "")
			}else{
				throw ex
			}
		}

		return new ModelAndView("redirect:/ui/projects/"+project.key+"#branches")
	}

	@RequestMapping(value = "/ui/projects/{projectKey}/delete")
	@Secured([
		Permission.PORTFOLIO_MANAGEMENT
	])
	public ModelAndView deleteProject(@PathVariable projectKey) {

		Project project = findProjectOrFail(projectKey)
		def node = treeManagementService.findNode(portfolioDAO.find(), new FindNodeByProjectIDPredicate(project.id))

		gate.dispatch new RemovePortfolioNodeCommand(node.id)

		return new ModelAndView("redirect:/ui/portfolio/manager")
	}

	private ModelAndView buildManagerViewData(ModelAndView model, Project project) {

		def commits = revisionService.getHistory(project.id, project.class)
		def owner = project.ownerId ? userDAO.findOne(project.ownerId) : null

		model.addObject "project", project
		model.addObject "status", statusDAO.findOne(project.statusId)
		model.addObject "category", project.categoryId ? projectCategoryDAO.findOne(project.categoryId) : null
		model.addObject "owner", owner ? new UserDto(owner) : null
		model.addObject "folderNodeTypes", folderNodeTypeDAO.findAll()
		model.addObject "branches", branchDAO.findByProjectId(project.id)
		model.addObject "commits", commits
		model.addObject "statuses", statusDAO.findAll()
		model.addObject "categories", projectCategoryDAO.findAll()

		return model
	}

	private ModelAndView buildBranchFormData(Project project, ProjectBranch branch, String originBranchName) {

		def model = new ModelAndView(BRANCH_FORM_VIEW)
		model.addObject "project", project
		model.addObject "branch", branch
		model.addObject "originBranchName", originBranchName

		return model
	}

	private Project findProjectOrFail(String projectKey) {

		def project = projectDAO.findByKey(projectKey)

		if (project == null) {
			throw new ProjectNotFoundException()
		}

		return project
	}

	private ProjectBranch findProjectBranchOrFail(String branchName, Project project) {

		def branch = branchDAO.findByNameAndProjectId(branchName, project.id)

		if (branch == null) {
			throw new ProjectBranchNotFoundException()
		}

		return branch
	}

	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler([
		InvalidCommitInformationException.class,
		ProjectNotFoundException.class
	])
	public void handlePageNotFoundException() {
	}
}