/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.labs.ksf.cc.plugin.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * This interface defines an extension. An extension is a contract to be implemented by a plugin to provide a functionality.
 *
 * @author sleroy
 */
@Component
@Target(ElementType.TYPE)
public @interface Extension {

}
