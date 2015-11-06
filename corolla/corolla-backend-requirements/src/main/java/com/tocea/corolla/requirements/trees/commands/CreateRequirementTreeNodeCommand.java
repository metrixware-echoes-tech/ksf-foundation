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

import org.hibernate.validator.constraints.NotEmpty;

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.products.domain.ProjectBranch;

@CommandOptions
public class CreateRequirementTreeNodeCommand {

	@NotNull
	private ProjectBranch branch;
	
	@NotEmpty
	private String requirementId;
	
	private Integer parentId;
	
	public CreateRequirementTreeNodeCommand() {
		super();
	}
	
	public CreateRequirementTreeNodeCommand(ProjectBranch branch, String requirementId, Integer parentId) {
		super();
		setBranch(branch);
		setRequirementId(requirementId);
		setParentId(parentId);
	}

	public String getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public ProjectBranch getBranch() {
		return branch;
	}

	public void setBranch(ProjectBranch branch) {
		this.branch = branch;
	}
	
}
