/**
 *
 */
package com.tocea.corolla.ui.views.profile

import groovy.util.logging.Slf4j

import java.security.Principal

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.users.commands.EditUserCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.dao.IUserDAO
import com.tocea.corolla.users.domain.User
import com.tocea.corolla.users.dto.UserProfileDto
import com.tocea.corolla.users.exceptions.InvalidLoginException
import com.tocea.corolla.users.service.UserDtoService;
import com.tocea.corolla.users.validation.UserValidation

/**
 * @author sleroy
 *
 */
@Controller
@Slf4j
public class UserViewProfileController {

	def static final String PROFILE_PAGE = "profiles/user_profile"

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

	@RequestMapping("/ui/edit_profile")
	public ModelAndView getAddPage(Principal _principal) {
		def login = _principal.name
		if (!this.validation.isValidLogin(login)) {
			throw new InvalidLoginException()
		}
		def ModelAndView model = new ModelAndView(PROFILE_PAGE)
		def user = userDAO.findUserByLogin(login)
		if (user == null) {
			log.error("User not found {}", login)
		}
		model.addObject "user", user == null ? new UserProfileDto() : new UserProfileDto(user)
		return model
	}



	@RequestMapping(value="/ui/edit_profile", method = RequestMethod.POST)
	public ModelAndView modifyUser(@Valid @ModelAttribute("user") UserProfileDto _userProfile, BindingResult _result, Principal _principal) {
		def login = _principal.name
		if (!login.equals(_userProfile.login)) {
			_result.addError( new ObjectError("login", "Login is invalid"))
		}
		def User user = userDAO.findUserByLogin(login)
		if (user == null) {
			_result.addError( new ObjectError("login", "User not found"))
		}

		if (_result.hasErrors()) {
			def ModelAndView model2 = new ModelAndView(PROFILE_PAGE)
			return model2
		}
		EditUserCommand command = new EditUserCommand(user, _userProfile)
		gate.dispatch(command)

		return new ModelAndView("redirect:/")
	}
}
