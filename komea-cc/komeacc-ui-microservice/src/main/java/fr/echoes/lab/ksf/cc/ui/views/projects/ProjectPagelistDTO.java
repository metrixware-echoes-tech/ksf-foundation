/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.cc.ui.views.projects;

import fr.echoes.lab.ksf.cc.ui.views.projects.*;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.users.domain.User;
import java.io.Serializable;

/**
 *
 * @author rgalerme
 */
public class ProjectPagelistDTO implements Serializable {

    private Project projet;
    private User utilisateur;

    public ProjectPagelistDTO(Project projet, User utilisateur) {
        this.projet = projet;
        this.utilisateur = utilisateur;
    }

    public ProjectPagelistDTO() {
    }

    public Project getProjet() {
        return projet;
    }

    public void setProjet(Project projet) {
        this.projet = projet;
    }

    public User getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

}
