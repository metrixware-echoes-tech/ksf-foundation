/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.cc.ui.views.projects;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.exceptions.ProjectNotFoundException;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;

import fr.echoes.lab.ksf.cc.sf.commands.CreateProjectAndProductionLineCommand;
import fr.echoes.lab.ksf.cc.sf.dto.SFProjectDTO;

/**
 *
 * @author rgalerme
 */
@Controller
public class ProjectController {

	private static String FORM_PROJECT 	= "projects/form";
	private static String LIST_PAGE		= "projects/list";
	private static String VIEW_PAGE		= "projects/overview";
	
    @Autowired
    IProjectDAO projectDao;

    @Autowired
    IUserDAO userDao;
    
    @Autowired
    Gate gate;

    @RequestMapping(value = "/ui/projects")
    public ModelAndView getListPage() {
        ModelAndView model = new ModelAndView(LIST_PAGE);
        List<Project> findAll = projectDao.findAll();
        List<ProjectPagelistDTO> projectsList = new ArrayList<>();

        for (Project pr : findAll) {
            projectsList.add(createProjectPageListDTO(pr));
        }

        model.addObject("projects", projectsList);
        return model;
    }
    
    private ProjectPagelistDTO createProjectPageListDTO(Project project) {
    	
    	User findOne = userDao.findOne(project.getOwnerId());
        if (findOne == null) {
            findOne = new User();
            findOne.setFirstName("");
            findOne.setLastName("");
        }
        
    	return new ProjectPagelistDTO(project, findOne);
    }
    
    @RequestMapping(value = "/ui/projects/{projectKey}")
    public ModelAndView getProjectPage(@PathVariable String projectKey) {
    	
    	Project project = projectDao.findByKey(projectKey);
    	
    	if (project == null) {
    		throw new ProjectNotFoundException();
    	}
    	
    	ModelAndView model = new ModelAndView(VIEW_PAGE);
    	model.addObject("projectData", createProjectPageListDTO(project));
    	
    	return model;
    }

    @RequestMapping(value = "/ui/projects/new")
    public ModelAndView getCreatePage() {

    	SFProjectDTO project = new SFProjectDTO();
        ModelAndView model = new ModelAndView(FORM_PROJECT);
        model.addObject("project", project);
        return model;

    }

    @RequestMapping(value = "/ui/projects/new", method = RequestMethod.POST)
    public ModelAndView createProject(@Valid @ModelAttribute("project") SFProjectDTO newProject, BindingResult _result) {

        newProject = (SFProjectDTO) _result.getModel().get("project");
        
        if (newProject == null) {
            return new ModelAndView("redirect:/ui/projects/new");
        }

        if (_result.hasErrors()) {
            ModelAndView model = new ModelAndView(FORM_PROJECT);
            model.addObject(newProject);
            return model;
        }

        try {

        	Project project = gate.dispatch(new CreateProjectAndProductionLineCommand(newProject));

            return new ModelAndView("redirect:/ui/projects/" + project.getKey());

        } catch (CommandExecutionException ex) {

            _result.addError(new ObjectError("project", ex.getCause().getMessage()));
            ModelAndView model = new ModelAndView(FORM_PROJECT);
            model.addObject(newProject);
            return model;
        }
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler({
		ProjectNotFoundException.class
	})
	public void handlePageNotFoundException() {
	}

}
