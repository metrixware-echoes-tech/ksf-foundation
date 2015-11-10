/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.cc.ui.views.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author rgalerme
 */
@Controller
public class HomeController {
    
    @RequestMapping(value="/ui/home")
    public ModelAndView getHomePage() {
        return new ModelAndView("home");
    }
        
    
    /**
     * Une direction est produite pour être capturée par Spring Security et afficher la page de login
     * @return 
     */
    @RequestMapping(value={"/ui/","/"})
    public String redirectSlashAndBasicUrl() {
        return "redirect:/ui/home";
    }
}
