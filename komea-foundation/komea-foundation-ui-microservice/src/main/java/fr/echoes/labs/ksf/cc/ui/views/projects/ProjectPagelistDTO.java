/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.labs.ksf.cc.ui.views.projects;

import java.io.Serializable;

import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.users.domain.User;

/**
 *
 * @author rgalerme
 */
public class ProjectPagelistDTO implements Serializable {

    private Project project;
    private User user;

    public ProjectPagelistDTO(Project projet, User user) {
        this.project = projet;
        this.user = user;
    }

    public ProjectPagelistDTO() {
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
