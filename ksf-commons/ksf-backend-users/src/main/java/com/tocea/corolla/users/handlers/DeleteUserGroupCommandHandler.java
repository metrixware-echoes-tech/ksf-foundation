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
import com.tocea.corolla.users.commands.DeleteUserGroupCommand;
import com.tocea.corolla.users.dao.IUserGroupDAO;
import com.tocea.corolla.users.domain.UserGroup;
import com.tocea.corolla.users.exceptions.InvalidUserGroupInformationException;

@CommandHandler
@Transactional
public class DeleteUserGroupCommandHandler implements ICommandHandler<DeleteUserGroupCommand, UserGroup> {

	@Autowired
	private IUserGroupDAO groupDAO;
	
	@Override
	public UserGroup handle(@Valid DeleteUserGroupCommand command) {
		
		String id = command.getGroupID();
		
		if (id == null || id.equals("")) {
			throw new InvalidUserGroupInformationException("No ID found");
		}
		
		UserGroup group = groupDAO.findOne(id);
		
		if (group == null) {
			throw new InvalidUserGroupInformationException("this user group does not exist");
		}
		
		groupDAO.delete(group);
		
		return group;

	}
	
	
}
