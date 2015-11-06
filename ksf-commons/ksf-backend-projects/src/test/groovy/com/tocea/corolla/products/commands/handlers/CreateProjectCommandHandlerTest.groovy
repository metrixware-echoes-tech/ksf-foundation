package com.tocea.corolla.products.commands.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.commands.CreateProjectBranchCommand
import com.tocea.corolla.products.commands.CreateProjectCommand
import com.tocea.corolla.products.dao.IProjectDAO
import com.tocea.corolla.products.dao.IProjectStatusDAO
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.products.domain.ProjectStatus
import com.tocea.corolla.products.exceptions.InvalidProjectInformationException
import com.tocea.corolla.products.exceptions.MissingProjectInformationException
import com.tocea.corolla.products.exceptions.ProjectAlreadyExistException
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "ADD_PROJECT")
class CreateProjectCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectDAO projectDAO = Mock(IProjectDAO)
	def IProjectStatusDAO statusDAO = Mock(IProjectStatusDAO)
	def CreateProjectCommandHandler handler
	def IRevisionService revisionService = Mock(IRevisionService)
	def Gate gate = Mock(Gate)
	
	def setup() {
		handler = new CreateProjectCommandHandler(
				projectDAO : projectDAO,
				statusDAO : statusDAO,
				revisionService : revisionService,
				gate : gate
				)
	}
	
	def "it should create a new project"() {
		
		given:
		def project = new Project()
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new CreateProjectCommand(project)
		
		then:
		notThrown(Exception.class)
		
		then:
		projectDAO.findByKey(project.key) >> null
		1 * projectDAO.save(_)
	}
	
	def "it should call the revision service after creating a new project"() {
		
		given:
		def project = new Project()
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new CreateProjectCommand(project)
		
		then:
		notThrown(Exception.class)
		
		then:
		projectDAO.findByKey(project.key) >> null
		1 * revisionService.commit(project)
	}
	
	def "it should create a default branch after creating a new project"() {
		
		given:
		def project = new Project()
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new CreateProjectCommand(project)
		
		then:
		notThrown(Exception.class)
		
		then:
		projectDAO.findByKey(project.key) >> null
		
		then:
		1 * gate.dispatch({
			it instanceof CreateProjectBranchCommand && it.branch.name //&& it.branch.projectId
		})
	}
	
	def "it should use the default status if the status of the project is not defined"() {
		
		given:
		def project = new Project()
		project.key = "my_project"
		project.name = "My Awesome Project"
		def defaultStatus =  new ProjectStatus(id: "55", name: "Active")
		
		when:
		handler.handle new CreateProjectCommand(project)
		
		then:
		notThrown(Exception.class)
		
		then:
		statusDAO.getDefaultStatus() >> defaultStatus
		
		then:
		1 * projectDAO.save { it.statusId == defaultStatus.id }
	}
	
	def "it should not create two projects with the same key"() {
		
		given:
		def project = new Project()
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new CreateProjectCommand(project)
		
		then:
		projectDAO.findByKey(project.key) >> new Project(key: project.key)
		
		then:
		0 * projectDAO.save(_)
		thrown(ProjectAlreadyExistException.class)
	}
	
	def "it should throw an exception if the project is null"() {
		
		given:
		def project = null
		
		when:
		handler.handle new CreateProjectCommand(project)
		
		then:
		0 * projectDAO.save(_)
		thrown(MissingProjectInformationException.class)
	}
	
	def "it should throw an exception if the ID is already defined"() {
		
		given:
		def project = new Project()
		project.id = "1"
		project.key = "my_project"
		project.name = "My Awesome Project"
		
		when:
		handler.handle new CreateProjectCommand(project)
		
		then:
		0 * projectDAO.save(_)
		thrown(InvalidProjectInformationException.class)
	}
	
	//	def "it should dispatch a command to insert a new node in the portfolio"() {
	//
	//		given:
	//			def project = new Project(key: "my_project", name:"My Project")
	//			def parentNodeID = 1
	//
	//		when:
	//			handler.handle new CreateProjectCommand(project, parentNodeID)
	//
	//		then:
	//			notThrown(Exception.class)
	//
	//		then:
	//			1 * gate.dispatch {
	//				it instanceof CreateProjectNodeCommand && it.projectID == project.id && it.parentID == parentNodeID
	//			}
	//
	//	}
	
}