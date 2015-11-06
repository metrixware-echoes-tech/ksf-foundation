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
package com.tocea.corolla.portfolio.domain;

import java.util.ArrayList;

import com.tocea.corolla.trees.domain.TreeNode;

public class ProjectNode extends TreeNode implements IPortfolioNode {

	private String projectId;
	
	public ProjectNode() {
		super();
	}
	
	public ProjectNode(String projectId) {
		super();
		setProjectId(projectId);
		setNodes(new ArrayList<TreeNode>());
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	@Override
	public TreeNode clone() {		
		return this.clone(new ProjectNode(this.projectId));		
	}
	
}
