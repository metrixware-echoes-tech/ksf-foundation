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
package com.tocea.corolla.trees.rest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.trees.commands.DeleteFolderNodeTypeCommand;
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO;
import com.tocea.corolla.trees.domain.FolderNodeType;
import com.tocea.corolla.users.domain.Permission;

@RestController
@RequestMapping("/rest/trees/folders/types")
public class FolderNodeTypeRestController {

	@Autowired
	private IFolderNodeTypeDAO folderNodeTypeDAO;
	
	@Autowired
	private Gate gate;
	
	@RequestMapping(value = "/all")
	public Collection<FolderNodeType> findAll() {
		
		return folderNodeTypeDAO.findAll();
	}
	
	@Secured(Permission.ADMIN)
	@RequestMapping(value = "/delete/{typeID}")
	public FolderNodeType delete(@PathVariable String typeID) {
		
		return gate.dispatch(new DeleteFolderNodeTypeCommand(typeID));
	}
	
}
