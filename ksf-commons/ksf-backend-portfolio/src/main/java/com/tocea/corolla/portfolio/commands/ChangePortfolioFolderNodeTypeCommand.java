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
package com.tocea.corolla.portfolio.commands;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.tocea.corolla.cqrs.annotations.CommandOptions;

@CommandOptions
public class ChangePortfolioFolderNodeTypeCommand {

	@NotNull
	private Integer nodeID;
	
	@NotBlank
	private String typeID;
	
	public ChangePortfolioFolderNodeTypeCommand() {
		super();
	}
	
	public ChangePortfolioFolderNodeTypeCommand(Integer nodeID, String typeID) {
		super();
		setNodeID(nodeID);
		setTypeID(typeID);
	}

	public Integer getNodeID() {
		return nodeID;
	}

	public void setNodeID(Integer nodeID) {
		this.nodeID = nodeID;
	}

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}
	
}
