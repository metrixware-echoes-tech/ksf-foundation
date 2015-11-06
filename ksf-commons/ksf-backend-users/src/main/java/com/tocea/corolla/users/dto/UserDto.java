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

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.DigestUtils;

import com.tocea.corolla.users.domain.User;

/**
 * @author sleroy
 *
 */
public class UserDto {
	
	private String	id;

	@NotBlank
	@Size(max = 40)
	private String	firstName		= "";

	@NotBlank
	@Size(max = 40)
	private String	lastName		= "";

	@NotBlank
	@Size(max = 128)
	@Email
	private String	email			= "";
	@NotBlank
	@Size(min = 3, max = 30)
	private String	login			= "";

	@NotNull
	private String	roleId;

	@NotBlank
	@Size(max = 10)
	private String	locale			= "en_GB";	//$NON-NLS-1$

	@Size(max = 50)
	private String	activationToken	= "";		//$NON-NLS-1$

	private Date	createdTime;

	@NotNull
	private boolean	active			= true;

	private String	gravatar		= "";

	/**
	 *
	 */
	public UserDto() {
		super();
	}

	public UserDto(final User _user) {
		
		this.activationToken = _user.getActivationToken();
		this.active = _user.isActive();
		this.createdTime = _user.getCreatedTime();
		this.email = _user.getEmail();
		this.firstName = _user.getFirstName();
		this.lastName = _user.getLastName();
		this.id = _user.getId();
		this.locale = _user.getLocale();
		this.login = _user.getLogin();
		this.roleId = _user.getRoleId();
		this.gravatar = DigestUtils.md5DigestAsHex(this.email.toLowerCase()
		                                           .trim()
		                                           .getBytes());
	}

	/**
	 * @return the activationToken
	 */
	public String getActivationToken() {
		return this.activationToken;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return this.createdTime;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @return the gravatar
	 */
	public String getGravatar() {
		return this.gravatar;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return this.locale;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return this.roleId;
	}

	public boolean hasId() {
		return this.id != null;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param _activationToken
	 *            the activationToken to set
	 */
	public void setActivationToken(final String _activationToken) {
		this.activationToken = _activationToken;
	}

	/**
	 * @param _active
	 *            the active to set
	 */
	public void setActive(final boolean _active) {
		this.active = _active;
	}

	/**
	 * @param _createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(final Date _createdTime) {
		this.createdTime = _createdTime;
	}

	/**
	 * @param _email
	 *            the email to set
	 */
	public void setEmail(final String _email) {
		this.email = _email;
	}

	/**
	 * @param _firstName
	 *            the firstName to set
	 */
	public void setFirstName(final String _firstName) {
		this.firstName = _firstName;
	}

	/**
	 * @param _gravatar
	 *            the gravatar to set
	 */
	public void setGravatar(final String _gravatar) {
		this.gravatar = _gravatar;
	}

	/**
	 * @param _id
	 *            the id to set
	 */
	public void setId(final String _id) {
		this.id = _id;
	}

	/**
	 * @param _lastName
	 *            the lastName to set
	 */
	public void setLastName(final String _lastName) {
		this.lastName = _lastName;
	}

	/**
	 * @param _locale
	 *            the locale to set
	 */
	public void setLocale(final String _locale) {
		this.locale = _locale;
	}

	/**
	 * @param _login
	 *            the login to set
	 */
	public void setLogin(final String _login) {
		this.login = _login;
	}

	/**
	 * @param _roleId
	 *            the roleId to set
	 */
	public void setRoleId(final String _roleId) {
		this.roleId = _roleId;
	}
	
	public void setRoleId(final Integer _roleId) {
		this.roleId = _roleId.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserDto [id=" + this.id + ", firstName=" + this.firstName
				+ ", lastName=" + this.lastName + ", email=" + this.email
				+ ", login=" + this.login + ", roleId=" + this.roleId
				+ ", locale=" + this.locale + ", activationToken="
				+ this.activationToken + ", createdTime=" + this.createdTime
				+ ", active=" + this.active + "]";
	}
}
