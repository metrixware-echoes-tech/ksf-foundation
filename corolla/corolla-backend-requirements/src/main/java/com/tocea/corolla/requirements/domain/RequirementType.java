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
package com.tocea.corolla.requirements.domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class RequirementType implements Serializable {
	
	@Id
	@Field("_id")
	private String id;

	@NotEmpty
	private String key;

	@NotEmpty
	private String name;

	private String icon;

	private Boolean active;
	
	public Boolean getActive() {
		return active;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public String getId() {
		return id;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setActive(final Boolean active) {
		this.active = active;
	}
	
	public void setIcon(final String icon) {
		this.icon = icon;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
	
	public void setKey(final String key) {
		this.key = key;
	}
	
	public void setName(final String name) {
		this.name = name;
	}

}
