/**
 *
 */
package fr.echoes.lab.ksf.cc.ui.views.projects

import com.tocea.corolla.products.commands.CreateProjectCommand
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.cqrs.gate.CommandExecutionException
import com.tocea.corolla.cqrs.gate.Gate

import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
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
	
	private static String FORM_PROJECT = "projects/form";
	
	@Autowired
	private Gate gate;

    @RequestMapping(value=[
		"/ui/projects"
	])
    public ModelAndView getListPage() {
        return new ModelAndView("projects/list")
    }
	
	@RequestMapping(value="/ui/projects/new")
	public ModelAndView getCreatePage() {
		
		Project project = new Project();
		def model = new ModelAndView(FORM_PROJECT);
		model.addObject "project", project
		return model
		
	}
	
	@RequestMapping(value = "/ui/projects/new", method = RequestMethod.POST)
	public ModelAndView createProject(@Valid @ModelAttribute("project") Project newProject, BindingResult _result) {

		newProject = _result.model.get("project")

		if (newProject == null) {
			return new ModelAndView("redirect:/ui/projects/new")
		}

		if (_result.hasErrors()) {
			def model = new ModelAndView(FORM_PROJECT)
			model.addObject newProject
			return model
		}

		try {
			
			def project = gate.dispatch new CreateProjectCommand(newProject)
			return new ModelAndView("redirect:/ui/projects/"+project.key)	
					
		}catch(CommandExecutionException ex){
		
			_result.addError(new ObjectError("project", ex.cause.message));
			def model = new ModelAndView(FORM_PROJECT)
			model.addObject newProject
			return model
		}
	}
	
}
