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
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.CreateProjectBranchCommand;
import com.tocea.corolla.products.commands.CreateProjectCommand;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.dao.IProjectStatusDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectStatus;
import com.tocea.corolla.products.events.EventNewProjectCreated;
import com.tocea.corolla.products.exceptions.InvalidProjectInformationException;
import com.tocea.corolla.products.exceptions.MissingProjectInformationException;
import com.tocea.corolla.products.exceptions.ProjectAlreadyExistException;
import com.tocea.corolla.revisions.services.IRevisionService;

@CommandHandler
@Transactional
public class CreateProjectCommandHandler implements ICommandHandler<CreateProjectCommand, Project> {

	@Autowired
	private IProjectDAO projectDAO;
	
	@Autowired
	private IProjectStatusDAO statusDAO;
	
	@Autowired
	private IRevisionService revisionService;
	
	@Autowired
	private Gate gate;
	
	@Override
	public Project handle(@Valid final CreateProjectCommand command) {
		
		final Project project = command.getProject();
		
		if (project == null) {
			throw new MissingProjectInformationException("No data provided to create a project");
		}
		
		if (StringUtils.isNotEmpty(project.getId())) {
			throw new InvalidProjectInformationException("No ID expected");
		}
		
		final Project existingProject = projectDAO.findByKey(project.getKey());
		
		if (existingProject != null) {
			throw new ProjectAlreadyExistException();
		}
		
		// Use the default status if the status is not defined
		if (StringUtils.isEmpty(project.getStatusId())) {
			final ProjectStatus defaultStatus = statusDAO.getDefaultStatus();
			if (defaultStatus != null) {
				project.setStatusId(defaultStatus.getId());
			}
		}
		
		// Store the project in the DB
		projectDAO.save(project);
		
		// Add revision control on the new instance
		revisionService.commit(project);
		
		// Create a new branch
		gate.dispatch(new CreateProjectBranchCommand("Master", project));
		
		// Insert the project in the portfolio tree
		final Integer parentNodeID = command.getParentNodeID();
		
		final EventNewProjectCreated eventNewProjectCreated = new EventNewProjectCreated(project, parentNodeID);
		gate.dispatchEvent(eventNewProjectCreated);

		
		return project;
		
	}
	
}
