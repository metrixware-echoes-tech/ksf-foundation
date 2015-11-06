/**
 *
 */
package com.tocea.corolla.utils.functests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sleroy
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionalTestDoc {
	String requirementId() default "";
	String requirementName();
	String testCaseId() default "";
	String ticketNumber() default "";
}
