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
package com.tocea.corolla.trees.commands.handlers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.trees.commands.CreateFolderNodeTypeCommand;
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO;
import com.tocea.corolla.trees.domain.FolderNodeType;
import com.tocea.corolla.trees.exceptions.InvalidFolderNodeTypeInformationException;
import com.tocea.corolla.trees.exceptions.MissingFolderNodeTypeInformationException;

@CommandHandler
@Transactional
public class CreateFolderNodeTypeCommandHandler implements ICommandHandler<CreateFolderNodeTypeCommand, FolderNodeType> {

	@Autowired
	private IFolderNodeTypeDAO folderNodeTypeDAO;
	
	@Override
	public FolderNodeType handle(@Valid CreateFolderNodeTypeCommand command) {
		
		FolderNodeType nodeType = command.getNodeType();
		
		if (nodeType == null) {
			throw new MissingFolderNodeTypeInformationException("No folder node type found");
		}
		
		if (StringUtils.isNotEmpty(nodeType.getId())) {
			throw new InvalidFolderNodeTypeInformationException("No ID expected");
		}
		
		nodeType.setId(null);
		
		folderNodeTypeDAO.save(nodeType);
		
		return nodeType;
	}

}
