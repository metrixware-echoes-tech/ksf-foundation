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
package com.tocea.corolla.products.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tocea.corolla.products.domain.ProjectStatus;

public interface IProjectStatusDAO extends MongoRepository<ProjectStatus, String> {

	/**
	 * Retrieve a ProjectStatus instance by its name
	 * @param name
	 * @return
	 */
	public ProjectStatus findByName(String name);
	
	/**
	 * Returns the default project status
	 * @return the default project status.
	 */
	@Query("{ 'defaultStatus': true }")
	public ProjectStatus getDefaultStatus();
	
}
