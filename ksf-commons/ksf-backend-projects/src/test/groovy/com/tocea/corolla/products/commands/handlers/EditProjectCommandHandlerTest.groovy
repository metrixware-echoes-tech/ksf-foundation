package com.tocea.corolla.products.commands.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.commands.EditProjectCommand
import com.tocea.corolla.products.dao.IProjectDAO
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.products.exceptions.InvalidProjectInformationException
import com.tocea.corolla.products.exceptions.MissingProjectInformationException
import com.tocea.corolla.products.exceptions.ProjectAlreadyExistException
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "EDIT_PROJECT")
class EditProjectCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectDAO projectDAO = Mock(IProjectDAO)
	def EditProjectCommandHandler handler
	def IRevisionService revisionService = Mock(IRevisionService)
	def Gate gate = Mock(Gate)
	
	def setup() {
		handler = new EditProjectCommandHandler(
				projectDAO : projectDAO,
				revisionService : revisionService,
				gate 					: gate
				)
	}
	
	def "it should edit a project"() {
		
		given:
		def project = new Project()
		project.id = "1"
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new EditProjectCommand(project)
		
		then:
		projectDAO.findByKey(project.key) >> project
		notThrown(Exception.class)
		1 * projectDAO.save(_)
	}
	
	def "it should not create two projects with the same key"() {
		
		given:
		def project = new Project()
		project.id = "1"
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new EditProjectCommand(project)
		
		then:
		projectDAO.findByKey(project.key) >> new Project(id: "2", key: project.key)
		0 * projectDAO.save(_)
		thrown(ProjectAlreadyExistException.class)
	}
	
	def "it should throw an exception if the project is null"() {
		
		given:
		def project = null
		
		when:
		handler.handle new EditProjectCommand(project)
		
		then:
		0 * projectDAO.save(_)
		thrown(MissingProjectInformationException.class)
	}
	
	def "it should throw an exception if the ID is not defined"() {
		
		given:
		def project = new Project()
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new EditProjectCommand(project)
		
		then:
		0 * projectDAO.save(_)
		thrown(InvalidProjectInformationException.class)
	}
}