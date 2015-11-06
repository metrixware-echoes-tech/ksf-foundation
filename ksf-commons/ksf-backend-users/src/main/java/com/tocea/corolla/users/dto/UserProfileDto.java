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

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.DigestUtils;

import com.tocea.corolla.users.domain.User;

/**
 * @author sleroy
 *
 */
public class UserProfileDto {
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param _password the password to set
	 */
	public void setPassword(String _password) {
		password = _password;
	}

	/**
	 * @return the passwordConfirm
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	/**
	 * @param _passwordConfirm the passwordConfirm to set
	 */
	public void setPasswordConfirm(String _passwordConfirm) {
		passwordConfirm = _passwordConfirm;
	}

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

	@NotBlank
	@Size(max = 10)
	private String	locale			= "en_GB";	//$NON-NLS-1$

	@Size(max = 50)
	private String	activationToken	= "";		//$NON-NLS-1$

	@NotBlank
	private String	password;

	@NotBlank
	private String	passwordConfirm;


	private String	gravatar		= "";

	/**
	 *
	 */
	public UserProfileDto() {
		super();
	}

	public UserProfileDto(final User _user) {
		
		this.activationToken = _user.getActivationToken();
		this.email = _user.getEmail();
		this.firstName = _user.getFirstName();
		this.lastName = _user.getLastName();
		this.id = _user.getId();
		this.locale = _user.getLocale();
		this.login = _user.getLogin();
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

	public boolean hasId() {
		return this.id != null;
	}

	@AssertTrue(message = "Password and confirmation should be identical")
	public boolean isValid() {
		return this.password.equals(this.passwordConfirm);
	}


	/**
	 * @param _activationToken
	 *            the activationToken to set
	 */
	public void setActivationToken(final String _activationToken) {
		this.activationToken = _activationToken;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserProfileDto [id=" + this.id + ", firstName=" + this.firstName
				+ ", lastName=" + this.lastName + ", email=" + this.email + ", login="
				+ this.login + ", locale=" + this.locale + ", activationToken="
				+ this.activationToken + ", gravatar=" + this.gravatar + "]";
	}
}
