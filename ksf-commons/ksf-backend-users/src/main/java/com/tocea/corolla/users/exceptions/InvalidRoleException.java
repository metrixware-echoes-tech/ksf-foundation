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
package com.tocea.corolla.users.exceptions;

import javax.validation.ValidationException;

/**
 * @author sleroy
 *
 */
public class InvalidRoleException extends ValidationException {

	private static final String	THE_LOGIN_IS_INVALID	= "This role does not exist.";

	public InvalidRoleException() {
		super(THE_LOGIN_IS_INVALID);
	}

	public InvalidRoleException(final String _roleMessage) {
		super(_roleMessage);
	}

	public InvalidRoleException(final Throwable _cause) {
		super(THE_LOGIN_IS_INVALID, _cause);

	}

}
