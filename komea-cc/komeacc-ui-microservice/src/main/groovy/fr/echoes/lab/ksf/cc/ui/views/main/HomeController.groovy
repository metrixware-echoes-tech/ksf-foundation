/**
 *
 */
package fr.echoes.lab.ksf.cc.ui.views.main

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

/**
 *  This controller defines the main URLs of Komea CC. 
 *  <ul>
 *  <li>The Homepage and its redirections</li>
 *  <li>Errors pages without logic are defined in ViewController</li>
 *  </ul>
 * @author sleroy
 *
 */

@Controller
public class HomeController {



    @RequestMapping(value=[
		"/ui/home"
	])
    public ModelAndView getHomePage() {
        return new ModelAndView("home")
    }
        
    
    /**
     * Une direction est produite pour être capturée par Spring Security et afficher la page de login
     */
    @RequestMapping(value=[
		"/ui/",
		"/"
	])
    public String redirectSlashAndBasicUrl() {
        return "redirect:/ui/home"
    }
}
