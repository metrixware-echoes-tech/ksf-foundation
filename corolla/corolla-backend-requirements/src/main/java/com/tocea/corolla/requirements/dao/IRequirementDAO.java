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
package com.tocea.corolla.requirements.dao;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tocea.corolla.requirements.domain.Requirement;

public interface IRequirementDAO extends MongoRepository<Requirement, String> {

	/**
	 * Retrieves a requirement by its key and its project branch id
	 * @param key
	 * @return
	 */
	public Requirement findByKeyAndProjectBranchId(String key, String projectBranchId);
	
	/**
	 * Retrieves all the requirements attached to a project branch
	 * @param projectBranchId
	 * @return
	 */
	public Collection<Requirement> findByProjectBranchId(String projectBranchId);
	
}
