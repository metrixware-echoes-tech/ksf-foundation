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
import com.tocea.corolla.users.commands.DuplicateRoleCommand;
import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.exceptions.MissingRoleInformationException;
import com.tocea.corolla.users.service.RolePermissionService;

/**
 * @author sleroy
 *
 */
@CommandHandler
@Transactional
public class DuplicateRoleCommandHandler implements
ICommandHandler<DuplicateRoleCommand, Role> {

	@Autowired
	private IRoleDAO				roleDAO;

	@Autowired
	private RolePermissionService	rolePermissionService;

	@Override
	public Role handle(@Valid final DuplicateRoleCommand _command) {
		
		final String roleID = _command.getRoleID();
		
		Role roleToDuplicate = this.roleDAO.findOne(roleID);
		
		if (roleToDuplicate == null) {
			throw new MissingRoleInformationException("No role provided");
		}
		
		roleToDuplicate = roleToDuplicate.duplicate();
		roleToDuplicate.setName(roleToDuplicate.getName() + " duplicated");
		roleToDuplicate.setId(null);
		roleToDuplicate.setDefaultRole(false);
		
		this.roleDAO.save(roleToDuplicate);

		return roleToDuplicate;
	}

}
