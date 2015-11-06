/*
 * Corolla - A Tool to manage software requirements and test cases 
 * Copyright (C) 2015 Tocea
 * 
 * This file is part of Corolla.
 * 
 * Corolla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, 
 * or any later version.
 * 
 * Corolla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Corolla.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.users.dto;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.base.Objects;

/**
 * @author sleroy
 *
 */
public class UserPasswordDtoValidator implements Validator {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> _arg0) {

		return UserPasswordDto.class.equals(_arg0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object _object, final Errors _errors) {
		ValidationUtils.invokeValidator(new UserDtoValidator(), _object,
		                                _errors);
		ValidationUtils.rejectIfEmptyOrWhitespace(	_errors, "password",
				"password.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(	_errors, "passwordConfirm",
				"password.need	Confirm");
		final UserPasswordDto u = (UserPasswordDto) _object;
		if (u.getPassword().length() > 256) {
			_errors.reject("password", "password.tooLong");
		}
		if (Objects.equal(u.getPassword(), u.getPasswordConfirm())) {
			_errors.reject("password", "password.notEqualsConfirm");

		}
	}

}
