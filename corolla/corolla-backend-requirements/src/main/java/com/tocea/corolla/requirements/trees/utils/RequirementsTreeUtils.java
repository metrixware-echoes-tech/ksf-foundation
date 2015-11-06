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
package com.tocea.corolla.requirements.trees.utils;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.requirements.trees.domain.RequirementNode;
import com.tocea.corolla.trees.domain.TreeNode;

public class RequirementsTreeUtils {

	/**
	 * Tests if a node is a Requirement Node
	 * @param node
	 * @return
	 */
	public static boolean isRequirementNode(TreeNode node) {
		
		return node instanceof RequirementNode;
	}
	
	/**
	 * Returns a flat list of all the requirements nodes
	 * @param nodes
	 * @return
	 */
	public static Collection<RequirementNode> getRequirementsNodes(Collection<TreeNode> nodes) {
		
		Collection<RequirementNode> requirementNodes = Lists.newArrayList();
		
		for(TreeNode node : nodes) {
			
			if (isRequirementNode(node)) {			
				requirementNodes.add((RequirementNode)node);			
			}
			
			requirementNodes.addAll(getRequirementsNodes(node.getNodes()));
			
		}
		
		return requirementNodes;
		
	}
	
	/**
	 * Retrieves the list of all requirement IDs
	 * from a collection of nodes
	 * @param nodes
	 * @return
	 */
	public static Collection<String> getRequirementsIDs(Collection<TreeNode> nodes) {
		
		Collection<String> ids = Lists.newArrayList();
		
		for(TreeNode node : nodes) {
			
			if (isRequirementNode(node)) {
				ids.add(((RequirementNode) node).getRequirementId());
			}
			
			ids.addAll(getRequirementsIDs(node.getNodes()));
		}
		
		return ids;
	}
	
	/**
	 * Removes nodes not attached to the given requirements IDs
	 * @param nodes
	 * @param ids
	 * @return
	 */
	public static Collection<TreeNode> removeRequirementsNotInIDs(Collection<TreeNode> nodes, Collection<String> ids) {
			
		for(TreeNode node : Lists.newArrayList(nodes)) {	
			if (isRequirementNode(node) && !ids.contains(((RequirementNode)node).getRequirementId())) {							
				nodes.remove(node);					
			}else{
				node.setNodes(removeRequirementsNotInIDs(node.getNodes(), ids));				
			}			
		}
		
		return nodes;	
	}
	
	/**
	 * Removes nodes not attached to the given requirements
	 * @param nodes
	 * @param requirements
	 * @return
	 */
	public static Collection<TreeNode> removeRequirementsNotIn(Collection<TreeNode> nodes, Collection<Requirement> requirements) {

		Collection<String> ids = Collections2.transform(requirements, new Function<Requirement, String>() {
			@Override
			public String apply(Requirement requirement) {
				return requirement != null ? requirement.getId() : null;
			}		
		});
		
		return removeRequirementsNotInIDs(nodes, ids);	
	}
	
}