package com.tocea.corolla.users.handlers

import static org.mockito.Mockito.*

import org.junit.Rule;
import org.mockito.Matchers;
import org.mockito.Mockito;

import spock.lang.Specification

import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.users.commands.DeleteUserGroupCommand;
import com.tocea.corolla.users.dao.IUserGroupDAO;
import com.tocea.corolla.users.domain.UserGroup
import com.tocea.corolla.users.exceptions.InvalidUserGroupInformationException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "DELETE_USER_GROUP")
class DeleteUserGroupCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IUserGroupDAO groupDao = Mockito.mock(IUserGroupDAO)
	def DeleteUserGroupCommandHandler handler
	
	def setup() {
		handler = new DeleteUserGroupCommandHandler(groupDAO : groupDao)
	}
	
	def "it should delete an user group"() {
		
		given:
			UserGroup group = new UserGroup()
			group.id = "5"
			group.name = "devs"
			group.userIds = ["scarreau", "afloch"]
					
			when(groupDao.findOne(group.id)).thenReturn(group)
				
		when:
			final DeleteUserGroupCommand command = new DeleteUserGroupCommand(group.id)
			this.handler.handle(command)
		
			Mockito.verify(groupDao).delete(Matchers.any())
		
		then:	
			notThrown(Exception.class)
		
	}
	
	def "it should throw an exception if the ID is not defined"() {
		
		given:
			def groupID = null
				
		when:
			final DeleteUserGroupCommand command = new DeleteUserGroupCommand(groupID)
			this.handler.handle(command)
		
			Mockito.verify(groupDao, times(0)).delete(Matchers.any())
		
		then:	
			thrown(InvalidUserGroupInformationException.class)
		
	}
	
	def "it should throw an exception if the ID is not associated to an user group"() {
		
		given:
			def groupID = "5"
			
			when(groupDao.findOne(groupID)).thenReturn(null)
				
		when:
			final DeleteUserGroupCommand command = new DeleteUserGroupCommand(groupID)
			this.handler.handle(command)
		
			Mockito.verify(groupDao, times(0)).delete(Matchers.any())
		
		then:	
			thrown(InvalidUserGroupInformationException.class)
		
	}
	
}