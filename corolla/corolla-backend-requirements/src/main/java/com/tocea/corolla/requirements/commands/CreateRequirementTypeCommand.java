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
package com.tocea.corolla.requirements.commands;

import javax.validation.constraints.NotNull;

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.requirements.domain.RequirementType;

@CommandOptions
public class CreateRequirementTypeCommand {

	@NotNull
	private RequirementType type;
	
	public CreateRequirementTypeCommand() {
		super();
	}
	
	public CreateRequirementTypeCommand(RequirementType _type) {
		super();
		setType(_type);
	}

	public RequirementType getType() {
		return type;
	}

	public void setType(RequirementType type) {
		this.type = type;
	}
	
}
