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
package com.tocea.corolla.users.commands;

import org.hibernate.validator.constraints.NotBlank;

import com.tocea.corolla.cqrs.annotations.CommandOptions;

@CommandOptions
public class DisableUserCommand {
	@NotBlank
	private String	userLogin;

	public DisableUserCommand() {
		super();
	}

	public DisableUserCommand(final String _userLogin) {
		super();
		this.userLogin = _userLogin;
	}

	public String getUserLogin() {
		return this.userLogin;
	}

	/**
	 * @param _userLogin
	 *            the userLogin to set
	 */
	public void setUserLogin(final String _userLogin) {
		this.userLogin = _userLogin;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DisableUserCommand [userLogin=" + this.userLogin + "]";
	}
}
