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

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tocea.corolla.products.domain.ProjectBranch;

public interface IProjectBranchDAO extends MongoRepository<ProjectBranch, String> {

	/**
	 * Retrieves a ProjectBranch instance from its name and its project id
	 * @param name
	 * @return
	 */
	public ProjectBranch findByNameAndProjectId(String name, String projectId);
	
	/**
	 * Retrieves all ProjectBranch instances from a project id
	 * @param projectId
	 * @return
	 */
	public Collection<ProjectBranch> findByProjectId(String projectId);
	
	/**
	 * Retrieves the default branch of a given project
	 * @param projectId
	 * @return
	 */
	@Query(value="{ 'projectId': ?0, 'defaultBranch': true }")
	public ProjectBranch findDefaultBranch(String projectId);
	
}
