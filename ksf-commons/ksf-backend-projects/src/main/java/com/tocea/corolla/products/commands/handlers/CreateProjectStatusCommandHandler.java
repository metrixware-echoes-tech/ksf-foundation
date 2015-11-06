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
package com.tocea.corolla.products.commands.handlers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.CreateProjectStatusCommand;
import com.tocea.corolla.products.dao.IProjectStatusDAO;
import com.tocea.corolla.products.domain.ProjectStatus;
import com.tocea.corolla.products.exceptions.InvalidProjectStatusInformationException;
import com.tocea.corolla.products.exceptions.MissingProjectStatusInformationException;
import com.tocea.corolla.products.exceptions.ProjectStatusAlreadyExistException;

@CommandHandler
@Transactional
public class CreateProjectStatusCommandHandler implements ICommandHandler<CreateProjectStatusCommand, ProjectStatus> {

	@Autowired
	private IProjectStatusDAO statusDAO;
	
	@Override
	public ProjectStatus handle(@Valid CreateProjectStatusCommand command) {
		
		ProjectStatus status = command.getStatus();
		
		if (status == null) {
			throw new MissingProjectStatusInformationException("No data provided to create a project status");
		}
		
		if (StringUtils.isNotEmpty(status.getId())) {
			throw new InvalidProjectStatusInformationException("No ID expected");
		}
		
		ProjectStatus withSameName = statusDAO.findByName(status.getName());
		
		if (withSameName != null) {
			throw new ProjectStatusAlreadyExistException();
		}
		
		statusDAO.save(status);
		
		return status;

	}
	
}
