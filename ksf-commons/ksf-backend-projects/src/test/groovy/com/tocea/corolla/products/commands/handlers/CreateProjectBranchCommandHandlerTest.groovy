package com.tocea.corolla.products.commands.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.commands.CreateProjectBranchCommand
import com.tocea.corolla.products.dao.IProjectBranchDAO
import com.tocea.corolla.products.domain.ProjectBranch
import com.tocea.corolla.products.exceptions.InvalidProjectBranchInformationException
import com.tocea.corolla.products.exceptions.MissingProjectBranchInformationException
import com.tocea.corolla.products.exceptions.ProjectBranchAlreadyExistException
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "ADD_PROJECT_BRANCH")
class CreateProjectBranchCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectBranchDAO branchDAO = Mock(IProjectBranchDAO)
	def Gate gate = Mock(Gate)
	def CreateProjectBranchCommandHandler handler
	def IRevisionService revisionService = Mock(IRevisionService)
	
	def setup() {
		handler = new CreateProjectBranchCommandHandler(
				branchDAO : branchDAO,
				gate : gate
				)
	}
	
	def "it should create a new project branch"() {
		
		given:
		def branch = new ProjectBranch(name: "Master", projectId: "35")
		
		when:
		handler.handle new CreateProjectBranchCommand(branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		branchDAO.findByProjectId(branch.projectId) >> []
		branchDAO.findByNameAndProjectId(branch.name, branch.projectId) >> null
		
		then:
		1 * branchDAO.save(branch)
	}
	
	def "it should create a tree of requirements when creating a new branch"() {
		
		given:
		def branch = new ProjectBranch(name: "Master", projectId: "35")
		
		when:
		handler.handle new CreateProjectBranchCommand(branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		branchDAO.findByProjectId(branch.projectId) >> []
		branchDAO.findByNameAndProjectId(branch.name, branch.projectId) >> null
	}
	
	def "it should not create two branches with the same name on the same project"() {
		
		given:
		def branch = new ProjectBranch(name: "Master", projectId: "35")
		def sameBranch = new ProjectBranch(name: branch.name, projectId: branch.projectId)
		
		when:
		handler.handle new CreateProjectBranchCommand(branch)
		
		then:
		branchDAO.findByNameAndProjectId(branch.name, branch.projectId) >> sameBranch
		
		then:
		0 * branchDAO.save(_)
		thrown(ProjectBranchAlreadyExistException.class)
	}
	
	def "it should throw an exception if the branch is null"() {
		
		given:
		def branch = null
		
		when:
		handler.handle new CreateProjectBranchCommand(branch)
		
		then:
		0 * branchDAO.save(_)
		thrown(MissingProjectBranchInformationException.class)
	}
	
	def "it should throw an exception if the ID is already defined"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name: "Master", projectId: "35")
		
		when:
		handler.handle new CreateProjectBranchCommand(branch)
		
		then:
		0 * branchDAO.save(_)
		thrown(InvalidProjectBranchInformationException.class)
	}
	
	//	def "it should create a new branch from another empty branch"() {
	//
	//		given:
	//		def origin = new ProjectBranch(id: "1", name: "Master", projectId: "35")
	//		def originTree = new RequirementsTree(nodes: [])
	//
	//		when:
	//		handler.handle new CreateProjectBranchCommand("newBranch", origin)
	//
	//		then:
	//		notThrown(Exception.class)
	//
	//		then:
	//		branchDAO.findByProjectId(origin.projectId) >> [origin]
	//		requirementsTreeDAO.findByBranchId(origin.id) >> originTree
	//	}
	//
	//	def "it should create a new branch from another non-empty branch"() {
	//
	//		given:
	//		def origin = new ProjectBranch(id: "1", name: "Master", projectId: "35")
	//		def originRequirements = [
	//			new Requirement(id: "REQ1", name: "req1"),
	//			new Requirement(id: "REQ2", name: "req2")
	//		]
	//		def originTree = new RequirementsTree(
	//				nodes: [
	//					new RequirementNode(requirementId: originRequirements[0].id, nodes: []),
	//					new RequirementNode(requirementId: originRequirements[1].id, nodes: [])
	//				]
	//				)
	//
	//		when:
	//		handler.handle new CreateProjectBranchCommand("newBranch", origin)
	//
	//		then:
	//		notThrown(Exception.class)
	//
	//		then:
	//		branchDAO.findByProjectId(origin.projectId) >> [origin]
	//		1 * requirementsTreeDAO.save(_)
	//		requirementDAO.findByProjectBranchId(origin.id) >> originRequirements
	//		requirementsTreeDAO.findByBranchId(origin.id) >> originTree
	//		1 * gate.dispatch {
	//			it instanceof CreateRequirementCommand && it.requirement.name == originRequirements[0].name && it.requirement.id != originRequirements[0].id
	//		}
	//		1 * gate.dispatch {
	//			it instanceof CreateRequirementCommand && it.requirement.name == originRequirements[1].name && it.requirement.id != originRequirements[1].id
	//		}
	//		1 * requirementsTreeDAO.save { it.nodes.size() == 2 }
	//	}
	
	//	def "it should throw an exception if the requirements tree of the origin branch cannot be found"() {
	//
	//		given:
	//		def origin = new ProjectBranch(id: "1", name: "Master", projectId: "35")
	//		def originRequirements = [
	//			new Requirement(id: "REQ1", name: "req1"),
	//			new Requirement(id: "REQ2", name: "req2")
	//		]
	//		def originTree = null
	//
	//		when:
	//		handler.handle new CreateProjectBranchCommand("newBranch", origin)
	//
	//		then:
	//		branchDAO.findByProjectId(origin.projectId) >> [origin]
	//		requirementDAO.findByProjectBranchId(origin.id) >> originRequirements
	//		requirementsTreeDAO.findByBranchId(origin.id) >> originTree
	//
	//		then:
	//		thrown(RequirementsTreeNotFoundException.class)
	//	}
	
	def "it should set the branch as default if there is no other branch in the project"() {
		
		given:
		def branch = new ProjectBranch(name: "Master", projectId: "35")
		
		when:
		handler.handle new CreateProjectBranchCommand(branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		branchDAO.findByProjectId(branch.projectId) >> []
		branchDAO.findByNameAndProjectId(branch.name, branch.projectId) >> null
		
		then:
		branch.defaultBranch == true
	}
	
	def "it should not set the branch as default if there is other branches in the project"() {
		
		given:
		def branch = new ProjectBranch(name: "Master", projectId: "35")
		
		when:
		handler.handle new CreateProjectBranchCommand(branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		branchDAO.findByProjectId(branch.projectId) >> [new ProjectBranch()]
		branchDAO.findByNameAndProjectId(branch.name, branch.projectId) >> null
		
		then:
		branch.defaultBranch == false
	}
}