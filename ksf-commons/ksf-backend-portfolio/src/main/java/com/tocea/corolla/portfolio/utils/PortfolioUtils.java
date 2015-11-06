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
package com.tocea.corolla.portfolio.utils;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.tocea.corolla.portfolio.domain.ProjectNode;
import com.tocea.corolla.trees.domain.TreeNode;

public class PortfolioUtils {

	/**
	 * Tests if a node is a ProjectNode
	 * @param node
	 * @return
	 */
	public static boolean isProjectNode(TreeNode node) {
		
		return node instanceof ProjectNode;
	}
	
	/**
	 * Retrieves the list of project IDs in the given nodes
	 * @param nodes
	 * @return
	 */
	public static List<String> getProjectIDs(Collection<TreeNode> nodes) {
		
		List<String> ids = Lists.newArrayList();
		
		for(TreeNode node : nodes) {
			
			if (isProjectNode(node)) {
				ids.add(((ProjectNode)node).getProjectId());
			}
			
			ids.addAll(getProjectIDs(node.getNodes()));
			
		}
		
		return ids;
	}
	
}