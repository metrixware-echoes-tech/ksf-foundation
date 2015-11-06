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
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.dto.UserPasswordDto;

@CommandOptions
public class CreateUserCommand {
	@NotNull
	private User	user;

	public CreateUserCommand() {
		super();
	}

	public CreateUserCommand(final User _user) {
		this.user = _user;

	}

	public CreateUserCommand(final UserPasswordDto _dto) {
		this.user = new User();
		this.user.setActivationToken(_dto.getActivationToken());
		this.user.setActive(_dto.isActive());
		this.user.setCreatedTime(_dto.getCreatedTime());
		this.user.setEmail(_dto.getEmail());
		this.user.setFirstName(_dto.getFirstName());
		this.user.setLastName(_dto.getLastName());
		this.user.setId(_dto.getId());
		this.user.setLocale(_dto.getLocale());
		this.user.setLocaleIfNecessary();
		this.user.setLogin(_dto.getLogin());
		this.user.setPassword(_dto.getPassword());
		this.user.setRoleId(_dto.getRoleId());

	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param _user
	 *            the user to set
	 */
	public void setUser(final User _user) {
		this.user = _user;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CreateUserCommand [user=" + this.user + "]";
	}
}
