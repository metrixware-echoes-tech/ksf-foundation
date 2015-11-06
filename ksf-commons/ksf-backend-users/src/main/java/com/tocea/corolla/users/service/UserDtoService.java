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
package com.tocea.corolla.users.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tocea.corolla.users.dao.IRoleDAO;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;
import com.tocea.corolla.users.dto.UserDto;
import com.tocea.corolla.users.dto.UserWithRoleDto;

/**
 * @author sleroy
 *
 */
@Service
public class UserDtoService implements IUserDtoService {

	@Autowired
	private IUserDAO	userDAO;

	@Autowired
	private IRoleDAO	roleDAO;

	public List<UserDto> getUsersDtoList() {
		final List<UserDto> userTable = new ArrayList<>();
		for (final User user : this.userDAO.findAll()) {
			userTable.add(new UserDto(user));
		}
		return userTable;
	}

	/**
	 * Return the user with role.
	 *
	 */
	public List<UserWithRoleDto> getUsersWithRoleList() {
		final List<UserWithRoleDto> userTable = new ArrayList<UserWithRoleDto>();
		for (final User user : this.userDAO.findAll()) {
			userTable.add(new UserWithRoleDto(user,
					this.roleDAO.findOne(user.getRoleId())));
		}
		return userTable;
	}
}
