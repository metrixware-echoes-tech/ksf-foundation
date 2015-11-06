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

import javax.validation.constraints.NotNull;

import com.tocea.corolla.cqrs.annotations.CommandOptions;

/**
 * @author sleroy
 *
 */
@CommandOptions
public class DuplicateRoleCommand {
	@NotNull
	private String	roleID;

	public DuplicateRoleCommand() {
		super();
	}

	public DuplicateRoleCommand(final String _roleID) {
		this.roleID = _roleID;

	}

	/**
	 * @return the roleID
	 */
	public String getRoleID() {
		return this.roleID;
	}

	/**
	 * @param _roleName
	 *            the roleID to set
	 */
	public void setRoleID(final String _roleID) {
		this.roleID = _roleID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DuplicateRoleCommand [roleID=" + this.roleID + "]";
	}

}
