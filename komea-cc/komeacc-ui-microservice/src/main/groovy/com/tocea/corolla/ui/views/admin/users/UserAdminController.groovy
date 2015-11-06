/**
 *
 */
package com.tocea.corolla.ui.views.admin.users

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.dao.IUserDAO
import com.tocea.corolla.users.domain.Permission
import com.tocea.corolla.users.dto.UserDto
import com.tocea.corolla.users.service.UserDtoService;
import com.tocea.corolla.users.validation.UserValidation

/**
 * @author sleroy
 *
 */
@Secured([
	Permission.ADMIN,
	Permission.ADMIN_USERS
])
@Controller
public class UserAdminController {

	@Autowired
	private UserDtoService userDTODAO

	@Autowired
	private IRoleDAO roleDAO

	@Autowired
	private IUserDAO userDAO

	@Autowired

	private UserValidation validation

	@ModelAttribute("menu")
	public String setMenu() {
		return "admin"
	}

	@RequestMapping("/ui/admin/users")
	public ModelAndView getHomePage() {
		def ModelAndView model = new ModelAndView("admin/users")
		model.addObject "users", userDTODAO.getUsersWithRoleList()

		return model
	}
}
