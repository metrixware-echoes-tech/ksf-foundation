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
class ReleasePageController {

	private static String RELEASES_VIEW = "project/releases"
	
	@Autowired
	private IProjectDAO projectDAO
			
	@ModelAttribute("menu")
	public String setMenu() {
		return "releases"
	}
			
	@RequestMapping("/ui/releases/{projectKey}")
	public ModelAndView getReleasePage(@PathVariable String projectKey) {
		
		Project project = projectDAO.findByKey(projectKey)
		
		def model = new ModelAndView(RELEASES_VIEW)
		model.addObject "project", project
		
		return model
	}
	
}
