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
package com.tocea.corolla.revisions.domain;

import java.util.Date;

import org.javers.core.metamodel.object.CdoSnapshot;

public class Commit implements ICommit {

	private String		id;

	private Date		date;

	private String		author;

	private String		objectID;

	private Class<?>	objectClass;

	private String		type;

	public Commit() {

	}

	public Commit(final String objectID, final Class<?> objectClass, final CdoSnapshot snapshot) {

		this.id = snapshot.getCommitId().valueAsNumber().toBigInteger().toString(); // snapshot.getCommitId().value();
		this.author = snapshot.getCommitMetadata().getAuthor();
		this.date = snapshot.getCommitMetadata().getCommitDate().toDate();
		this.objectID = objectID;
		this.objectClass = objectClass;
		this.type = snapshot.getType().name();

	}

	@Override
	public String getAuthor() {
		return this.author;
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public Class<?> getObjectClass() {
		return this.objectClass;
	}

    /**
     *
     * @return
     */
     
	public String getObjectID() {
		return this.objectID;
	}

	@Override
	public String getType() {
		return this.type;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setObjectClass(final Class<?> objectClass) {
		this.objectClass = objectClass;
	}

	public void setObjectID(final String objectID) {
		this.objectID = objectID;
	}

	public void setType(final String type) {
		this.type = type;
	}

}
