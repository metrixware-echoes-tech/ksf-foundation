/*
 * Corolla - A Tool to manage software requirements and test cases
 * Copyright (C) 2015 Tocea
 * This file is part of Corolla.
 * Corolla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License,
 * or any later version.
 * Corolla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Corolla. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.products.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tocea.corolla.products.domain.Project;

public interface IProjectDAO extends MongoRepository<Project, String> {

	/**
	 * Search by categories
	 *
	 * @param categoryIds
	 * @return
	 */
	@Query("{ 'categoryId' : { $in: ?0 } }")
	List<Project> filterByCategories(List<String> categoryIds);

	/**
	 * Search by owners
	 *
	 * @param ownerIds
	 * @return
	 */
	@Query("{ 'ownerId' : { $in: ?0 } }")
	List<Project> filterByOwner(List<String> ownerIds);

	/**
	 * Search by statuses
	 *
	 * @param statusIds
	 * @return
	 */
	@Query("{ 'statusId' : { $in: ?0 } }")
	List<Project> filterByStatuses(List<String> statusIds);

	/**
	 * Search by tags
	 *
	 * @param tags
	 * @return
	 */
	@Query("{ 'tags' : { $in: ?0 } }")
	List<Project> filterByTags(List<String> tags);

	/**
	 * Find all projects by order by name asc.
	 *
	 * @return the list
	 */
	List<Project> findAllByOrderByNameAsc();

	/**
	 * Retrieve a project by its key
	 *
	 * @param key
	 * @return
	 */
	Project findByKey(String key);

}