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
package com.tocea.corolla.trees.domain;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.tocea.corolla.trees.visitors.ITreeNodeVisitor;

public class TreeNode implements Cloneable {

	private Integer id;
	
	private Collection<TreeNode> nodes;
	
	public TreeNode() {
		
	}

	public Collection<TreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(Collection<TreeNode> nodes) {
		this.nodes = nodes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void accept(ITreeNodeVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public TreeNode clone() {		
		return clone(new TreeNode());		
	}
	
	protected TreeNode clone(TreeNode clone) {
		
		clone.setId(this.id);
		
		List<TreeNode> nodesCloned = Lists.newArrayList();
		
		for(TreeNode node : this.nodes) {			
			nodesCloned.add(node.clone());			
		}
		
		clone.setNodes(nodesCloned);
		
		return clone;	
		
	}
	
}