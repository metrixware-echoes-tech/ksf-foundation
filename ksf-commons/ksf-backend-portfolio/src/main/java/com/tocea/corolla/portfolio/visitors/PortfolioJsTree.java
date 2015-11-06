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
package com.tocea.corolla.portfolio.visitors;

import java.util.Collection;

import com.tocea.corolla.portfolio.domain.ProjectNode;
import com.tocea.corolla.portfolio.utils.PortfolioUtils;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.utils.ProjectUtils;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.visitors.JsTreeNodeVisitor;

public class PortfolioJsTree extends JsTreeNodeVisitor {

	private Collection<Project> projects;
	
	public PortfolioJsTree(Collection<Project> projects) {
		super();
		this.projects = projects;
	}
	
	@Override
	public void visit(TreeNode node) {
		
		super.visit(node);
		
		if (PortfolioUtils.isProjectNode(node)) {
			
			String ID = ((ProjectNode) node).getProjectId();
			result.getA_attr().put("data-projectID", ID);

			if (this.projects != null) {
				Project project = ProjectUtils.findByID(ID, projects);
				if (project != null) {
					result.setText(project.getName());
					result.getA_attr().put("data-key", project.getKey());
					result.setIcon(project.getImage() != null ? project.getImage().toString() : "");
				}
			}
			
		}
		
	}
	
	@Override
	protected JsTreeNodeVisitor getChildVisitor() {		
		return new PortfolioJsTree(projects);		
	}
	
}
