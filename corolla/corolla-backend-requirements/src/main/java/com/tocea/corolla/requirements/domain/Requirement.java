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
import java.lang.reflect.InvocationTargetException;

import javax.validation.constraints.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tocea.corolla.utils.domain.ObjectValidation;

@Document
public class Requirement implements Serializable {

	@Id
	@Field("_id")
	private String id;
	
	@NotBlank
	@Pattern(regexp=ObjectValidation.URL_SAFE_PATTERN)
	private String key;
	
	@NotBlank
	private String projectBranchId;
	
	@NotBlank
	private String name;
	
	private String description;
	
	private String typeID;
	
	private String statusID;
	
	private String versionNumber = "1.0";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String type) {
		this.typeID = type;
	}

	public String getProjectBranchId() {
		return projectBranchId;
	}

	public void setProjectBranchId(String projectBranchId) {
		this.projectBranchId = projectBranchId;
	}

	public String getStatusID() {
		return statusID;
	}

	public void setStatusID(String statusID) {
		this.statusID = statusID;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String revisionNumber) {
		this.versionNumber = revisionNumber;
	}
	
}