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
package com.tocea.corolla.revisions.domain;

import java.util.Date;

import org.javers.core.metamodel.object.CdoSnapshot;

public class Commit implements ICommit {

	private String id;
	
	private Date date;
	
	private String author;
	
	private String objectID;
	
	private Class<?> objectClass;
	
	private String type;

	public Commit() {
		
	}
	
	public Commit(String objectID, Class<?> objectClass, CdoSnapshot snapshot) {
		
		this.id = snapshot.getCommitId().valueAsNumber().toBigInteger().toString(); //snapshot.getCommitId().value();
		this.author = snapshot.getCommitMetadata().getAuthor();
		this.date = snapshot.getCommitMetadata().getCommitDate().toDate();
		this.objectID = objectID;
		this.objectClass = objectClass;
		this.type = snapshot.getType().name();
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public Class<?> getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(Class<?> objectClass) {
		this.objectClass = objectClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
