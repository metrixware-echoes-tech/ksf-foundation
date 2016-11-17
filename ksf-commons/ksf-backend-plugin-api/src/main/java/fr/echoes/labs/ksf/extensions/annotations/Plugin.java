package fr.echoes.labs.ksf.extensions.annotations;

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
