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
import com.tocea.corolla.products.commands.DeleteProjectBranchCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.events.EventBranchDeleted;
import com.tocea.corolla.products.exceptions.InvalidProjectBranchInformationException;
import com.tocea.corolla.products.exceptions.MissingProjectBranchInformationException;
import com.tocea.corolla.products.exceptions.ProjectBranchOperationForbiddenException;

@CommandHandler
@Transactional
public class DeleteProjectBranchCommandHandler implements ICommandHandler<DeleteProjectBranchCommand, ProjectBranch> {

	@Autowired
	private IProjectBranchDAO branchDAO;
	
	
	@Autowired
	private IProjectDAO projectDAO;
	
	@Autowired
	private Gate gate;
	
	@Override
	public ProjectBranch handle(@Valid final DeleteProjectBranchCommand command) {
		
		final ProjectBranch branch = command.getBranch();
		
		if (branch == null) {
			throw new MissingProjectBranchInformationException("No data provided to delete a branch");
		}
		
		if (StringUtils.isEmpty(branch.getId())) {
			throw new InvalidProjectBranchInformationException("No ID found on the given branch");
		}
		
		if (branch.getDefaultBranch()) {
			
			// The only way to delete the default branch of a project is to delete the project first
			final Project project = projectDAO.findOne(branch.getProjectId());
			
			if (project != null) {
				throw new ProjectBranchOperationForbiddenException("Cannot delete the default branch");
			}
			
		}
		
		
		// Delete the branch
		branchDAO.delete(branch);
		
		gate.dispatchEvent(new EventBranchDeleted(branch));
		
		return branch;
	}

}