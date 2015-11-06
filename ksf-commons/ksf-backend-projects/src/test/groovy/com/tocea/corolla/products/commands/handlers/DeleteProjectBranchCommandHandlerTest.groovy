package com.tocea.corolla.products.commands.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.commands.DeleteProjectBranchCommand
import com.tocea.corolla.products.dao.IProjectBranchDAO
import com.tocea.corolla.products.dao.IProjectDAO
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.products.domain.ProjectBranch
import com.tocea.corolla.products.exceptions.InvalidProjectBranchInformationException
import com.tocea.corolla.products.exceptions.MissingProjectBranchInformationException
import com.tocea.corolla.products.exceptions.ProjectBranchOperationForbiddenException
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "DELETE_PROJECT_BRANCH")
class DeleteProjectBranchCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectBranchDAO branchDAO = Mock(IProjectBranchDAO)
	//	def IRequirementsTreeDAO requirementsTreeDAO = Mock(IRequirementsTreeDAO)
	//	def IRequirementDAO requirementDAO = Mock(IRequirementDAO)
	def Gate gate = Mock(Gate)
	def DeleteProjectBranchCommandHandler handler
	def IRevisionService revisionService = Mock(IRevisionService)
	def IProjectDAO projectDAO = Mock(IProjectDAO)
	
	def setup() {
		handler = new DeleteProjectBranchCommandHandler(
				branchDAO : branchDAO,
				//				requirementsTreeDAO : requirementsTreeDAO,
				//				requirementDAO : requirementDAO,
				projectDAO : projectDAO,
				gate : gate
				)
	}
	
	def "it should delete a project branch"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name: "Master", projectId: "35")
		def requirements = []
		
		when:
		handler.handle new DeleteProjectBranchCommand(branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		1 * branchDAO.delete(branch)
		//1 * requirementDAO.findByProjectBranchId(branch.id) >> requirements
		
	}
	
	def "it should delete the requirements tree of the project branch"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name: "Master", projectId: "35")
		def requirements = []
		//	def requirementsTree = new RequirementsTree()
		
		when:
		handler.handle new DeleteProjectBranchCommand(branch)
		
		then:
		notThrown(Exception.class)
		
		//		then:
		//		1 * requirementsTreeDAO.findByBranchId(branch.id) >> requirementsTree
		//
		//		then:
		//		1 * requirementsTreeDAO.delete(requirementsTree)
		//		1 * requirementDAO.findByProjectBranchId(branch.id) >> requirements
		
	}
	
	def "it should delete all the requirements attached to the project branch"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name: "Master", projectId: "35")
		//		def requirements = [
		//			new Requirement(id: "1"),
		//			new Requirement(id: "2")
		//		]
		//		def requirementsTree = new RequirementsTree()
		
		when:
		handler.handle new DeleteProjectBranchCommand(branch)
		
		then:
		notThrown(Exception.class)
		
		//		then:
		//		1 * requirementDAO.findByProjectBranchId(branch.id) >> requirements
		//		1 * gate.dispatch { it instanceof DeleteRequirementCommand && it.requirementID == requirements[0].id }
		//		1 * gate.dispatch { it instanceof DeleteRequirementCommand && it.requirementID == requirements[1].id }
		
	}
	
	def "it should throw an exception if the project branch is missing"() {
		
		given:
		def branch = null
		
		when:
		handler.handle new DeleteProjectBranchCommand(branch)
		
		then:
		thrown(MissingProjectBranchInformationException.class)
		
	}
	
	def "it should throw an exception if the project branch ID is missing"() {
		
		given:
		def branch = new ProjectBranch(id: "", name: "Master", projectId: "35")
		
		when:
		handler.handle new DeleteProjectBranchCommand(branch)
		
		then:
		thrown(InvalidProjectBranchInformationException.class)
		
	}
	
	def "it should not delete the branch if it is marked as default"() {
		
		given:
		def branch = new ProjectBranch(
				id: "1",
				name: "Master",
				projectId: "35",
				defaultBranch: true
				)
		def project = new Project(id: branch.projectId)
		def requirements = []
		
		when:
		handler.handle new DeleteProjectBranchCommand(branch)
		
		then:
		projectDAO.findOne(branch.projectId) >> project
		
		then:
		0 * branchDAO.delete(_)
		thrown(ProjectBranchOperationForbiddenException.class)
		
	}
	
}