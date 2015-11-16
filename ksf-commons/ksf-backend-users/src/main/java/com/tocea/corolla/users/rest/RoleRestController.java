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
package com.tocea.corolla.users.rest;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.users.commands.DeleteRoleCommand;
import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.domain.Permission;
import com.tocea.corolla.users.exceptions.InvalidRoleException;

/**
 * @author sleroy
 *
 */
@RestController()
@RequestMapping("/rest/roles")
@Secured(Permission.REST)
@Transactional
public class RoleRestController {

	@Autowired
	private IRoleDAO	roleDao;
	@Autowired
	private Gate		gate;

	@Secured({ Permission.ADMIN, Permission.ADMIN_ROLES })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public void deleteUser(@PathVariable final String id) {
		if (roleDao.findOne(id) == null) {
			throw new InvalidRoleException();
		}

		gate.dispatchAsync(new DeleteRoleCommand(id));

	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public JsonError handleException(final Exception e) {
		return new JsonError(e.getMessage());
	}

}
