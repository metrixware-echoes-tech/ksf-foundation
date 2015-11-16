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

import java.security.Principal;

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
import com.tocea.corolla.users.commands.DeleteUserCommand;
import com.tocea.corolla.users.commands.DisableUserCommand;
import com.tocea.corolla.users.commands.EnableUserCommand;
import com.tocea.corolla.users.domain.Permission;
import com.tocea.corolla.users.dto.UserDto;
import com.tocea.corolla.users.dto.UserWithRoleDto;
import com.tocea.corolla.users.exceptions.InvalidLoginException;
import com.tocea.corolla.users.exceptions.OperationForbidenWithThisLoginException;
import com.tocea.corolla.users.service.UserDtoService;
import com.tocea.corolla.users.validation.UserValidation;
import com.tocea.corolla.utils.datatable.DataTableList;

/**
 * @author sleroy
 *
 */
@RestController()
@RequestMapping("/rest/users")
@Secured(Permission.REST)
public class UserRestController {
	@Autowired
	private Gate			gate;

	@Autowired
	private UserDtoService	dtoService;

	@Autowired
	private UserValidation	userValidation;

	@Secured({ Permission.ADMIN, Permission.ADMIN_USERS })
	@RequestMapping(value = "/delete/{login}", method = RequestMethod.GET)
	public void deleteUser(@PathVariable final String login,
			final Principal _principal) {
		if (!userValidation.isValidLogin(login)) {
			throw new InvalidLoginException();
		}
		if (login.equals(_principal.getName())) {
			throw new OperationForbidenWithThisLoginException();
		}

		gate.dispatchAsync(new DeleteUserCommand(login));

	}

	@Secured({ Permission.ADMIN, Permission.ADMIN_USERS })
	@RequestMapping(value = "/disable/{login}", method = RequestMethod.GET)
	public void disableUser(@PathVariable final String login,
			final Principal _principal) {
		if (!userValidation.isValidLogin(login)) {
			throw new InvalidLoginException();
		}
		if (login.equals(_principal.getName())) {
			throw new OperationForbidenWithThisLoginException();
		}

		gate.dispatchAsync(new DisableUserCommand(login));
	}

	@Secured({ Permission.ADMIN, Permission.ADMIN_USERS })
	@RequestMapping(value = "/enable/{login}", method = RequestMethod.GET)
	public void enableUser(@PathVariable final String login,
			final Principal _principal) {
		if (!userValidation.isValidLogin(login)) {
			throw new InvalidLoginException();
		}
		if (login.equals(_principal.getName())) {
			throw new OperationForbidenWithThisLoginException();
		}

		gate.dispatchAsync(new EnableUserCommand(login));
	}

	@RequestMapping("/all")
	public DataTableList<UserDto> getAllUsers() {
		final DataTableList<UserDto> userTable = new DataTableList();
		userTable.addAll(dtoService.getUsersDtoList());
		return userTable;
	}

	@RequestMapping("/allwithrole")
	public DataTableList<UserWithRoleDto> getAllUsersWithRole() {
		final DataTableList<UserWithRoleDto> userTable = new DataTableList();
		userTable.addAll(dtoService.getUsersWithRoleList());
		return userTable;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public JsonError handleException(final Exception e) {
		return new JsonError(e.getMessage());
	}

}
