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

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.CreateProjectBranchCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.events.EventNewBranchCreated;
import com.tocea.corolla.products.exceptions.InvalidProjectBranchInformationException;
import com.tocea.corolla.products.exceptions.MissingProjectBranchInformationException;
import com.tocea.corolla.products.exceptions.ProjectBranchAlreadyExistException;

@CommandHandler
@Transactional
public class CreateProjectBranchCommandHandler implements ICommandHandler<CreateProjectBranchCommand, ProjectBranch> {
	
	@Autowired
	private IProjectBranchDAO branchDAO;
	
	@Autowired
	private Gate gate;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateProjectBranchCommandHandler.class);
	
	@Override
	public ProjectBranch handle(@Valid final CreateProjectBranchCommand command) {
		
		final ProjectBranch branch = command.getBranch();
		
		if (branch == null) {
			throw new MissingProjectBranchInformationException("No data provided to create a project branch");
		}
		
		if (StringUtils.isNotEmpty(branch.getId())) {
			throw new InvalidProjectBranchInformationException("No ID expected");
		}
		
		final ProjectBranch withSameName = branchDAO.findByNameAndProjectId(branch.getName(), branch.getProjectId());
		
		if (withSameName != null) {
			throw new ProjectBranchAlreadyExistException();
		}
		
		final Collection<ProjectBranch> otherBranches = branchDAO.findByProjectId(branch.getProjectId());
		
		if (otherBranches.isEmpty()) {
			branch.setDefaultBranch(true);
		}
		
		branchDAO.save(branch);
		LOGGER.info("branch {} created for project {}", branch.getName(), branch.getProjectId());
		
		gate.dispatchEvent(new EventNewBranchCreated(branch, command.getOriginBranch()));
		
		
		return branch;
		
	}
		
}