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
package com.tocea.corolla.trees.utils;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.tocea.corolla.trees.domain.FolderNode;
import com.tocea.corolla.trees.domain.TreeNode;

public class TreeNodeUtils {
		
	/**
	 * Retrieves the max id of the nodes in a given tree
	 * @param nodes
	 * @return
	 */
	public static Integer getMaxNodeId(Collection<TreeNode> nodes) {
		
		int max = 0;
		
		for(TreeNode node : nodes) {
			
			if (node.getId() > max) {				
				max = node.getId();
			}
			
			int childrenId = getMaxNodeId(node.getNodes());
			
			if (childrenId > max) {
				max = childrenId;
			}
			
		}	
		
		return max;		
	}
	
	/**
	 * Retrieves the parent node of the node with the given ID
	 * @param id
	 * @param nodes
	 * @return
	 */
	public static TreeNode getParentNodeOf(Integer id, Collection<TreeNode> nodes) {
		
		for(TreeNode node : nodes) {
			
			if (hasNodeWithId(id, node, false)) {
				return node;
			}
			
			TreeNode childNode = getParentNodeOf(id, node.getNodes());
			
			if (childNode != null) {
				return childNode;
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * Indicates if a node has a child matching a given id
	 * @param id
	 * @param parent
	 * @return
	 */
	public static Boolean hasNodeWithId(Integer id, TreeNode parent, Boolean recursive) {
		
		for(TreeNode node : Lists.newArrayList(parent.getNodes())) {
			
			if (node.getId().equals(id)) {
				return true;
			}
			
			if (recursive) {
				Boolean found = hasNodeWithId(id, node, true);
				if (found) {
					return true;
				}
			}
			
		}
		
		return false;		
	}
	
	/**
	 * Tests if a node is a text node
	 * @param node
	 * @return
	 */
	public static Boolean isFolderNode(TreeNode node) {
		
		return node instanceof FolderNode;
	}

}