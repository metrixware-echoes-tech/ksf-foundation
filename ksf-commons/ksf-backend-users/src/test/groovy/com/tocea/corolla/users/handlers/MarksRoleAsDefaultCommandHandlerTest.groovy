/**
 *
 */
package com.tocea.corolla.users.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.users.commands.MarksRoleAsDefaultCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.exceptions.InvalidRoleInformationException
import com.tocea.corolla.utils.functests.FunctionalTestDoc


/**
 * @author sleroy
 *
 */
@FunctionalTestDoc(requirementName = "SET_ROLE_DEFAULT")
class MarksRoleAsDefaultCommandHandlerTest extends Specification{
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IRoleDAO roleDao = Mock(IRoleDAO)
	def MarksRoleAsDefaultCommandHandler handler



	def Role nonDefaultRole
	def Role defaultRole

	def setup() {
		handler = new MarksRoleAsDefaultCommandHandler(
				roleDAO : roleDao,
				)
		nonDefaultRole = new Role(id:'1',name:"adminRole", note:"Admin role", permissions:"",defaultRole:false)
		defaultRole = new Role(id:'2',name:"defaultRole", note:"Default role", permissions:"",defaultRole:true)
	}

	def "test non-defaut-role-becomes-default"() {

		when:
		final MarksRoleAsDefaultCommand command = new MarksRoleAsDefaultCommand(nonDefaultRole.id)
		roleDao.findOne('1') >> nonDefaultRole
		roleDao.exists('1') >> true
		roleDao.findAll() >> [nonDefaultRole, defaultRole]

		this.handler.handle command

		then:
		1 * roleDao.save(nonDefaultRole)
		1 * roleDao.save(defaultRole)
	}

	def "test defaut-role-becomes-default-again"() {

		when:
		final MarksRoleAsDefaultCommand command = new MarksRoleAsDefaultCommand(defaultRole.id)
		roleDao.findOne('2') >> defaultRole
		roleDao.exists('2') >> true
		roleDao.findAll() >> [nonDefaultRole, defaultRole]

		this.handler.handle command

		then:
		2 * roleDao.save(defaultRole)
	}
}


