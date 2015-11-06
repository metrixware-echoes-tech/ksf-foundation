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
package com.tocea.corolla.products.dto;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class ProjectFilterDTO {

	private List<String> categoryIds;
	
	private List<String> statusIds;
	
	private List<String> ownerIds;
	
	private List<String> tags;
	
	public ProjectFilterDTO() {
		
	}
	
	public boolean isEmpty() {
		return 
				CollectionUtils.isEmpty(categoryIds)
				&& CollectionUtils.isEmpty(statusIds)
				&& CollectionUtils.isEmpty(ownerIds)
				&& CollectionUtils.isEmpty(tags);
	}

	public List<String> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<String> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<String> getStatusIds() {
		return statusIds;
	}

	public void setStatusIds(List<String> statusIds) {
		this.statusIds = statusIds;
	}

	public List<String> getOwnerIds() {
		return ownerIds;
	}

	public void setOwnerIds(List<String> ownerIds) {
		this.ownerIds = ownerIds;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}