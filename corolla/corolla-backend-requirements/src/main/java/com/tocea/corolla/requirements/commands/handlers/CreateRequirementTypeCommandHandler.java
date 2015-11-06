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
import com.tocea.corolla.requirements.commands.CreateRequirementTypeCommand;
import com.tocea.corolla.requirements.dao.IRequirementTypeDAO;
import com.tocea.corolla.requirements.domain.RequirementType;
import com.tocea.corolla.requirements.exceptions.InvalidRequirementTypeInformationException;
import com.tocea.corolla.requirements.exceptions.MissingRequirementTypeInformationException;
import com.tocea.corolla.requirements.exceptions.RequirementTypeAlreadyExistException;

@CommandHandler
@Transactional
public class CreateRequirementTypeCommandHandler implements ICommandHandler<CreateRequirementTypeCommand, RequirementType> {

	@Autowired
	private IRequirementTypeDAO typeDAO;
	
	@Override
	public RequirementType handle(@Valid CreateRequirementTypeCommand command) {
		
		RequirementType type = command.getType();
		
		if (type == null) {
			throw new MissingRequirementTypeInformationException("No data found for creating a requirement type");
		}
		
		if (StringUtils.isNotEmpty(type.getId())) {
			throw new InvalidRequirementTypeInformationException("No ID expected");
		}
		
		RequirementType withSameKey = typeDAO.findByKey(type.getKey());
		
		if (withSameKey != null) {
			throw new RequirementTypeAlreadyExistException();
		}
		
		type.setActive(true);
		
		typeDAO.save(type);
		
		return type;
		
	}

}
