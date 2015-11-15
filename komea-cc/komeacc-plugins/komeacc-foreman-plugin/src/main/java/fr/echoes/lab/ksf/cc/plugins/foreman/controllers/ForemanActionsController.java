package fr.echoes.lab.ksf.cc.plugins.foreman.controllers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;

import fr.echoes.lab.ksf.cc.plugins.foreman.dao.IForemanEnvironmentDAO;
import fr.echoes.lab.ksf.cc.plugins.foreman.dao.IForemanTargetDAO;
import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanEnvironnment;
import fr.echoes.lab.ksf.cc.plugins.foreman.model.ForemanTarget;

@Controller
public class ForemanActionsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanActionsController.class);


	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private IForemanEnvironmentDAO environmentDAO;

	@Autowired
	private IForemanTargetDAO targetDAO;

	@RequestMapping(value = "/ui/foreman/environment/new", method = RequestMethod.POST)
	public String createEnvironment(@RequestParam("projectId") String projectId, @RequestParam("name") String name, @RequestParam("configuration") String configuration) {

		final Project project = this.projectDAO.findOne(projectId);

		final ForemanEnvironnment env = new ForemanEnvironnment();
		env.setName(name);
		env.setConfiguration(configuration);
		env.setProjectId(projectId);

		this.environmentDAO.save(env);

		return "redirect:/ui/projects/"+project.getKey();
	}

	@RequestMapping(value = "/ui/foreman/targets/new", method = RequestMethod.POST)
	public String createTarget(@RequestParam("projectId") String projectId, @RequestParam("name") String name, @RequestParam("environment") String env, @RequestParam("operatingsystem") String operatingsystem, @RequestParam("computeprofiles") String computeprofiles, @RequestParam("puppetConfiguration") String puppetConfiguration) {
		final Project project = this.projectDAO.findOne(projectId);

		final ForemanTarget foremanTarget = new ForemanTarget();
		foremanTarget.setProject(project);
		foremanTarget.setName(name);
		foremanTarget.setComputeProfile(computeprofiles);

		if (!StringUtils.isEmpty(operatingsystem)) {
			final String[] os = StringUtils.split(operatingsystem, '-');
			foremanTarget.setOperatingSystemId(os[0]);
			foremanTarget.setOperatingSystemName(os[1]);
		}

		final ForemanEnvironnment environment = this.environmentDAO.findOne(env);
		foremanTarget.setEnvironment(environment);

		this.targetDAO.save(foremanTarget);

		return "redirect:/ui/projects/"+project.getKey();
	}


	@RequestMapping(value = "/ui/foreman/targets/instantiate", method = RequestMethod.GET)
	public String instantiateTarget(@RequestParam("projectId") String projectId, @RequestParam("targetId") String targetId) {
		final Project project = this.projectDAO.findOne(projectId);

		final ForemanTarget target = this.targetDAO.findOne(targetId);
		target.setProject(project);

		return "redirect:/ui/projects/"+project.getKey();
	}
}
