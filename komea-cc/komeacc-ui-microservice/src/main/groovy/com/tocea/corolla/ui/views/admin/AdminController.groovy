/**
 *
 */
package com.tocea.corolla.ui.views.admin

import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

import com.tocea.corolla.users.domain.Permission

/**
 * @author sleroy
 *
 */
@Controller
public class AdminController {

	@ModelAttribute("sideMenu")
	public String addAccount() {
		return "adminMenu"
	}
	@Secured(Permission.ADMIN)
	@RequestMapping("/ui/admin")
	public ModelAndView getHomePage() {
		return new ModelAndView("admin/admin")
	}
}
