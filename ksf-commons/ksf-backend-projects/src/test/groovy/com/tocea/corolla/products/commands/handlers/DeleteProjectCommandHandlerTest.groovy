package com.tocea.corolla.products.commands.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.commands.DeleteProjectBranchCommand
import com.tocea.corolla.products.commands.DeleteProjectCommand
import com.tocea.corolla.products.dao.IProjectBranchDAO
import com.tocea.corolla.products.dao.IProjectDAO
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.products.domain.ProjectBranch
import com.tocea.corolla.products.exceptions.*
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "ADD_PROJECT")
class DeleteProjectCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectDAO projectDAO = Mock(IProjectDAO)
	def DeleteProjectCommandHandler handler
	def IProjectBranchDAO branchDAO = Mock(IProjectBranchDAO)
	def Gate gate = Mock(Gate)
	
	def setup() {
		handler = new DeleteProjectCommandHandler(
				projectDAO 				: projectDAO,
				branchDAO 				: branchDAO,
				gate 					: gate
				)
	}
	
	def "it should delete a project"() {
		
		given:
		def project = new Project()
		project.id = "3"
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new DeleteProjectCommand(project.id)
		
		then:
		projectDAO.findOne(project.id) >> project
		
		then:
		1 * projectDAO.delete(_)
		branchDAO.findByProjectId(project.id) >> []
		notThrown(Exception.class)
	}
	
	def "it should invoke the commands to delete the branches attached to the project"() {
		
		given:
		def project = new Project()
		project.id = "3"
		project.key = "my_project"
		project.name = "My Awesome Project"
		def branches = [
			new ProjectBranch(),
			new ProjectBranch()
		]
		
		when:
		handler.handle new DeleteProjectCommand(project.id)
		
		then:
		projectDAO.findOne(project.id) >> project
		branchDAO.findByProjectId(project.id) >> branches
		
		then:
		notThrown(Exception.class)
		2 * gate.dispatch { it instanceof DeleteProjectBranchCommand }
	}
	
	def "it should throw an exception if the project ID is empty"() {
		
		given:
		def project = new Project()
		project.id = ""
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new DeleteProjectCommand(project.id)
		
		then:
		0 * projectDAO.delete(_)
		thrown(MissingProjectInformationException.class)
	}
	
	def "it should throw an exception if the project does not exist"() {
		
		given:
		def project = new Project()
		project.id = "3"
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new DeleteProjectCommand(project.id)
		
		then:
		projectDAO.findOne(project.id) >> null
		0 * projectDAO.delete(_)
		thrown(ProjectNotFoundException.class)
	}
}