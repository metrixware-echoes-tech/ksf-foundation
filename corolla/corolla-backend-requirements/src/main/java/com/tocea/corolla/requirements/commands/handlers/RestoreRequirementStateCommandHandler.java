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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.requirements.commands.EditRequirementCommand;
import com.tocea.corolla.requirements.commands.RestoreRequirementStateCommand;
import com.tocea.corolla.requirements.dao.IRequirementDAO;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.requirements.exceptions.MissingRequirementInformationException;
import com.tocea.corolla.requirements.exceptions.RequirementNotFoundException;
import com.tocea.corolla.revisions.exceptions.MissingCommitInformationException;
import com.tocea.corolla.revisions.services.IRevisionService;

@CommandHandler
@Transactional
public class RestoreRequirementStateCommandHandler implements ICommandHandler<RestoreRequirementStateCommand, Requirement> {
	
	@Autowired
	private IRequirementDAO requirementDAO;

	@Autowired
	private IRevisionService revisionService;

	@Autowired
	private Gate gate;

	@Override
	public Requirement handle(@Valid final RestoreRequirementStateCommand command) {

		final String requirementID = command.getRequirementID();

		if (StringUtils.isEmpty(requirementID)) {
			throw new MissingRequirementInformationException("No requirement ID found");
		}

		final String commitID = command.getCommitID();

		if (StringUtils.isEmpty(commitID)) {
			throw new MissingCommitInformationException("No commit ID found");
		}

		final Requirement requirement = requirementDAO.findOne(requirementID);

		if (requirement == null) {
			throw new RequirementNotFoundException();
		}

		final Requirement restored = (Requirement) revisionService.getSnapshot(requirementID, Requirement.class, commitID);

		gate.dispatchAsync(new EditRequirementCommand(restored));

		return restored;

	}

}