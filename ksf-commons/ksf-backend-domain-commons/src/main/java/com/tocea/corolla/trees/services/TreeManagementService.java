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

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tocea.corolla.trees.domain.ITree;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.exceptions.InvalidTreeNodeInformationException;
import com.tocea.corolla.trees.exceptions.MissingTreeInformationException;
import com.tocea.corolla.trees.exceptions.MissingTreeNodeInformationException;
import com.tocea.corolla.trees.predicates.FindNodeByIDPredicate;
import com.tocea.corolla.trees.predicates.ITreeNodePredicate;
import com.tocea.corolla.trees.utils.TreeNodeUtils;

@Service
public class TreeManagementService implements ITreeManagementService {

	@Override
	public TreeNode findNode(final Collection<TreeNode> nodes, final ITreeNodePredicate predicate) {
		
		for(final TreeNode node : nodes) {			
			
			if (predicate.test(node)) {
				return node;
			}
			
			final TreeNode child = findNode(node.getNodes(), predicate);
			
			if (child != null) {
				return child;
			}
		}
		
		return null;
		
	}
	
	@Override
	public TreeNode findNode(final ITree tree, final ITreeNodePredicate predicate) {
            final Collection<TreeNode> treeChildreNodes = tree.getNodes();
		
		return findNode(treeChildreNodes, predicate);
	}

	@Override
	public ITree insertNode(final ITree tree, final Integer parentID, final TreeNode newNode) {
		
		if (tree == null) {
			throw new MissingTreeInformationException("No tree found");
		}
		
		if (newNode == null) {
			throw new MissingTreeNodeInformationException("The tree node to insert is null");
		}
		
		final Collection<TreeNode> nodes = Lists.newArrayList(tree.getNodes());

		if (newNode.getId() == null) {
			newNode.setId(TreeNodeUtils.getMaxNodeId(nodes)+1);
		}
		
		if (newNode.getNodes() == null) {
			newNode.setNodes(new ArrayList<TreeNode>());
		}
		
		if (parentID == null) {
			
			nodes.add(newNode);
			
		}else{
			
			final TreeNode parentNode = findNodeByID(nodes, parentID);
			
			if (parentNode == null) {
				throw new InvalidTreeNodeInformationException("The given parent ID does not match any of the nodes of this tree.");
			}
			
			final Collection<TreeNode> parentNodes = Lists.newArrayList(parentNode.getNodes());
			parentNodes.add(newNode);
			parentNode.setNodes(parentNodes);
			
		}
		
		tree.setNodes(nodes);
		
		return tree;
	}

	@Override
	public ITree moveNode(ITree tree, final Integer nodeID, final Integer newParentID) {
		
		if (tree == null) {
			throw new MissingTreeInformationException("No tree found");
		}
		
		if (nodeID == null) {
			throw new MissingTreeNodeInformationException("No ID found");
		}
		
		final Collection<TreeNode> nodes = Lists.newArrayList(tree.getNodes());
		
		final TreeNode node = findNodeByID(nodes, nodeID);
		
		if (node == null) {
			throw new InvalidTreeNodeInformationException("The given node ID does not match any node of this tree");
		}
		
		if (newParentID == null) {
			
			tree = removeNode(tree, nodeID);
			tree = insertNode(tree, newParentID, node);
			
		}else{
			
			final TreeNode newParentNode = findNodeByID(nodes, nodeID);
			
			if (newParentNode == null) {
				throw new InvalidTreeNodeInformationException("The given parent node ID does not match any node of this tree");
			}
			
			if (TreeNodeUtils.hasNodeWithId(newParentID, node, true)) {
				throw new InvalidTreeNodeInformationException("Cannot move a tree node inside one of its own children");
			}
			
			tree = removeNode(tree, nodeID);
			tree = insertNode(tree, newParentID, node);
			
		}
		
		return tree;
	}
	
	@Override
	public ITree removeNode(final ITree tree, final Integer nodeID) {
				
		if (tree == null) {
			throw new MissingTreeInformationException("No tree found");
		}
		
		if (nodeID == null) {
			throw new MissingTreeNodeInformationException("No ID found");
		}
		
		final Collection<TreeNode> nodes = Lists.newArrayList(tree.getNodes());
		
		final TreeNode node = findNodeByID(nodes, nodeID);
		
		if (node == null) {
			throw new InvalidTreeNodeInformationException("The given node ID does not match any node of this tree");
		}
		
		final TreeNode parentNode = TreeNodeUtils.getParentNodeOf(nodeID, nodes);
		
		if (parentNode == null) {
			
			nodes.remove(node);
		
		}else{
		
			parentNode.getNodes().remove(node);
			
		}
		
		tree.setNodes(nodes);
		
		return tree; 
	}
	
	private TreeNode findNodeByID(final Collection<TreeNode> nodes, final Integer nodeID) {
		
		return findNode(nodes, new FindNodeByIDPredicate(nodeID));
	}
	
}