/**
 *
 */
package com.tocea.corolla.users.handlers

import static org.mockito.Mockito.*

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.users.commands.EditUserCommand
import com.tocea.corolla.users.dao.IUserDAO
import com.tocea.corolla.users.domain.User
import com.tocea.corolla.users.exceptions.InvalidUserInformationException
import com.tocea.corolla.users.exceptions.MissingUserInformationException
import com.tocea.corolla.users.service.EmailValidationService
import com.tocea.corolla.utils.functests.FunctionalTestDoc

/**
 * @author sleroy
 *
 */
@FunctionalTestDoc(requirementName = "EDIT_USER")
class EditUserCommandHandlerTest extends Specification{

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()

	def IUserDAO userDao = Mock(IUserDAO)
	def emailValid = new EmailValidationService()

	def EditUserCommandHandler	handler

	def User validUser
	def User userWithoutRole
	def User userWithInvalidEmail
	def User userWithInvalidId


	def setup() {
		handler = new EditUserCommandHandler(
				userDAO : userDao,
				emailValidationService : emailValid
				)

		validUser = 		new User(
				id:1,
				activationToken:"",
				active:true,
				createdTime:null,
				email:"user@dummy.org",
				firstName:"John",
				lastName:"Doe",
				locale:"fr",
				login:"login",
				password:"superPassword",
				roleId:1)

		userWithInvalidId = 		new User(
				id:null,
				)
		userWithoutRole = 		new User(
				id:1,
				roleId:null)

		userWithInvalidEmail = 		new User(
				id:1,
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
	 * {@link com.tocea.corolla.users.handlers.EditUserCommandHandler#handle(com.tocea.corolla.users.commands.EditUserCommand)}
	 * .
	 */
	def "testvalid user creation"() {

		when:
		final EditUserCommand command = new EditUserCommand()


		command.setUser validUser

		this.handler.handle(command)
		then:
		1 * userDao.save(validUser)
	}

	/**
	 * Test method for
	 * {@link com.tocea.corolla.users.handlers.EditUserCommandHandler#handle(com.tocea.corolla.users.commands.EditUserCommand)}
	 * .
	 */
	def "test invalid user without role creation"() {
		when:
		final EditUserCommand command = new EditUserCommand()


		command.setUser userWithoutRole

		this.handler.handle(command)
		then:
		0 * userDao.save(validUser)
		thrown(InvalidUserInformationException)
	}


	def "test user with invalid email"() {
		when:
		final EditUserCommand command = new EditUserCommand()


		command.setUser userWithInvalidEmail

		this.handler.handle(command)
		then:
		0 * userDao.save(validUser)
		thrown(InvalidUserInformationException)
	}

	def "test user with invalid id"() {
		when:
		final EditUserCommand command = new EditUserCommand()


		command.setUser userWithInvalidId

		this.handler.handle(command)
		then:
		0 * userDao.save(validUser)
		thrown(InvalidUserInformationException)
	}


	def "test creation with missing user"() {
		when:
		final EditUserCommand command = new EditUserCommand()

		this.handler.handle(command)

		then:
		thrown(MissingUserInformationException.class)
	}
}
