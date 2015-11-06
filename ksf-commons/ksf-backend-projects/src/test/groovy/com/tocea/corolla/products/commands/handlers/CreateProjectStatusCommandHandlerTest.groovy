package com.tocea.corolla.products.commands.handlers

import org.junit.Rule;
import org.mockito.Mockito;

import spock.lang.Specification;

import com.tocea.corolla.products.commands.CreateProjectStatusCommand
import com.tocea.corolla.products.dao.IProjectStatusDAO;
import com.tocea.corolla.products.domain.ProjectStatus
import com.tocea.corolla.products.exceptions.*;
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "ADD_PROJECT_STATUS")
class CreateProjectStatusCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectStatusDAO statusDAO = Mock(IProjectStatusDAO)	
	def CreateProjectStatusCommandHandler handler
	
	def setup() {
		handler = new CreateProjectStatusCommandHandler(
				statusDAO : statusDAO
		)
	}
	
	def "it should create a new project status"() {
		
		given:
			def status = new ProjectStatus()
			status.name = "Active"
		
		when:
			handler.handle new CreateProjectStatusCommand(status)
	
		then:
			statusDAO.findByName(status.name) >> null
			1 * statusDAO.save(_)
			notThrown(Exception.class)
	}
	
	def "it should not create two project statuses with the same key"() {
		
		given:
			def status = new ProjectStatus()
			status.name = "Active"
		
		when:
			handler.handle new CreateProjectStatusCommand(status)
	
		then:
			statusDAO.findByName(status.name) >> new ProjectStatus(name: status.name)
			0 * statusDAO.save(_)
			thrown(ProjectStatusAlreadyExistException.class)
		
	}
	
	def "it should throw an exception if the project status is null"() {
		
		given:
			def status = null
		
		when:
			handler.handle new CreateProjectStatusCommand(status)
	
		then:
			0 * statusDAO.save(_)
			thrown(MissingProjectStatusInformationException.class)
			
	}
	
	def "it should throw an exception if the ID is already defined"() {
		
		given:
			def status = new ProjectStatus()
			status.id = "1"
			status.name = "Active"
		
		when:
			handler.handle new CreateProjectStatusCommand(status)
	
		then:
			0 * statusDAO.save(_)
			thrown(InvalidProjectStatusInformationException.class)
			
	}
	
}