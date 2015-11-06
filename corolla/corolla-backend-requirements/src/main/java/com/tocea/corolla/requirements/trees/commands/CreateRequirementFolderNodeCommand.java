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
package com.tocea.corolla.requirements.trees.commands;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.trees.domain.FolderNodeType;

@CommandOptions
public class CreateRequirementFolderNodeCommand {

	@NotNull
	private ProjectBranch branch;
	
	private Integer parentID;
	
	private String typeID;
	
	@NotEmpty
	@Size(min=1, max=100)
	private String text;
	
	public CreateRequirementFolderNodeCommand() {
		super();
	}
	
	public CreateRequirementFolderNodeCommand(ProjectBranch branch, Integer parentID, String text, String typeID) {
		super();
		setBranch(branch);
		setParentID(parentID);
		setText(text);
		setTypeID(typeID);
	}
	
	public CreateRequirementFolderNodeCommand(ProjectBranch branch, Integer parentID, String text, FolderNodeType type) {
		this(branch, parentID, text, type.getId());
	}

	public ProjectBranch getBranch() {
		return branch;
	}

	public void setBranch(ProjectBranch branch) {
		this.branch = branch;
	}

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}
	
}
