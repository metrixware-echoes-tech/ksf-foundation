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
package com.tocea.corolla.requirements.trees.domain;

import java.util.Collection;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tocea.corolla.trees.domain.ITree;
import com.tocea.corolla.trees.domain.TreeNode;

@Document
public class RequirementsTree implements ITree {
	
	@Id
	@Field("_id")
	private String id;

	@NotEmpty
	private String branchId;

	private Collection<TreeNode> nodes;

	public String getBranchId() {
		return branchId;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public Collection<TreeNode> getNodes() {
		return nodes;
	}
	
	public void setBranchId(final String branchId) {
		this.branchId = branchId;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
	
	@Override
	public void setNodes(final Collection<TreeNode> nodes) {
		this.nodes = nodes;
	}

}
