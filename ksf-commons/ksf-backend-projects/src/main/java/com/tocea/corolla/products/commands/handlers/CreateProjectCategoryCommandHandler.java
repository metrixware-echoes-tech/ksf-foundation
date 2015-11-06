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
import com.tocea.corolla.products.commands.CreateProjectCategoryCommand;
import com.tocea.corolla.products.dao.IProjectCategoryDAO;
import com.tocea.corolla.products.domain.ProjectCategory;
import com.tocea.corolla.products.exceptions.InvalidProjectCategoryInformationException;
import com.tocea.corolla.products.exceptions.MissingProjectCategoryInformationException;
import com.tocea.corolla.products.exceptions.ProjectCategoryAlreadyExistException;

@CommandHandler
@Transactional
public class CreateProjectCategoryCommandHandler implements ICommandHandler<CreateProjectCategoryCommand, ProjectCategory> {

	@Autowired
	private IProjectCategoryDAO categoryDAO;
	
	@Override
	public ProjectCategory handle(@Valid CreateProjectCategoryCommand command) {
		
		ProjectCategory category = command.getCategory();
		
		if (category == null) {
			throw new MissingProjectCategoryInformationException("No category found");
		}
		
		if (StringUtils.isEmpty(category.getName())) {
			throw new MissingProjectCategoryInformationException("Cannot create a project category without a name");
		}
		
		if (StringUtils.isNotEmpty(category.getId())) {
			throw new InvalidProjectCategoryInformationException("No ID expected");
		}
		
		ProjectCategory withSameName = categoryDAO.findByName(category.getName());
		
		if (withSameName != null) {
			throw new ProjectCategoryAlreadyExistException();
		}
		
		categoryDAO.save(category);
		
		return category;
	}

}