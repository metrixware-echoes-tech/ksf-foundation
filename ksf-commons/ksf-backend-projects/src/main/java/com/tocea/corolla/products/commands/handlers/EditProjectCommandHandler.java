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
import com.tocea.corolla.products.commands.EditProjectCommand;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventProjectUpdated;
import com.tocea.corolla.products.exceptions.InvalidProjectInformationException;
import com.tocea.corolla.products.exceptions.MissingProjectInformationException;
import com.tocea.corolla.products.exceptions.ProjectAlreadyExistException;
import com.tocea.corolla.revisions.services.IRevisionService;

@CommandHandler
@Transactional
public class EditProjectCommandHandler implements ICommandHandler<EditProjectCommand, Project> {

	@Autowired
	private IProjectDAO projectDAO;
	
	@Autowired
	private IRevisionService revisionService;
	
	@Autowired
	private Gate gate;
	
	@Override
	public Project handle(@Valid final EditProjectCommand command) {
		
		final Project project = command.getProject();
		
		if (project == null) {
			throw new MissingProjectInformationException("No data provided to edit a project");
		}
		
		if (StringUtils.isEmpty(project.getId())) {
			throw new InvalidProjectInformationException("No ID found");
		}

		final Project existingProject = projectDAO.findByKey(project.getKey());
		
		if (existingProject != null && !existingProject.getId().equals(project.getId())) {
			throw new ProjectAlreadyExistException();
		}
		
		projectDAO.save(project);
		
		revisionService.commit(project);
		gate.dispatchEvent(new EventProjectUpdated(project));
		return project;
		
	}
	
}
