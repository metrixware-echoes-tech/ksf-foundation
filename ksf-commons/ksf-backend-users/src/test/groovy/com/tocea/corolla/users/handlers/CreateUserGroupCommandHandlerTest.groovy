package com.tocea.corolla.users.handlers

import static org.mockito.Mockito.*

import org.junit.Rule
import org.mockito.Mockito

import spock.lang.Specification;

import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.users.commands.CreateUserGroupCommand;
import com.tocea.corolla.users.dao.IUserGroupDAO
import com.tocea.corolla.users.domain.UserGroup
import com.tocea.corolla.users.exceptions.InvalidUserGroupInformationException;
import com.tocea.corolla.users.exceptions.MissingUserGroupInformationException;
import com.tocea.corolla.users.exceptions.UserGroupAlreadyExistException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc


@FunctionalTestDoc(requirementName = "ADD_USER_GROUP")
class CreateUserGroupCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IUserGroupDAO groupDao = Mockito.mock(IUserGroupDAO)
	def CreateUserGroupCommandHandler handler
	
	def setup() {
		handler = new CreateUserGroupCommandHandler(groupDAO : groupDao)
	}
	
	def "it should create an user group"() {
		
		given:
		def validGroup = new UserGroup()
		validGroup.name = "devs"
		validGroup.userIds = ["scarreau", "afloch"]
				
		when:
		final CreateUserGroupCommand command = new CreateUserGroupCommand()
		command.setUserGroup validGroup
		this.handler.handle(command)
		
		Mockito.verify(groupDao).save validGroup
		
		then:	
		notThrown(Exception.class)
		
	}
	
	def "it should not create an user group if the ID is already set"() {
		
		given:
		def group = new UserGroup()
		group.id = "1"
		group.name = "devs"
		group.userIds = ["scarreau", "afloch"]
				
		when:
		final CreateUserGroupCommand command = new CreateUserGroupCommand()
		command.setUserGroup group
		this.handler.handle(command)
		
		then:
		thrown(InvalidUserGroupInformationException.class)
		
	}
	
	def "it should throw an exception if the group is null"() {
		
		given:
		def group = null
				
		when:
		final CreateUserGroupCommand command = new CreateUserGroupCommand()
		command.setUserGroup group
		this.handler.handle(command)
		
		then:
		thrown(MissingUserGroupInformationException.class)
		
	}
	
	def "it should not create multiple user groups with the same name"() {
		
		given:
			def group = new UserGroup()
			group.name = "devs"
			group.userIds = ["scarreau", "afloch"]
			when(groupDao.findByName(group.name)).thenReturn(new UserGroup())
		
		when:
			final CreateUserGroupCommand command = new CreateUserGroupCommand()
			command.setUserGroup group
			this.handler.handle(command)
			
		then:
			thrown(UserGroupAlreadyExistException.class)
		
	}
	
}