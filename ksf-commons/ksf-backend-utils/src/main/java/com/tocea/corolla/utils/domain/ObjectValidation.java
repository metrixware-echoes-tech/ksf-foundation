/**
 *
 */
package com.tocea.corolla.utils.domain;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * This class is used to performs manual validation of pojo.
 * @author sleroy
 *
 */
public class ObjectValidation {
	
	public final static String URL_SAFE_PATTERN = "[\\w[-]?]*";
	
	private final Validator	validator;

	public ObjectValidation() {
		super();
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	public boolean isValid(final Object _object) {
		final Set<ConstraintViolation<Object>> constraints = this.validator.validate(_object);
		return constraints.isEmpty();
	}

	public void validate(final Object _object) {
		final Set<ConstraintViolation<Object>> constraints = this.validator.validate(_object);
		if (!constraints.isEmpty()) {
			throw new ConstraintViolationException("Object " + _object +" + is not valid", constraints);
		}
	}
}
