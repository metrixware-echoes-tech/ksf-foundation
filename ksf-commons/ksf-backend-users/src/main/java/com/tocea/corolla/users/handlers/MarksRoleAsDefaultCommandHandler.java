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
import com.tocea.corolla.users.commands.MarksRoleAsDefaultCommand;
import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.domain.Role;
import com.tocea.corolla.users.exceptions.InvalidRoleException;

/**
 * @author sleroy
 *
 */
@CommandHandler
@Transactional
public class MarksRoleAsDefaultCommandHandler implements
ICommandHandler<MarksRoleAsDefaultCommand, Boolean> {

	@Autowired
	private IRoleDAO	roleDAO;

	@Override
	public Boolean handle(@Valid final MarksRoleAsDefaultCommand _command) {
		final String roleID = _command.getRoleID();
		if (!this.roleDAO.exists(roleID)) {
			throw new InvalidRoleException("Role was not found " + roleID);
		}
		this.disableOtherRoles();
		this.enableSelectedRole(roleID);

		return true;
	}

	private void disableOtherRoles() {
		for (final Role role : this.roleDAO.findAll()) {
			if (role.isDefaultRole()) {
				role.setDefaultRole(false);
				this.roleDAO.save(role);
			}
		}

	}

	private void enableSelectedRole(final String _roleID) {
		final Role role = this.roleDAO.findOne(_roleID);
		role.setDefaultRole();
		this.roleDAO.save(role);
	}

}
