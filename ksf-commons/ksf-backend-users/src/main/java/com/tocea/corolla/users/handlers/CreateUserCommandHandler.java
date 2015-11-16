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
package com.tocea.corolla.users.handlers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.users.commands.CreateUserCommand;
import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.exceptions.AlreadyExistingUserWithLoginException;
import com.tocea.corolla.users.exceptions.InvalidEmailAddressException;
import com.tocea.corolla.users.exceptions.InvalidUserInformationException;
import com.tocea.corolla.users.exceptions.MissingUserInformationException;
import com.tocea.corolla.users.exceptions.RoleManagementBrokenException;
import com.tocea.corolla.users.service.EmailValidationService;

/**
 * @author sleroy
 *
 */
@CommandHandler
@Transactional
public class CreateUserCommandHandler implements
ICommandHandler<CreateUserCommand, User> {
	
	@Autowired
	private IUserDAO				userDAO;
	
	@Autowired
	private IRoleDAO				roleDAO;

	@Autowired
	private PasswordEncoder			passwordEncoder;
	
	@Autowired
	private EmailValidationService	emailValidationService;
	
	@Autowired
	private Gate gate;
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tocea.corolla.cqrs.handler.ICommandHandler#handle(java.lang.Object)
	 */
	@Override
	public User handle(@Valid final CreateUserCommand _command) {
		final User user = _command.getUser();
		if (user == null) {
			throw new MissingUserInformationException("No data provided to create user");
		}
		if (user.getId() != null && !user.getId().equals("")) {
			throw new InvalidUserInformationException("No ID expected");
		}
		checkUserLogin(user);
		user.copyMissingFields();
		user.setActive(true);
		setDefaultRoleIfNecessary(user);
		if (!emailValidationService.validateEmail(user.getEmail())) {
			throw new InvalidEmailAddressException(user.getEmail());
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userDAO.save(user);

		return user;
	}
	
	/**
	 * @param _user
	 */
	private void checkUserLogin(final User _user) {
		if (userDAO.findUserByLogin(_user.getLogin()) != null) {
			throw new AlreadyExistingUserWithLoginException(_user.getLogin());
		}
		
	}
	
	/**
	 * @param user
	 */
	private void setDefaultRoleIfNecessary(final User user) {
		if (user.getRoleId() == null) {
			Role defaultRole = roleDAO.getDefaultRole();
			if (defaultRole == null) {
				if (Role.DEFAULT_ROLE != null) {
					defaultRole = roleDAO.save(Role.DEFAULT_ROLE);
				}else{
					throw new RoleManagementBrokenException("Could not find the default role");
				}
			}
			user.setRoleId(defaultRole.getId());			
		}
	}
	
	
}
