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
package com.tocea.corolla.trees.services;

import java.util.Collection;

import com.tocea.corolla.trees.domain.ITree;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.predicates.ITreeNodePredicate;

public interface ITreeManagementService {
	
	/**
	 * Insert a node in a tree
	 * @param tree
	 * @param parentID
	 * @param node
	 * @return
	 */
	public ITree insertNode(ITree tree, Integer parentID, TreeNode node);
	
	/**
	 * Move a node in a tree
	 * @param tree
	 * @param nodeID
	 * @param newParentID
	 * @return
	 */
	public ITree moveNode(ITree tree, Integer nodeID, Integer newParentID);
	
	/**
	 * Remove a node in a tree
	 * @param tree
	 * @param nodeID
	 * @return
	 */
	public ITree removeNode(ITree tree, Integer nodeID);

	/**
	 * Find a specific node in a tree
	 * @param tree
	 * @param predicate
	 * @return
	 */
	public TreeNode findNode(ITree tree, ITreeNodePredicate predicate);
	
	/**
	 * Find a specific node in a collection of nodes
	 * @param nodes
	 * @param predicate
	 * @return
	 */
	public TreeNode findNode(Collection<TreeNode> nodes, ITreeNodePredicate predicate);
	
}
