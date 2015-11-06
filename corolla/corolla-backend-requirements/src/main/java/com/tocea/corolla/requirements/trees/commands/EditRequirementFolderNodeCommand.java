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

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.products.domain.ProjectBranch;

@CommandOptions
public class EditRequirementFolderNodeCommand {

	private ProjectBranch branch;
	
	private Integer nodeID;
	
	@NotBlank
	@Size(min = 1, max = 100)
	private String text;
	
	public EditRequirementFolderNodeCommand() {
		super();
	}

	public EditRequirementFolderNodeCommand(ProjectBranch branch, Integer nodeID, String text) {
		super();
		setBranch(branch);
		setNodeID(nodeID);
		setText(text);
	}
	
	public ProjectBranch getBranch() {
		return branch;
	}

	public void setBranch(ProjectBranch branch) {
		this.branch = branch;
	}

	public Integer getNodeID() {
		return nodeID;
	}

	public void setNodeID(Integer nodeID) {
		this.nodeID = nodeID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
