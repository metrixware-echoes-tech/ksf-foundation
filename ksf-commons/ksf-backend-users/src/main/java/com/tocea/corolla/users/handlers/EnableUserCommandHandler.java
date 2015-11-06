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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.users.commands.EnableUserCommand;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;

/**
 * @author sleroy
 *
 */
@CommandHandler
@Transactional
public class EnableUserCommandHandler implements
ICommandHandler<EnableUserCommand, Boolean> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnableUserCommandHandler.class);

	@Autowired
	private IUserDAO				userDAO;


	/* (non-Javadoc)
	 * @see com.tocea.corolla.cqrs.handler.ICommandHandler#handle(java.lang.Object)
	 */
	@Override
	public Boolean handle(final EnableUserCommand _command) {

		final User user = this.userDAO.findUserByLogin(_command.getUserLogin());
		final Boolean found =  user != null;
		if (found) {
			LOGGER.info("Enable user {}", user.getLogin());
			user.setActive(true);
			this.userDAO.save(user);

		}

		return found;
	}

}
