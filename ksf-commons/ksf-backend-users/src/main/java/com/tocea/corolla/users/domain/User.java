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

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import com.google.common.base.Strings;

/**
 * This class declares a member that contributes to a repository. It is
 * basically identified by the email.
 *
 * @author sleroy
 *
 */
/*@Entity()
 @Table(name = "users", indexes = { @Index(unique = true, name = "login_index", columnList = "login") })*/
@Document(collection = "users")
public class User implements Serializable {
	
	@Id
	@Field("_id")
	private String id;
	
	@NotBlank
	@Size(min = 3, max = 30)
	//@Column(nullable = false, length = 30)
	private String login = "";
	
	@NotBlank
	@Size(max = 40)
	//@Column(nullable = false, length = 40)
	private String firstName = "";
	
	@NotBlank
	@Size(max = 40)
	//@Column(nullable = false, length = 40)
	private String lastName = "";
	
	@NotBlank
	@Size(max = 128)
	//@Column(nullable = false, length = 128)
	@Email
	private String email = "";
	
	@NotBlank
	@Size(max = 256)
	//@Column(nullable = false, length = 256)
	private String password = "";
	
	@NotNull
	//@Column(nullable = false)
	private String roleId;
	
	@NotBlank
	@Size(max = 10)
	//@Column(nullable = false, length = 10)
	private String locale = "en_GB";	//$NON-NLS-1$
	
	@Size(max = 50)
	//@Column(nullable = true, length = 50)
	private String activationToken = "";		//$NON-NLS-1$
	
	@NotNull
	//@Column(nullable = false)
	private Date createdTime;
	
	@Size(max = 256)
	//@Column(nullable = false, length = 128)
	private String salt = "";
	
	@NotNull
	//@Column(nullable = false)
	private boolean active = true;
	
	public User() {
	}
	
	public User(final LdapUserDetails _principal) {
		super();
		setLogin(_principal.getUsername());
		setFirstName(_principal.getUsername());
		setPassword(_principal.getPassword());


	}

	/**
	 * Copy values if missing fields
	 */
	public void copyMissingFields() {
		if (activationToken == null) {
			activationToken = "";
		}
		if (Strings.isNullOrEmpty(firstName)
				&& Strings.isNullOrEmpty(lastName)) {
			firstName = login;
		}

		if (createdTime == null) {
			createdTime = new Date();
		}
		
		setLocaleIfNecessary();
		
	}
	
	/**
	 * @return the activationToken
	 */
	public String getActivationToken() {
		return activationToken;
	}
	
	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return the role_id
	 */
	public String getRoleId() {
		return roleId;
	}
	
	public String getSalt() {
		return salt;
	}
	
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * @param _activationToken the activationToken to set
	 */
	public void setActivationToken(final String _activationToken) {
		activationToken = _activationToken;
	}
	
	/**
	 * @param _active the active to set
	 */
	public void setActive(final boolean _active) {
		active = _active;
	}
	
	/**
	 * @param _createdTime the createdTime to set
	 */
	public void setCreatedTime(final Date _createdTime) {
		createdTime = _createdTime;
	}
	
	/**
	 * @param _email the email to set
	 */
	public void setEmail(final String _email) {
		email = _email;
	}
	
	/**
	 * @param _firstName the firstName to set
	 */
	public void setFirstName(final String _firstName) {
		firstName = _firstName;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
	
	/**
	 * @param _lastName the lastName to set
	 */
	public void setLastName(final String _lastName) {
		lastName = _lastName;
	}
	
	/**
	 * @param _locale the locale to set
	 */
	public void setLocale(final String _locale) {
		locale = _locale;
	}
	
	/**
	 * Set locale if necessary
	 *
	 * @param _user
	 */
	public void setLocaleIfNecessary() {
		if (Strings.isNullOrEmpty(getLocale())) {
			setLocale(Locale.getDefault().toString());
		}
		
	}
	
	/**
	 * @param _login the login to set
	 */
	public void setLogin(final String _login) {
		login = _login;
	}
	
	/**
	 * @param _password the password to set
	 */
	public void setPassword(final String _password) {
		password = _password;
	}
	
	public void setRole(final Role _role) {
		roleId = _role.getId();
	}
	
	/**
	 * @param _role_id the role_id to set
	 */
	public void setRoleId(final String _role_id) {
		roleId = _role_id;
	}
	
	public void setSalt(final String salt) {
		this.salt = salt;
	}
	
	@Override
	public String toString() {
		return "User{" + "id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", roleId=" + roleId + ", locale=" + locale + ", activationToken=" + activationToken + ", createdTime=" + createdTime + ", active=" + active + '}';
	}
	
}
