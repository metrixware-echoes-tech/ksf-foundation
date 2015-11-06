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
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.requirements.commands.DeleteRequirementCommand;
import com.tocea.corolla.requirements.dao.IRequirementDAO;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.requirements.exceptions.MissingRequirementInformationException;
import com.tocea.corolla.requirements.exceptions.RequirementNotFoundException;

@CommandHandler
@Transactional
public class DeleteRequirementCommandHandler implements ICommandHandler<DeleteRequirementCommand, Requirement> {

	@Autowired
	private IRequirementDAO requirementDAO;
	
	@Override
	public Requirement handle(@Valid DeleteRequirementCommand command) {
		
		String requirementID = command.getRequirementID();
		
		if (StringUtils.isEmpty(requirementID)) {
			throw new MissingRequirementInformationException("No requirement ID found");
		}
		
		Requirement requirement = requirementDAO.findOne(requirementID);
		
		if (requirement == null) {
			throw new RequirementNotFoundException();
		}
		
		requirementDAO.delete(requirement);
		
		return requirement;
		
	}

}
