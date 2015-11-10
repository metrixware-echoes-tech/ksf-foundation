/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.cc.ui.views.projects;

import com.tocea.corolla.cqrs.gate.CommandExecutionException;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author rgalerme
 */
@Controller
public class ProjectController {

    @Autowired
    IProjectDAO projetDao;

    @Autowired
    IUserDAO userDao;
    
    @Autowired
    Gate gate;

    private static String FORM_PROJECT = "projects/form";

    @RequestMapping(value = "/ui/projects")
    public ModelAndView getHomePage() {
        ModelAndView model = new ModelAndView("projects/list");
        List<Project> findAll = projetDao.findAll();
        List<ProjectPagelistDTO> projectsList = new ArrayList<>();

        for (Project pr : findAll) {
            User findOne = userDao.findOne(pr.getOwnerId());
            if (findOne == null) {
                findOne = new User();
                findOne.setFirstName("");
                findOne.setLastName("");
            }
            projectsList.add(new ProjectPagelistDTO(pr, findOne));
        }

        model.addObject("projects", projectsList);
        return model;
    }

    @RequestMapping(value = "/ui/projects/new")
    public ModelAndView getCreatePage() {

        Project project = new Project();
        ModelAndView model = new ModelAndView(FORM_PROJECT);
        model.addObject("project", project);
        return model;

    }

    @RequestMapping(value = "/ui/projects/new", method = RequestMethod.POST)
    public ModelAndView createProject(@Valid @ModelAttribute("project") Project newProject, BindingResult _result) {

        newProject = (Project) _result.getModel().get("project");
        

        if (newProject == null) {
            return new ModelAndView("redirect:/ui/projects/new");
        }

        if (_result.hasErrors()) {
            ModelAndView model = new ModelAndView(FORM_PROJECT);
            model.addObject(newProject);
            return model;
        }

        try {

            Project project = gate.dispatch(new CreateProjectCommand(newProject));

            return new ModelAndView("redirect:/ui/projects/" + project.getKey());

        } catch (CommandExecutionException ex) {

            _result.addError(new ObjectError("project", ex.getMessage()));
            ModelAndView model = new ModelAndView(FORM_PROJECT);
            model.addObject(newProject);
            return model;
        }
    }

}
