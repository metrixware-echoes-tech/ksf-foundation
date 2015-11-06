/**
 *
 */
package com.tocea.corolla.users.handlers

import static org.mockito.Mockito.*

import org.junit.Rule

import spock.lang.Specification

import com.google.common.collect.Lists;
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.users.commands.DeleteRoleCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.exceptions.RoleOperationForbiddenException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc

/**
 * @author sleroy
 *
 */
@FunctionalTestDoc(requirementName = "DELETE_ROLE")
class DeleteRoleCommandHandlerTest extends Specification{

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()

	def IRoleDAO roleDao = Mock(IRoleDAO)
	
	def IUserDAO userDao = Mock(IUserDAO)

	def DeleteRoleCommandHandler	handler

	def Role validRole
	
	def Role defaultRole
	
	def Role protectedRole


	def setup() {
		
		handler = new DeleteRoleCommandHandler(roleDAO : roleDao, userDAO : userDao)
		validRole = new Role(id:'1', name:"admin", note:"Admin role", permissions:"",defaultRole:false)
		defaultRole = new Role(id:'2', name:"default", note:"Default Role", permissions:"", defaultRole:true)
		protectedRole = new Role(id:'3', name:"protected", note:"Protected Role", permissions:"", defaultRole:false, roleProtected:true)
		
		roleDao.getDefaultRole() >> defaultRole
	}


	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.CreateRoleCommandHandler#handle(com.tocea.corolla.users.commands.CreateRoleCommand)}
	 * .
	 */
	def "it should delete an existing role"() {
		
		given:
			roleDao.findOne('1') >> validRole
			userDao.findByRoleId('1') >> Lists.newArrayList()
		
		when:
			final DeleteRoleCommand command = new DeleteRoleCommand(roleID : '1')
			this.handler.handle command

		then:
			1 * roleDao.delete(_)
			
	}

	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.AddNewRoleCommandHandler#handle(com.tocea.corolla.users.commands.AddNewRoleCommand)}
	 * .
	 */
	def "it should not delete an unknown role"() {
		
		given:
		roleDao.findOne('1') >> null
		
		when:
		final DeleteRoleCommand command = new DeleteRoleCommand(roleID : '1')

		this.handler.handle command

		then:
		0 * roleDao.delete(_)
	}
	
	def "it should not delete a protected role"() {
		
		given:
			roleDao.findOne('3') >> protectedRole
		
		when:
			final DeleteRoleCommand command = new DeleteRoleCommand(roleID : '3')
			this.handler.handle command
		
		then:
			thrown RoleOperationForbiddenException
		
	}
	
	def "it should not delete the default role"() {
		
		given:
			roleDao.findOne('2') >> defaultRole
		
		when:
			final DeleteRoleCommand command = new DeleteRoleCommand(roleID : '2')
			this.handler.handle command
		
		then:
			thrown RoleOperationForbiddenException
		
	}
	
	def "it should change the role of the users assigned to the deleted role"() {
		
		given:
			roleDao.findOne('1') >> validRole
			def users = [Mock(User), Mock(User)]
			userDao.findByRoleId('1') >> users
		
		when:
			final DeleteRoleCommand command = new DeleteRoleCommand(roleID : '1')
			this.handler.handle command
		
		then:
			users.each() { it.role = defaultRole }
			1 * userDao.save(users)
			
	}
}
