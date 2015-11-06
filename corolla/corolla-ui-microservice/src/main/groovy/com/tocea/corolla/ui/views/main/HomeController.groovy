/**
 *
 */
package com.tocea.corolla.ui.views.main

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
public class HomeController {

    @ModelAttribute("sideMenu")
    public String addAccount() {
        return "mainMenu"
    }

    @RequestMapping(value=[
		"/ui/home",
		"/ui/"		
	])
    public ModelAndView getHomePage() {
        return new ModelAndView("home")
    }
        
    @RequestMapping(value=[
		"/"		
	])
    public String redirectSlash() {
        return "redirect:/ui/home"
    }
}
