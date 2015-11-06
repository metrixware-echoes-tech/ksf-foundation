/**
 *
 */
package com.tocea.corolla.ui.views.admin.roles

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.users.commands.DuplicateRoleCommand
import com.tocea.corolla.users.commands.MarksRoleAsDefaultCommand
import com.tocea.corolla.users.dao.IRoleDAO
import com.tocea.corolla.users.domain.Permission
import com.tocea.corolla.users.validation.UserValidation

/**
 * @author sleroy
 *
 */
@Secured([
	Permission.ADMIN,
	Permission.ADMIN_ROLES
])
@Controller
public class RoleAdminController {


	@Autowired
	private IRoleDAO roleDAO

	@Autowired
	private UserValidation validation
	
	@Autowired
	private Gate gate

	@ModelAttribute("menu")
	public String setMenu() {
		return "admin"
	}

	@RequestMapping("/ui/admin/roles")
	public ModelAndView getHomePage() {
		
		def ModelAndView model = new ModelAndView("admin/roles")
		model.addObject "roles", roleDAO.findAll()

		return model
	}

	@RequestMapping("/ui/admin/roles/default/{id}")
	public String marksRoleAsDefault(@PathVariable String id) {
		
		if (roleDAO.findOne(id) != null) {
			gate.dispatch(new MarksRoleAsDefaultCommand(id))
		}
		
		return "redirect:/ui/admin/roles"
		
	}

	@RequestMapping("/ui/admin/roles/duplicate/{id}")
	public String duplicateRole(@PathVariable String id) {
		
		if (roleDAO.findOne(id) != null) {
			gate.dispatch(new DuplicateRoleCommand(id))
		}
		
		return "redirect:/ui/admin/roles"
	}
	
}