/**
 *
 */
package fr.echoes.lab.ksf.cc.ui.views.projects

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
public class ProjectController {

    @RequestMapping(value=[
		"/ui/projects"
	])
    public ModelAndView getHomePage() {
        return new ModelAndView("projects/list")
    }
	
}
