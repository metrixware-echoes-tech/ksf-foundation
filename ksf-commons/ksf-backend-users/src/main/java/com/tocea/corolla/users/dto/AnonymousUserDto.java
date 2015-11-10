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
package com.tocea.corolla.users.dto;

import java.util.Date;

/**
 * This class defines the anonymmous user Dto.
 *
 * @author sleroy
 *
 */
public class AnonymousUserDto extends UserDto {
	public static final String UNKNOWN = "unknown";

	public AnonymousUserDto() {
		setEmail("");
		setFirstName(UNKNOWN);
		setLastName("");
		setCreatedTime(new Date());
	}
}
