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
package com.tocea.corolla.requirements.trees.visitors;

import java.util.Collection;

import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.requirements.trees.domain.RequirementNode;
import com.tocea.corolla.requirements.trees.utils.RequirementsTreeUtils;
import com.tocea.corolla.requirements.utils.RequirementUtils;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.visitors.JsTreeNodeVisitor;

public class RequirementsJsTreeVisitor extends JsTreeNodeVisitor {

	private Collection<Requirement> requirements;
	
	public RequirementsJsTreeVisitor(Collection<Requirement> requirements) {
		super();
		this.requirements = requirements;
	}
	
	@Override
	public void visit(TreeNode node) {
		
		super.visit(node);
		
		if (RequirementsTreeUtils.isRequirementNode(node)) {
			
			String ID = ((RequirementNode)node).getRequirementId();
			result.getA_attr().put("data-requirementID", ID);

			if (this.requirements != null) {
				
				Requirement requirement = RequirementUtils.findByID(requirements, ID);

				if (requirement != null) {
					result.setText(requirement.getName());
					result.getA_attr().put("data-key", requirement.getKey());
					result.setType("REQ");
				}
				
			}
			
		}
		
	}
	
	@Override
	protected JsTreeNodeVisitor getChildVisitor() {		
		return new RequirementsJsTreeVisitor(requirements);		
	}
	
}
