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

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.tocea.corolla.users.domain.Role;

public class RoleDTO {
	
	private String id;
	private String name;
	private String note;
	private List<String> permissions;
	
	public RoleDTO() {
		this.permissions = Lists.newArrayList();
	}
	
	public RoleDTO(Role role) {
		this.id = role.getId();
		this.name = role.getName();
		this.note = role.getNote();
		this.permissions = Lists.newArrayList();
		this.permissions.addAll(Arrays.asList(role.getPermissions().split(", ")));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
}