package com.tocea.corolla.products.commands.handlers

import org.junit.Rule;
import org.mockito.Mockito;

import spock.lang.Specification;

import com.tocea.corolla.products.commands.RestoreProjectStateCommand
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.requirements.commands.handlers.RestoreProjectStateCommandHandler;
import com.tocea.corolla.revisions.exceptions.InvalidCommitInformationException;
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "RESTORE_PROJECT_STATE")
class RestoreProjectStateCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectDAO projectDAO = Mock(IProjectDAO)	
	def RestoreProjectStateCommandHandler handler
	def IRevisionService revisionService = Mock(IRevisionService)
	
	def setup() {
		handler = new RestoreProjectStateCommandHandler(
				projectDAO : projectDAO,
				revisionService : revisionService
		)
	}
	
	def "it should restore a project to a previous state"() {
		
		given:
			def project = new Project(
				id: "35",
				key: "my_project",
				name: "My Awesome Project"
			)
			def previousState = new Project(
				id: "35",
				key: "my_old_project",
				name: "My Not Awesome Project"
			)
			def commitID = "1"
		
		when:
			handler.handle new RestoreProjectStateCommand(project, commitID)
	
		then:
			notThrown(Exception.class)
			
		then:
			revisionService.getSnapshot(project.id, Project.class, commitID) >> previousState
			
		then:
			1 * projectDAO.save(previousState)
			1 * revisionService.commit(previousState)
			
	}
	
	
	def "it should throw an exception if the given commit ID doesn't match a previous state"() {
		
		given:
			def project = new Project(
				id: "35",
				key: "my_project",
				name: "My Awesome Project"
			)
			def previousState = null
			def commitID = "1"
			
		when:
			handler.handle new RestoreProjectStateCommand(project, commitID)
		
		then:
			revisionService.getSnapshot(project.id, Project.class, commitID) >> previousState
		
		then:
			thrown(InvalidCommitInformationException.class)
				
	}
	
}