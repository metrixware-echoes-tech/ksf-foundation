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

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.dto.RoleDTO;

/**
 * @author sleroy
 *
 */
@CommandOptions
public class CreateRoleCommand {

	@NotNull
	private Role	role;

	public CreateRoleCommand() {
		
	}
	
	/**
	 * @param _role
	 */
	public CreateRoleCommand(final Role _role) {
		super();
		this.role = _role;
	}
	
	public CreateRoleCommand(final RoleDTO _roleDTO) {
		
		super();
		this.role = new Role();
		this.role.setName(_roleDTO.getName());
		this.role.setNote(_roleDTO.getNote());
		
		List<String> permissions = _roleDTO.getPermissions();
		if (permissions != null) {
			this.role.setPermissions(StringUtils.join(permissions, ", "));
		}
		
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return this.role;
	}

	/**
	 * @param _role
	 *            the role to set
	 */
	public void setRole(final Role _role) {
		this.role = _role;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CreateRoleCommand [role=" + this.role + "]";
	}

}
