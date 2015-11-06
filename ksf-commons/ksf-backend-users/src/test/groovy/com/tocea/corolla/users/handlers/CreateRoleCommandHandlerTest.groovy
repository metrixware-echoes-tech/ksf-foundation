/**
 *
 */
package com.tocea.corolla.users.handlers

import static org.mockito.Mockito.*

import org.javers.core.Javers
import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.users.commands.CreateRoleCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.exceptions.InvalidRoleInformationException
import com.tocea.corolla.users.exceptions.MissingRoleInformationException
import com.tocea.corolla.users.service.RolePermissionService
import com.tocea.corolla.utils.functests.FunctionalTestDoc

/**
 * @author sleroy
 *
 */
@FunctionalTestDoc(requirementName = "CREATE_NEW_ROLE")
class CreateRoleCommandHandlerTest extends Specification{

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IRoleDAO roleDao = Mock(IRoleDAO)
	def RolePermissionService rolePermissionDao = Mock(RolePermissionService)

	def CreateRoleCommandHandler	handler

	def Role validRole
	def Role defaultRole

	def setup() {
		handler = new CreateRoleCommandHandler(
				roleDAO : roleDao,
				rolePermissionService : rolePermissionDao
		)
		validRole = new Role(name:"admin", note:"Admin role", permissions:"",defaultRole:true )
		defaultRole = new Role(id:'1', name:'Guest', note:'Guest role', permissions:"ALL")
	}

	def "testvalid role creation"() {

		when:
		final CreateRoleCommand command = new CreateRoleCommand(validRole)

		this.handler.handle command

		then:
		1 * roleDao.save(_)
		notThrown(Exception.class)
	}

	def "testvalid missing role creation"() {

		when:
		final CreateRoleCommand command = new CreateRoleCommand()

		this.handler.handle command

		then:
		0 * roleDao.save(_)
		thrown MissingRoleInformationException
	}

	def "testvalid role with id creation"() {

		when:
		final CreateRoleCommand command = new CreateRoleCommand(new Role(id:'1'))

		this.handler.handle command

		then:
		0 * roleDao.save(_)
		thrown InvalidRoleInformationException
	}
}
