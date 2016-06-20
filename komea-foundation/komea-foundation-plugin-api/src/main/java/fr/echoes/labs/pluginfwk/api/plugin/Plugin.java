/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.labs.pluginfwk.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * This interface defines an plugin. An extension is a component
 * that implements a specific interface (Plugin). The system use this plugin
 * according to extends the functionalities of the software;
 *
 * @author sleroy
 */
@Component
@Target(ElementType.TYPE)
public @interface Plugin {

}
