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
package com.tocea.corolla.products.commands;

import javax.validation.constraints.NotNull;

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.products.domain.ProjectBranch;

@CommandOptions
public class MergeProjectBranchCommand {

	@NotNull
	private ProjectBranch origin;
	
	@NotNull
	private ProjectBranch destination;
	
	public MergeProjectBranchCommand() {
		super();
	}
	
	public MergeProjectBranchCommand(ProjectBranch origin, ProjectBranch destination) {
		super();
		setOrigin(origin);
		setDestination(destination);
	}

	public ProjectBranch getOrigin() {
		return origin;
	}

	public void setOrigin(ProjectBranch origin) {
		this.origin = origin;
	}

	public ProjectBranch getDestination() {
		return destination;
	}

	public void setDestination(ProjectBranch destination) {
		this.destination = destination;
	}
	
}
