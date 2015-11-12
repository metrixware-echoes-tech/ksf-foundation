package fr.echoes.lab.ksf.cc.plugins.foreman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

@Controller
public class ForemanActionsController {

	@Autowired
	private IProjectDAO projectDAO;
	
	@RequestMapping(value = "/ui/foreman/targets/new", method = RequestMethod.POST)
	public String createTarget(@RequestParam("projectKey") String projectKey) {
		
		Project project = projectDAO.findByKey(projectKey);
		
		return "redirect:/ui/projects/"+project.getKey();
	}
	
	@RequestMapping(value = "/ui/foreman/environment/new", method = RequestMethod.POST)
	public String createEnvironment(@RequestParam("projectKey") String projectKey) {
		
		Project project = projectDAO.findByKey(projectKey);
		
		return "redirect:/ui/projects/"+project.getKey();
	}
	
}
