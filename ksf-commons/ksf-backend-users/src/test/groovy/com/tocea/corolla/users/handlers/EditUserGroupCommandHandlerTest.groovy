package com.tocea.corolla.users.handlers

import static org.mockito.Mockito.*

import org.junit.Rule
import org.mockito.Mockito

import spock.lang.Specification;

import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.users.commands.EditUserGroupCommand;
import com.tocea.corolla.users.dao.IUserGroupDAO;
import com.tocea.corolla.users.domain.UserGroup;
import com.tocea.corolla.users.exceptions.InvalidUserGroupInformationException;
import com.tocea.corolla.users.exceptions.MissingUserGroupInformationException;
import com.tocea.corolla.users.exceptions.UserGroupAlreadyExistException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "EDIT_USER_GROUP")
class EditUserGroupCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IUserGroupDAO groupDao = Mockito.mock(IUserGroupDAO)
	def EditUserGroupCommandHandler handler
	
	def setup() {
		handler = new EditUserGroupCommandHandler(groupDAO : groupDao)
	}
	
	def "it should change the user group data"() {
		
		given:
			UserGroup group = new UserGroup()
			group.id = "15"
			group.name = "devs"
			group.userIds = ["scarreau", "afloch"]
			when(groupDao.findByName(group.name)).thenReturn(group)
					
		when:
			final EditUserGroupCommand command = new EditUserGroupCommand()
			command.setUserGroup group
			this.handler.handle(command)
			
			Mockito.verify(groupDao).save group
			
		then:
			notThrown(Exception.class)
		
	}
	
	def "it should throw an exception if the user group ID is not defined"() {
		
		given:
			UserGroup group = new UserGroup()
			group.name = "devs"
			group.userIds = ["scarreau", "afloch"]
					
		when:
			final EditUserGroupCommand command = new EditUserGroupCommand()
			command.setUserGroup group
			this.handler.handle(command)
			
		then:
			thrown(InvalidUserGroupInformationException.class)	
		
	}
	
	def "it should throw an exception if the user group is not defined"() {
		
		given:
			UserGroup group = null
					
		when:
			final EditUserGroupCommand command = new EditUserGroupCommand()
			command.setUserGroup group
			this.handler.handle(command)
			
		then:
			thrown(MissingUserGroupInformationException.class)	
		
	}
	
	def "it should not have multiple user groups with the same name"() {
		
		given:
			def group = new UserGroup()
			group.id = "5"
			group.name = "devs"
			group.userIds = ["scarreau", "afloch"]
					
			def group2 = new UserGroup()
			group2.id = "6"
			group2.name = "testers"
			when(groupDao.findByName(group.name)).thenReturn(group2)
		
		when:
			final EditUserGroupCommand command = new EditUserGroupCommand()
			command.setUserGroup group
			this.handler.handle(command)
			
		then:
			thrown(UserGroupAlreadyExistException.class)
		
	}
	
}