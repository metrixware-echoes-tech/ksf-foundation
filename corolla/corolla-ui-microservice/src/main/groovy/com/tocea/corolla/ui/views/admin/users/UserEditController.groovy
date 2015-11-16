/**
 *
 */
package com.tocea.corolla.ui.views.admin.users

import groovy.util.logging.Slf4j

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

import com.google.common.collect.Lists
import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.users.commands.CreateUserCommand
import com.tocea.corolla.users.commands.EditUserCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.dao.IUserDAO
import com.tocea.corolla.users.domain.Permission
import com.tocea.corolla.users.domain.Role
import com.tocea.corolla.users.dto.UserPasswordDto
import com.tocea.corolla.users.exceptions.InvalidLoginException
import com.tocea.corolla.users.service.UserDtoService
import com.tocea.corolla.users.validation.UserValidation

/**
 * @author sleroy
 *
 */
@Secured([
	Permission.ADMIN,
	Permission.ADMIN_USERS
])
@RequestMapping("/ui/admin/users/")
@Controller
@Slf4j
public class UserEditController {

	def static final String ADMIN_USERS_EDIT = "admin/users_edit"

	@Autowired
	private UserDtoService userDTODAO

	@Autowired
	private IRoleDAO roleDAO

	@Autowired
	private IUserDAO userDAO

	@Autowired
	private UserValidation validation

	@Autowired
	private Gate gate

	@ModelAttribute("menu")
	public String setMenu() {
		return "admin"
	}

	@ModelAttribute("roles")
	public List<Role> addRoles() {
		Lists.newArrayList roleDAO.findAll()
	}

	@RequestMapping("/add")
	public ModelAndView getAddPage(@ModelAttribute UserPasswordDto user) {

		if (user.roleId == null) {
			user.roleId = roleDAO.getDefaultRole().getId()
		}

		def ModelAndView model = new ModelAndView(ADMIN_USERS_EDIT)
		model.addObject "user", user

		return model
	}

	@RequestMapping(value="/add", method = RequestMethod.POST)
	public ModelAndView saveNewUser(@Valid @ModelAttribute("user") UserPasswordDto _passwordDto, BindingResult _result) {

		if (_result.hasErrors()) {
			def ModelAndView model2 = new ModelAndView(ADMIN_USERS_EDIT)
			model2.addObject("user", _passwordDto)
			return model2
		}
		_passwordDto.setCreatedTime(new Date())
		CreateUserCommand command = new CreateUserCommand(_passwordDto)
		gate.dispatchAsync(command)

		return new ModelAndView("redirect:/ui/admin/users")
	}

	@RequestMapping("/edit/{login}")
	public ModelAndView getAddPage(@PathVariable String login) {
		if (!this.validation.isValidLogin(login)) {
			throw new InvalidLoginException()
		}
		def ModelAndView model = new ModelAndView(ADMIN_USERS_EDIT)
		def user = userDAO.findUserByLogin(login)
		if (user == null) {
			log.error("User not found {}", login)
		}
		model.addObject "user", user == null ? new UserPasswordDto() : new UserPasswordDto(user)
		return model
	}

	@RequestMapping(value="/edit/{login}", method = RequestMethod.POST)
	public ModelAndView modifyUser(@Valid @ModelAttribute("user") UserPasswordDto _passwordDto, BindingResult _result, @PathVariable String login) {
		if (!login.equals(_passwordDto.login)) {
			_result.addError( new ObjectError("login", "Login is invalid"))
		}
		if (!_passwordDto.hasId() && userDAO.findUserByLogin(_passwordDto.getLogin()) != null) {
			_result.addError( new ObjectError("login", "Login already exists"))
		}

		if (_result.hasErrors()) {
			def ModelAndView model2 = new ModelAndView(ADMIN_USERS_EDIT)
			model2.addObject("user", _passwordDto)
			return model2
		}
		_passwordDto.setCreatedTime(new Date())
		EditUserCommand command = new EditUserCommand(_passwordDto)
		gate.dispatchAsync(command)

		return new ModelAndView("redirect:/ui/admin/users")
	}

}