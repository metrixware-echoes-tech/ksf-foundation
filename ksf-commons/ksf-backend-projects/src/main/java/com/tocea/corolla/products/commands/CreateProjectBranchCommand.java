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

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectBranch;

@CommandOptions
public class CreateProjectBranchCommand {

	private ProjectBranch branch;
	private ProjectBranch originBranch;
	
	public CreateProjectBranchCommand() {
		super();
	}
	
	public CreateProjectBranchCommand(ProjectBranch _branch) {
		super();
		setBranch(_branch);
	}
	
	public CreateProjectBranchCommand(String name, Project project) {
		super();
		ProjectBranch branch = new ProjectBranch();
		branch.setName(name);
		branch.setProjectId(project.getId());
		setBranch(branch);
	}
	
	public CreateProjectBranchCommand(String name, ProjectBranch originBranch) {
		super();
		ProjectBranch branch = new ProjectBranch();
		branch.setName(name);
		branch.setProjectId(originBranch.getProjectId());
		setBranch(branch);
		setOriginBranch(originBranch);
	}

	public ProjectBranch getBranch() {
		return branch;
	}

	public void setBranch(ProjectBranch branch) {
		this.branch = branch;
	}

	public ProjectBranch getOriginBranch() {
		return originBranch;
	}

	public void setOriginBranch(ProjectBranch originBranch) {
		this.originBranch = originBranch;
	}
	
}
