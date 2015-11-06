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
package com.tocea.corolla.requirements.commands.handlers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.RestoreProjectStateCommand;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.revisions.exceptions.InvalidCommitInformationException;
import com.tocea.corolla.revisions.services.IRevisionService;

@CommandHandler
@Transactional
public class RestoreProjectStateCommandHandler implements ICommandHandler<RestoreProjectStateCommand, Project> {

	@Autowired
	private IProjectDAO projectDAO;
	
	@Autowired
	private IRevisionService revisionService;
	
	@Override
	public Project handle(@Valid RestoreProjectStateCommand command) {
		
		Project project = command.getProject();
		String commitID = command.getCommitID();
		
		Project restoredVersion = (Project) revisionService.getSnapshot(project.getId(), Project.class, commitID);
		
		if (restoredVersion == null) {
			throw new InvalidCommitInformationException("Cannot restore the project to the given commit ID");
		}
		
		projectDAO.save(restoredVersion);
		revisionService.commit(restoredVersion);
		
		return project;
	}

}