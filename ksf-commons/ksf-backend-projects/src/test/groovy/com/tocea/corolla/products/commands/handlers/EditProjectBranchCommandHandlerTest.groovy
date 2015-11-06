package com.tocea.corolla.products.commands.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.commands.EditProjectBranchCommand
import com.tocea.corolla.products.dao.IProjectBranchDAO
import com.tocea.corolla.products.domain.ProjectBranch
import com.tocea.corolla.products.exceptions.InvalidProjectBranchInformationException
import com.tocea.corolla.products.exceptions.MissingProjectBranchInformationException
import com.tocea.corolla.products.exceptions.ProjectBranchAlreadyExistException
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "EDIT_PROJECT_BRANCH")
class EditProjectBranchCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectBranchDAO branchDAO = Mock(IProjectBranchDAO)
	def EditProjectBranchCommandHandler handler
	def Gate gate = Mock(Gate)
	def setup() {
		handler = new EditProjectBranchCommandHandler(
				branchDAO : branchDAO,
				gate : gate
				)
	}
	
	def "it should edit a project branch"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name: "old")
		
		when:
		handler.handle new EditProjectBranchCommand(branch)
		
		then:
		branchDAO.findByNameAndProjectId(branch.name, branch.projectId) >> branch
		
		then:
		notThrown(Exception.class)
		1 * branchDAO.save(branch)
	}
	
	def "it should throw an exception if the name is already associated to another branch"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name: "old", projectId: "1")
		
		when:
		handler.handle new EditProjectBranchCommand(branch)
		
		then:
		branchDAO.findByNameAndProjectId(branch.name, branch.projectId) >> new ProjectBranch(id: 2, name: "other")
		
		then:
		0 * branchDAO.save(branch)
		thrown(ProjectBranchAlreadyExistException.class)
	}
	
	def "it should throw an exception if the branch is missing"() {
		
		given:
		def branch = null
		
		when:
		handler.handle new EditProjectBranchCommand(branch)
		
		then:
		0 * branchDAO.save(_)
		thrown(MissingProjectBranchInformationException.class)
	}
	
	def "it should throw an exception if the ID is not defined"() {
		
		given:
		def branch = new ProjectBranch(id: "", name: "old")
		
		when:
		handler.handle new EditProjectBranchCommand(branch)
		
		then:
		0 * branchDAO.save(_)
		thrown(InvalidProjectBranchInformationException.class)
	}
}