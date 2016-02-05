/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.echoes.lab.ksf.extensions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * This interface defines an extension component. An extension is a component
 * that implements a specific interface (ExtensionPoint). The system use this extension
 * according the extension point specification.
 *
 * @author sleroy
 */
@Component
@Target(ElementType.TYPE)
public @interface Extension {

}
