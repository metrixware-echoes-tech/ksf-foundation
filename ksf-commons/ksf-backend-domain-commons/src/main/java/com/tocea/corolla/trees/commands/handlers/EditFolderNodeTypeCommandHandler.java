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
import com.tocea.corolla.trees.commands.EditFolderNodeTypeCommand;
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO;
import com.tocea.corolla.trees.domain.FolderNodeType;
import com.tocea.corolla.trees.exceptions.InvalidFolderNodeTypeInformationException;
import com.tocea.corolla.trees.exceptions.MissingFolderNodeTypeInformationException;

@CommandHandler
@Transactional
public class EditFolderNodeTypeCommandHandler implements ICommandHandler<EditFolderNodeTypeCommand, FolderNodeType> {

	@Autowired
	private IFolderNodeTypeDAO folderNodeTypeDAO;
	
	@Override
	public FolderNodeType handle(@Valid EditFolderNodeTypeCommand command) {
		
		FolderNodeType type = command.getFolderNodeType();
		
		if (type == null) {
			throw new MissingFolderNodeTypeInformationException("No data found to edit a folder node type");
		}
		
		if (StringUtils.isEmpty(type.getId())) {
			throw new InvalidFolderNodeTypeInformationException("No ID found on this folder node type");
		}
		
		folderNodeTypeDAO.save(type);
		
		return type;
	}

}
