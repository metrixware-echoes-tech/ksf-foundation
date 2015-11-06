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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.users.commands.CreateUserGroupCommand;
import com.tocea.corolla.users.dao.IUserGroupDAO;
import com.tocea.corolla.users.domain.UserGroup;
import com.tocea.corolla.users.exceptions.InvalidUserGroupInformationException;
import com.tocea.corolla.users.exceptions.MissingUserGroupInformationException;
import com.tocea.corolla.users.exceptions.UserGroupAlreadyExistException;

@CommandHandler
@Transactional
public class CreateUserGroupCommandHandler implements
		ICommandHandler<CreateUserGroupCommand, UserGroup> {

	@Autowired
	private IUserGroupDAO groupDAO;

	@Override
	public UserGroup handle(@Valid CreateUserGroupCommand command) {

		UserGroup group = command.getUserGroup();

		if (group == null) {
			throw new MissingUserGroupInformationException(
					"No data provided to create user group");
		}

		if (StringUtils.isNotBlank(group.getId())) {
			throw new InvalidUserGroupInformationException("No ID expected");
		}
		
		if (groupDAO.findByName(group.getName()) != null) {
			throw new UserGroupAlreadyExistException();
		}
		
		group.setId(null);

		groupDAO.save(group);

		return group;

	}

}
