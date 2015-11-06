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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.dto.ProjectNodeDTO;

@CommandOptions
public class CreateProjectCommand {
	
	@NotNull
	private Project project;

	private Integer parentNodeID;

	public CreateProjectCommand() {
		super();
	}

	public CreateProjectCommand(final Project _project) {
		super();
		setProject(_project);
	}

	public CreateProjectCommand(final Project _project, final Integer parentNodeID) {
		super();
		setProject(_project);
		setParentNodeID(parentNodeID);
	}
	
	public void fromProjectNodeDTO(final ProjectNodeDTO projectNodeDTO) {
		project = new Project();
		project.setName(projectNodeDTO.getName());
		project.setKey(projectNodeDTO.getKey());
		setParentNodeID(projectNodeDTO.getParentID());
	}
	
	public Integer getParentNodeID() {
		return parentNodeID;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setParentNodeID(final Integer parentNodeID) {
		this.parentNodeID = parentNodeID;
	}

	public void setProject(@Valid final Project project) {
		this.project = project;
	}

}