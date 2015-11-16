package fr.echoes.lab.ksf.cc.plugins.foreman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.echoes.lab.ksf.cc.plugins.foreman.dao.IForemanEnvironmentDAO;
import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanEnvironnment;

@RestController
public class ForemanRestController {

	@Autowired
	private IForemanEnvironmentDAO environmentDAO;
	
	@RequestMapping(value = "/rest/foreman/environments/all")
	public List<ForemanEnvironnment> findAll() {
		
		return environmentDAO.findAll();
	}
	
	@RequestMapping(value = "/rest/foreman/environments/{id}")
	public ForemanEnvironnment findOne(@PathVariable("id") String id) {
		
		return environmentDAO.findOne(id);
	}
	
}
