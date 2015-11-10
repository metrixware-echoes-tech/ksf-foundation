/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.cc.ui.views.projects;

import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/ui/projects")
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

}
