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

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.users.commands.DeleteRoleCommand;
import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.exceptions.RoleOperationForbiddenException;

/**
 * @author sleroy
 *
 */
@CommandHandler
@Transactional
public class DeleteRoleCommandHandler implements
ICommandHandler<DeleteRoleCommand, Boolean> {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(DeleteRoleCommandHandler.class);

	@Autowired
	private IRoleDAO			roleDAO;
	
	@Autowired
	private IUserDAO 			userDAO;

	@Override
	public Boolean handle(final DeleteRoleCommand _command) {

		final Role role = this.roleDAO.findOne(_command.getRoleID());
		final Boolean found = role != null;
		
		if (found) {
			
			if (role.isRoleProtected()) {
				throw new RoleOperationForbiddenException("cannot delete a protected role");
			}
			
			if (role.isDefaultRole()) {
				throw new RoleOperationForbiddenException("cannot delete the default role");
			}
			
			Role defaultRole = roleDAO.getDefaultRole();
			
			List<User> users = userDAO.findByRoleId(role.getId());			
			for(User user : users) {
				LOGGER.info("Reset role for user {}", user.getLogin());
				user.setRole(defaultRole);	
			}
			userDAO.save(users);
			
			LOGGER.info("Delete role {}", role.getName());
			this.roleDAO.delete(role);

		}

		return found;
	}

}
