/**
 *
 */
package com.tocea.corolla.users.handlers

import static org.mockito.Mockito.*

import org.javers.core.Javers;
import org.junit.Rule
import org.mockito.Mockito
import org.springframework.security.crypto.password.PasswordEncoder

import spock.lang.Specification

import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.users.commands.CreateUserCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.dao.IUserDAO
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.domain.User
import com.tocea.corolla.users.exceptions.AlreadyExistingUserWithLoginException
import com.tocea.corolla.users.exceptions.InvalidEmailAddressException
import com.tocea.corolla.users.exceptions.InvalidUserInformationException
import com.tocea.corolla.users.exceptions.MissingUserInformationException
import com.tocea.corolla.users.exceptions.RoleManagementBrokenException
import com.tocea.corolla.users.service.EmailValidationService
import com.tocea.corolla.utils.functests.FunctionalTestDoc

/**
 * @author sleroy
 *
 */
@FunctionalTestDoc(requirementName = "ADD_USER")
class CreateUserCommandHandlerTest extends Specification{

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IUserDAO userDao = Mockito.mock(IUserDAO)
	def IRoleDAO roleDao = Mockito.mock(IRoleDAO)
	def emailValid = new EmailValidationService()
	def defaultRole = new Role(id:1, name:'Guest', note:'Guest role', permissions:"ALL")
	def PasswordEncoder passwordEncoder = Mock(PasswordEncoder)
	def CreateUserCommandHandler	handler

	def User validUser
	def User validUserWithoutRole
	def User userWithInvalidEmail


	def setup() {
		handler = new CreateUserCommandHandler(
				userDAO : userDao,
				roleDAO : roleDao,				
				emailValidationService : emailValid,
				passwordEncoder : passwordEncoder
		)
		validUser = 		new User(
				activationToken:"",
				active:true,
				createdTime:null,
				email:"user@dummy.org",
				firstName:"John",
				lastName:"Doe",
				locale:"fr",
				login:"login",
				password:"superPassword",
				roleId:"1")

		validUserWithoutRole = 		new User(
				activationToken:"",
				active:true,
				createdTime:null,

				email:"user@dummy.org",
				firstName:"John",
				lastName:"Doe",
				locale:"fr",
				login:"login",
				password:"superPassword",
				roleId:null)

		userWithInvalidEmail = 		new User(
				activationToken:"",
				active:true,
				createdTime:null,

				email:"user",
				firstName:"John",
				lastName:"Doe",
				locale:"fr",
				login:"login",
				password:"superPassword",
				roleId:null)
	}


	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.AddNewUserCommandHandler#handle(com.tocea.corolla.users.commands.AddNewUserCommand)}
	 * .
	 */
	def "testvalid user creation"() {

		given:
			final CreateUserCommand command = new CreateUserCommand()
			command.setUser validUser
		
		when:
			this.handler.handle(command)
			Mockito.verify(roleDao, times(0)).getDefaultRole()
			Mockito.verify(userDao, times(1)).save validUser
		
		then:
			notThrown(Exception.class)
	}

	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.AddNewUserCommandHandler#handle(com.tocea.corolla.users.commands.AddNewUserCommand)}
	 * .
	 */
	def "test valid user without role creation"() {
		given:
		when(roleDao.getDefaultRole()).thenReturn defaultRole
		when:
		final CreateUserCommand command = new CreateUserCommand()

		command.setUser validUserWithoutRole
		this.handler.handle(command)

		Mockito.verify(roleDao, times(1)).getDefaultRole()
		Mockito.verify(userDao, times(1)).save validUserWithoutRole
		then:
		notThrown(Exception.class)
	}


	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.AddNewUserCommandHandler#handle(com.tocea.corolla.users.commands.AddNewUserCommand)}
	 * .
	 */
	def "test user with invalid email"() {
		given:
		when(roleDao.getDefaultRole()).thenReturn defaultRole
		when:
		final CreateUserCommand command = new CreateUserCommand()

		command.setUser userWithInvalidEmail
		this.handler.handle(command)

		Mockito.verify(roleDao, times(1)).getDefaultRole()
		Mockito.verify(userDao, times(1)).save userWithInvalidEmail
		then:
		thrown(InvalidEmailAddressException.class)
	}


	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.AddNewUserCommandHandler#handle(com.tocea.corolla.users.commands.AddNewUserCommand)}
	 * .
	 */
	def "test creation with missing user"() {
		when:
		final CreateUserCommand command = new CreateUserCommand()

		this.handler.handle(command)

		then:
		thrown(MissingUserInformationException.class)
	}


	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.AddNewUserCommandHandler#handle(com.tocea.corolla.users.commands.AddNewUserCommand)}
	 * .
	 */
	def "test valid user with missing role and no role available"() {
		
		given:
			when(roleDao.getDefaultRole()).thenReturn null
			when(roleDao.save(Role.DEFAULT_ROLE)).thenReturn(Role.DEFAULT_ROLE)
		
		when:
			final CreateUserCommand command = new CreateUserCommand()
			command.setUser validUserWithoutRole
			this.handler.handle(command)

		then:
			notThrown(Exception)

	}


	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.AddNewUserCommandHandler#handle(com.tocea.corolla.users.commands.AddNewUserCommand)}
	 * .
	 */
	def "testvalid user with already existing login "() {
		given:
		when(userDao.findUserByLogin('login')).thenReturn( validUser)
		when:
		final CreateUserCommand command = new CreateUserCommand()


		command.setUser validUser

		this.handler.handle(command)
		then:
		thrown(AlreadyExistingUserWithLoginException.class)
	}


	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.AddNewUserCommandHandler#handle(com.tocea.corolla.users.commands.AddNewUserCommand)}
	 * .
	 */
	def "test user with primary key"() {
		given:
		def primaryKeyUser = new User(id:"1")

		when:
		final CreateUserCommand command = new CreateUserCommand()


		command.setUser primaryKeyUser

		this.handler.handle(command)
		then:
		thrown(InvalidUserInformationException)
	}
}
