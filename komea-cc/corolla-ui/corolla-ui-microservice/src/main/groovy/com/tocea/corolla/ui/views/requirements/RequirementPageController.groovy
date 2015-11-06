package com.tocea.corolla.ui.views.requirements

import groovy.util.logging.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.products.domain.ProjectBranch
import com.tocea.corolla.products.exceptions.ProjectBranchNotFoundException
import com.tocea.corolla.products.exceptions.ProjectNotFoundException
import com.tocea.corolla.requirements.dao.IRequirementDAO;
import com.tocea.corolla.requirements.domain.Requirement
import com.tocea.corolla.requirements.exceptions.RequirementNotFoundException
import com.tocea.corolla.revisions.domain.IChange
import com.tocea.corolla.revisions.domain.ICommit
import com.tocea.corolla.revisions.exceptions.InvalidCommitInformationException
import com.tocea.corolla.revisions.services.IRevisionService;
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO;

@Controller
@Slf4j
class RequirementPageController {

	private static String PROJECT_VIEW 		= "requirements/requirements"
	private static String REVISION_VIEW 	= "requirements/revision"
	
	@Autowired
	private IRevisionService revisionService;
	
	@Autowired
	private IRequirementDAO requirementDAO;
	
	@Autowired
	private IProjectBranchDAO branchDAO;
	
	@Autowired
	private IFolderNodeTypeDAO folderNodeTypeDAO;
	
	@Autowired
	private IProjectDAO projectDAO;
	
	@ModelAttribute("menu")
	public setMenu() {
		return "requirements"
	}
	
	@ModelAttribute("theme")
	public setTheme() {
		return "cape-honey"
	}
	
	@RequestMapping("/ui/requirements/{projectKey}")
	public ModelAndView getRequirementsPage(@PathVariable projectKey) {
			
		return getRequirementPage(projectKey, null, null)
	}
	
	@RequestMapping("/ui/requirements/{projectKey}/{branchName}")
	public ModelAndView getRequirementPageForBranch(@PathVariable projectKey, @PathVariable branchName) {
		
		return getRequirementPage(projectKey, branchName, null)
	}
	
	@RequestMapping("/ui/requirements/{projectKey}/{branchName}/{requirementKey}")
	public ModelAndView getRequirementPage(@PathVariable projectKey, @PathVariable branchName, @PathVariable requirementKey) {
		
		def project = findProjectOrFail(projectKey)				
		def branch = branchName ? findBranchOrFail(branchName, project) : branchDAO.findDefaultBranch(project.id)	
		def requirement = requirementDAO.findByKeyAndProjectBranchId(requirementKey, branch.id)
		
		def commits = requirement ? revisionService.getHistory(requirement.id, Requirement.class) : null
		
		def model = new ModelAndView(PROJECT_VIEW)
		model.addObject "project", project
		model.addObject "branch", branch
		model.addObject "requirement", requirement
		model.addObject "commits", commits
		model.addObject "folderNodeTypes", folderNodeTypeDAO.findAll()
		model.addObject "branches", branchDAO.findByProjectId(project.id)
		
		return model
	}
	
	@RequestMapping("/ui/requirements/{projectKey}/{branchName}/{requirementKey}/revisions/{commitID}")
	public ModelAndView getRevisionPage(@PathVariable projectKey, @PathVariable branchName, @PathVariable requirementKey, @PathVariable commitID) {
			
		Project project = findProjectOrFail(projectKey)
		def branch = branchName ? findBranchOrFail(branchName, project) : branchDAO.findDefaultBranch(project.id)
				
		Requirement requirement = requirementDAO.findByKeyAndProjectBranchId(requirementKey, branch.id)
				
		if (requirement == null) {
			throw new RequirementNotFoundException()
		}
		
		Collection<ICommit> commits = revisionService.getHistory(requirement.id, Requirement.class)
		
		commits.each { log.info it.id }

		def commit = revisionService.findCommitByID(requirement.id, Requirement.class, commitID)
		
		if (commit == null) {
			throw new InvalidCommitInformationException("No commit associated to this ID");
		}
		
		def previousCommit = revisionService.getPreviousCommit(requirement.id, Requirement.class, commitID)	
		
		def version = revisionService.getSnapshot(commit)
		def oldVersion = previousCommit != null ? revisionService.getSnapshot(previousCommit) : new Requirement(id: requirement.id)
		
		def changes = revisionService.compare oldVersion, version
		
		def model = new ModelAndView(REVISION_VIEW)
		model.addObject "project", project
		model.addObject "branch", branch
		model.addObject "requirement", requirement
		model.addObject "commit", commit
		model.addObject "previousCommit", previousCommit
		model.addObject "changes", changes
		
		return model	
	}
	
	private Project findProjectOrFail(String projectKey) {
		
		Project project = projectDAO.findByKey(projectKey)
				
		if (project == null) {
			throw new ProjectNotFoundException()
		}
		
		return project
	}
	
	private ProjectBranch findBranchOrFail(String branchName, Project project) {
		
		ProjectBranch branch = branchDAO.findByNameAndProjectId(branchName, project.id)
				
		if (branch == null) {
			throw new ProjectBranchNotFoundException()
		}
		
		return branch
	}
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler([
	       InvalidCommitInformationException.class, 
	       RequirementNotFoundException.class,
	       ProjectBranchNotFoundException.class,
	       ProjectNotFoundException.class
	])
	public void handlePageNotFoundException() {
		
	}
	
}