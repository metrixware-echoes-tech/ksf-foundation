package com.tocea.corolla.ui.views.projects

import groovy.util.logging.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project

@Controller
@Slf4j
class VariablePageController {

	private static String VARIABLES_VIEW = "project/variables"
	
	@Autowired
	private IProjectDAO projectDAO
			
	@ModelAttribute("menu")
	public String setMenu() {
		return "variables"
	}
			
	@RequestMapping("/ui/variables/{projectKey}")
	public ModelAndView getVariablesPage(@PathVariable projectKey) {
		
		Project project = projectDAO.findByKey(projectKey)
				
		def model = new ModelAndView(VARIABLES_VIEW)
		model.addObject "project", project
		
		return model
		
	}
	
}
