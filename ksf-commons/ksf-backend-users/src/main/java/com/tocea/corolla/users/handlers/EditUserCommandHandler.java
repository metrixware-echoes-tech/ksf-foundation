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

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.users.commands.EditUserCommand;
import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.exceptions.InvalidEmailAddressException;
import com.tocea.corolla.users.exceptions.InvalidUserInformationException;
import com.tocea.corolla.users.exceptions.MissingUserInformationException;
import com.tocea.corolla.users.service.EmailValidationService;

/**
 * @author sleroy
 *
 */
@CommandHandler
@Transactional
public class EditUserCommandHandler implements
ICommandHandler<EditUserCommand, User> {

	@Autowired
	private IUserDAO				userDAO;

	@Autowired
	private IRoleDAO				roleDAO;

	@Autowired
	private EmailValidationService	emailValidationService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tocea.corolla.cqrs.handler.ICommandHandler#handle(java.lang.Object)
	 */
	@Override
	public User handle(@Valid final EditUserCommand _command) {
		final User user = _command.getUser();
		if (user == null) {
			throw new MissingUserInformationException("No data provided to edit user");
		}
		if (user.getId() == null) {
			throw new InvalidUserInformationException("ID expected");
		}
		if (user.getRoleId() == null) {
			throw new InvalidUserInformationException("Role expected");
		}
		if (!this.emailValidationService.validateEmail(user.getEmail())) {
			throw new InvalidEmailAddressException(user.getEmail());
		}

		this.userDAO.save(user);
		return user;
	}



}
