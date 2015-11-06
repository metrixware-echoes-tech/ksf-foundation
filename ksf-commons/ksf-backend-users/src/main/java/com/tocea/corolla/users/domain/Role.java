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
package com.tocea.corolla.users.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * @author sleroy
 *
 */
/*@Entity
@Table(name = "roles")*/
@Document(collection = "roles")
public class Role implements GrantedAuthority {
	
	public static final Role	DEFAULT_ROLE	= new Role("MISSING_ROLE",
			"Missing role",
			"",
			false);
	
	@Id
	@Field("_id")
	private String				id;
	
	//@Size(min = 1, max = 30)
	//@NotBlank
	//@Column(nullable = false)
	private String				name;
	
	//@NotBlank
	//@Column(nullable = false, length = 256)
	private String				note;
	
	//@Column(nullable = false, length = 256)
	private String				permissions;
	
	//@Column(nullable = false)
	private boolean				defaultRole = false;
	
	//@Column(nullable = false)
	private boolean				roleProtected	= false;
	
	/**
	 * default constructor
	 */
	public Role() {
		super();
	}
	
	/**
	 * @param _name
	 * @param _note
	 * @param _permissions
	 * @param _defaultRole
	 */
	public Role(final String _name, final String _note,
			final String _permissions, final boolean _defaultRole) {
		super();
		name = _name;
		note = _note;
		permissions = _permissions;
		defaultRole = _defaultRole;
	}
	
	/**
	 * Returns a new object will all attributes initialized from this instance.
	 *
	 */
	public Role duplicate() {
		final Role role = new Role();
		BeanUtils.copyProperties(this, role);
		return role;
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		
		return permissions;
	}
	
	/**
	 * @return the spring security authorities.
	 */
	public List<GrantedAuthority> getGrantedAuthorities() {
		final Iterable<String> permissionIterator = Splitter.on(',')
				.trimResults()
				.omitEmptyStrings()
				.split(	permissions);
		final ArrayList<GrantedAuthority> authorities = Lists.newArrayList();
		for (final String permission : permissionIterator) {
			authorities.add(new SimpleGrantedAuthority(permission));
		}
		return authorities;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * @return the permissions
	 */
	public String getPermissions() {
		return permissions;
	}
	
	/**
	 * @return the defaultRole
	 */
	public boolean isDefaultRole() {
		return defaultRole;
	}
	
	/**
	 * @return the roleProtected
	 */
	public boolean isRoleProtected() {
		return roleProtected;
	}
	
	public void setDefaultRole() {
		this.setDefaultRole(true);
		
	}
	
	/**
	 * @param _defaultRole
	 *            the defaultRole to set
	 */
	public void setDefaultRole(final boolean _defaultRole) {
		defaultRole = _defaultRole;
	}
	
	/**
	 * @param _id
	 *            the id to set
	 */
	public void setId(final String _id) {
		id = _id;
	}
	
	/**
	 * @param _name
	 *            the name to set
	 */
	public void setName(final String _name) {
		name = _name;
	}
	
	/**
	 * @param _note
	 *            the note to set
	 */
	public void setNote(final String _note) {
		note = _note;
	}
	
	/**
	 * @param _permissions
	 *            the permissions to set
	 */
	public void setPermissions(final String _permissions) {
		permissions = _permissions;
	}
	
	/**
	 * @param _roleProtected
	 *            the roleProtected to set
	 */
	public void setRoleProtected(final boolean _roleProtected) {
		roleProtected = _roleProtected;
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", note="
				+ note + ", permissions=" + permissions
				+ ", defaultRole=" + defaultRole + ", roleProtected="
				+ roleProtected + "]";
	}
}
