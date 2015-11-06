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
package com.tocea.corolla.users.validation;

import org.springframework.stereotype.Service;

/**
 * @author sleroy
 *
 */
@Service
public class UserValidation {

	/**
	 * Checks if the login is valid.
	 *
	 * @param _login
	 *            the login name
	 * @return true if the login is valid.
	 */
	public boolean isValidLogin(final String _login) {
		if (_login.length() > 30) {
			return false;
		}
		for (int i = 0; i < _login.length(); ++i) {
			if (!Character.isDigit(_login.charAt(i))
					&& !Character.isLetter(_login.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
