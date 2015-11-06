/**
 *
 */
package com.tocea.corolla.users.handlers

import static org.mockito.Mockito.*

import org.javers.core.Javers;
import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.users.commands.EditRoleCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.exceptions.InvalidRoleInformationException
import com.tocea.corolla.users.exceptions.MissingRoleInformationException
import com.tocea.corolla.users.exceptions.RoleOperationForbiddenException;
import com.tocea.corolla.users.service.RolePermissionService
import com.tocea.corolla.utils.functests.FunctionalTestDoc

/**
 * @author sleroy
 *
 */
@FunctionalTestDoc(requirementName = "EDIT_ROLE")
class EditRoleCommandHandlerTest extends Specification{

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IRoleDAO roleDao = Mock(IRoleDAO)
	def RolePermissionService rolePermissionDao = Mock(RolePermissionService)

	def EditRoleCommandHandler	handler

	def Role validRole

	def setup() {
		handler = new EditRoleCommandHandler(
				roleDAO : roleDao,
				rolePermissionService : rolePermissionDao
		)
		validRole = new Role(id:1,name:"admin", note:"Admin role", permissions:"",defaultRole:true )
	}

	def "testvalid role edition"() {

		when:
			final EditRoleCommand command = new EditRoleCommand(validRole)
			this.handler.handle command

		then:
			roleDao.findOne(validRole.id) >> new Role()
			1 * roleDao.save(_)
			notThrown(Exception.class)
			
	}

	def "testvalid missing role creation"() {

		when:
			final EditRoleCommand command = new EditRoleCommand()
			this.handler.handle command

		then:
			0 * roleDao.save(_)
			thrown MissingRoleInformationException
	}

	def "testvalid role with id creation"() {

		when:
			final EditRoleCommand command = new EditRoleCommand(new Role(id:null))
			this.handler.handle command

		then:
			0 * roleDao.save(_)
			thrown InvalidRoleInformationException
	}
	
	def "it should set the role as protected"() {
		
		given:
			validRole.setRoleProtected(true)
		
		when:
			EditRoleCommand command = new EditRoleCommand(validRole)
			this.handler.handle command		
		
		then:
			roleDao.findOne(validRole.id) >> new Role(id: 1, roleProtected: false)
			1 * roleDao.save(_)
			notThrown(Exception.class)
	}
	
	def "it should not edit a protected role"() {
		
		given:
			validRole.setRoleProtected(true)
		
		when:
			EditRoleCommand command = new EditRoleCommand(validRole)
			this.handler.handle command		
		
		then:
			roleDao.findOne(validRole.id) >> new Role(id: 1, roleProtected: true)
			0 * roleDao.save(_)
			thrown(RoleOperationForbiddenException.class)
		
	}
}
