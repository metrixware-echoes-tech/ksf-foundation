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
package com.tocea.corolla.trees.visitors;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.tocea.corolla.trees.domain.FolderNode;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.dto.JsTreeNodeDTO;
import com.tocea.corolla.trees.utils.TreeNodeUtils;

public class JsTreeNodeVisitor implements ITreeNodeVisitor {

	protected JsTreeNodeDTO result;
	
	public JsTreeNodeVisitor() {
		
	}
	
	@Override
	public void visit(TreeNode node) {
		
		result = new JsTreeNodeDTO();
		
		if (TreeNodeUtils.isFolderNode(node)) {
			result.setText(((FolderNode)node).getText());
			result.setType(((FolderNode)node).getTypeID());
		}	
		
		result.setA_attr(new HashMap<String, String>());
		result.getA_attr().put("data-nodeID", node.getId().toString());

		List<JsTreeNodeDTO> children = Lists.newArrayList();
		
		for(TreeNode child : node.getNodes()) {
			
			JsTreeNodeVisitor visitor = getChildVisitor();
			child.accept(visitor);
			
			children.add(visitor.getResult());		
			
		}
		
		result.setChildren(children);
		
	}

	public JsTreeNodeDTO getResult() {
		return result;
	}
	
	protected JsTreeNodeVisitor getChildVisitor() {		
		return new JsTreeNodeVisitor();		
	}

}