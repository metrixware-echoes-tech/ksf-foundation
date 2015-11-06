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

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tocea.corolla.users.dao.IRoleDAO;

/**
 * @author sleroy
 *
 */
@Component
public class UserDtoValidator implements Validator {
	
	@Autowired
	private IRoleDAO	roleDAO;
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> _arg0) {
		
		return UserDto.class.equals(_arg0);
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object _arg0, final Errors _errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(	_errors, "firstName",
				"firstName.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(	_errors, "lastName",
				"firstName.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(	_errors, "email",
				"firstName.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(	_errors, "login",
				"firstName.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(	_errors, "locale",
				"firstName.empty");
		
		final UserDto dto = (UserDto) _arg0;
		if (dto.getRoleId() == null) {
			_errors.reject("roleId", "role.missing");
		}
		
		if (roleDAO.findOne(dto.getRoleId()) == null) {
			_errors.reject("roleId", "role.invalid");
		}
		
		if (Locale.forLanguageTag(dto.getLocale()) == null) {
			_errors.reject("locale", "locale.invalid");
		}
		
		final Set<ConstraintViolation<UserDto>> failures = Validation.buildDefaultValidatorFactory()
				.getValidator()
				.validate(	dto);
		
		for (final ConstraintViolation<UserDto> failure : failures) {
			System.out.println(failure);
			_errors.reject(	failure.getPropertyPath().toString(),
					failure.getPropertyPath().toString() + "-> "
							+ failure.getMessage());
			
		}
		
	}
}
