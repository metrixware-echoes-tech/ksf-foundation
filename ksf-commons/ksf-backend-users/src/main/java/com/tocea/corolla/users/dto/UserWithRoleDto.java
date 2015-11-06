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

import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;

/**
 * @author sleroy
 *
 */
public class UserWithRoleDto extends UserDto {
	private String	role;

	public UserWithRoleDto(final User _user, final Role _role) {
		super(_user);
		this.role = _role == null ? "" : _role.getName();
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return this.role;
	}

	/**
	 * @param _role
	 *            the role to set
	 */
	public void setRole(final String _role) {
		this.role = _role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserWithRoleDto [role=" + this.role + ", toString()="
				+ super.toString() + "]";
	}
}
