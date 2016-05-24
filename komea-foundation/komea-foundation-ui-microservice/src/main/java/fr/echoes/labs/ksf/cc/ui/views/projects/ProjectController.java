/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.labs.ksf.cc.ui.views.projects;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.CreateFeatureCommand;
import com.tocea.corolla.products.commands.CreateReleaseCommand;
import com.tocea.corolla.products.commands.DeleteProjectCommand;
import com.tocea.corolla.products.commands.FinishFeatureCommand;
import com.tocea.corolla.products.commands.FinishReleaseCommand;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.exceptions.ProjectNotFoundException;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;

import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.IProjectTabPanel;
import fr.echoes.labs.ksf.cc.extensions.gui.project.dashboard.ProjectDashboardWidget;
import fr.echoes.labs.ksf.cc.extmanager.projects.ui.IProjectDashboardExtensionManager;
import fr.echoes.labs.ksf.cc.sf.commands.CreateProjectAndProductionLineCommand;
import fr.echoes.labs.ksf.cc.sf.dto.SFProjectDTO;

/**
 *
 * @author rgalerme
 */
@Controller
public class ProjectController {

	private static String FORM_PROJECT 	= "projects/form";
	private static String LIST_PAGE		= "projects/list";
	private static String VIEW_PAGE		= "projects/overview";

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private IProjectDashboardExtensionManager projectDashboardExtensionManager;

	@Autowired
	IProjectDAO projectDao;

	@Autowired
	IUserDAO userDao;

	@Autowired
	Gate gate;

	@RequestMapping(value = "/ui/projects/new", method = RequestMethod.POST)
	public ModelAndView createProject(@Valid @ModelAttribute("project") SFProjectDTO newProject, final BindingResult _result) {

		newProject = (SFProjectDTO) _result.getModel().get("project");

		if (newProject == null) {
			return new ModelAndView("redirect:/ui/projects/new");
		}

		if (_result.hasErrors()) {
			final ModelAndView model = new ModelAndView(FORM_PROJECT);
			model.addObject(newProject);
			return model;
		}

		try {

			final Project project = this.gate.dispatch(new CreateProjectAndProductionLineCommand(newProject));

			return new ModelAndView("redirect:/ui/projects/" + project.getKey());

		} catch (final CommandExecutionException ex) {

			_result.addError(new ObjectError("project", ex.getCause().getMessage()));
			final ModelAndView model = new ModelAndView(FORM_PROJECT);
			model.addObject(newProject);
			return model;
		}
	}

	@RequestMapping(value = "/ui/projects/releases/new")
	public ModelAndView createRelease(@RequestParam("projectKey") final String projectKey, @RequestParam("releaseVersion") final String releaseVersion) {

		final Project project = this.projectDao.findByKey(projectKey);

		if (project == null) {
			throw new ProjectNotFoundException();
		}

    	this.gate.dispatch(new CreateReleaseCommand(project, releaseVersion));

		return new ModelAndView("redirect:/ui/projects/" + project.getKey());
	}	

	@RequestMapping(value = "/ui/projects/releases/finish")
	public ModelAndView finishRelease(@RequestParam("projectKey") final String projectKey,  @RequestParam("releaseVersion") final String releaseVersion) {

		final Project project = this.projectDao.findByKey(projectKey);

		if (project == null) {
			throw new ProjectNotFoundException();
		}

        this.gate.dispatch(new FinishReleaseCommand(project, releaseVersion));

		return new ModelAndView("redirect:/ui/projects/" + project.getKey());
	}	
	
	@RequestMapping(value = "/ui/projects/features/new")
	public ModelAndView createFeature(@RequestParam("projectKey") final String projectKey, @RequestParam("featureId") final String featureId, @RequestParam("featureSubject") final String featureSubject) {

		final Project project = this.projectDao.findByKey(projectKey);

		if (project == null) {
			throw new ProjectNotFoundException();
		}

        this.gate.dispatch(new CreateFeatureCommand(project, featureId, featureSubject));

		return new ModelAndView("redirect:/ui/projects/" + project.getKey());
	}
	
	@RequestMapping(value = "/ui/projects/features/finish")
	public ModelAndView closeFeature(@RequestParam("projectKey") final String projectKey, @RequestParam("featureId") final String featureId, @RequestParam("featureSubject") final String featureSubject) {

		final Project project = this.projectDao.findByKey(projectKey);

		if (project == null) {
			throw new ProjectNotFoundException();
		}

        this.gate.dispatch(new FinishFeatureCommand(project, featureId, featureSubject));

		return new ModelAndView("redirect:/ui/projects/" + project.getKey());
	}

	@RequestMapping(value = "/ui/projects/new")
	public ModelAndView getCreatePage() {

		final SFProjectDTO project = new SFProjectDTO();
		final ModelAndView model = new ModelAndView(FORM_PROJECT);
		model.addObject("project", project);

		final List<Project> projects = this.projectDao.findAll();

		model.addObject("projects", projects);

		return model;

	}

	@RequestMapping(value = "/ui/projects")
	public ModelAndView getListPage() {
		final ModelAndView model = new ModelAndView(LIST_PAGE);
		final List<Project> findAll = this.projectDao.findAll();
		final List<ProjectPagelistDTO> projectsList = new ArrayList<>();

		for (final Project pr : findAll) {
			projectsList.add(createProjectPageListDTO(pr));
		}

		model.addObject("projects", projectsList);
		return model;
	}

	@RequestMapping(value = "/ui/projects/{projectKey}")
	public ModelAndView getProjectPage(@PathVariable final String projectKey) {

		final Project project = this.projectDao.findByKey(projectKey);

		if (project == null) {
			throw new ProjectNotFoundException();
		}

		final ModelAndView model = new ModelAndView(VIEW_PAGE);
		model.addObject("projectData", createProjectPageListDTO(project));

		final List<IProjectTabPanel> panels = Lists.newArrayList();
		final List<ProjectDashboardWidget> widgets = this.projectDashboardExtensionManager.getDashboardWidgets();

		for(final ProjectDashboardWidget widget : widgets) {
			panels.addAll(widget.getTabPanels(projectKey));
		}

		model.addObject("widgets", widgets);
		model.addObject("panels", panels);

		return model;
	}


    @RequestMapping(value = "/ui/projects/delete/{projectKey}")
    public ModelAndView DeleteProjectPage(@PathVariable final String projectKey) {
    	final Project findByKey = this.projectDao.findByKey(projectKey);
    	this.gate.dispatch(new DeleteProjectCommand(findByKey.getId()));
    	return new ModelAndView("redirect:/ui/projects/");
    }


	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler({
		ProjectNotFoundException.class
	})
	public void handlePageNotFoundException() {
	}

	private ProjectPagelistDTO createProjectPageListDTO(final Project project) {

		User findOne = null;

		if (StringUtils.isNotEmpty(project.getOwnerId())) {
			findOne = this.userDao.findOne(project.getOwnerId());
		}

		if (findOne == null) {
			findOne = new User();
			findOne.setFirstName("");
			findOne.setLastName("");
		}

		return new ProjectPagelistDTO(project, findOne);
	}

}
