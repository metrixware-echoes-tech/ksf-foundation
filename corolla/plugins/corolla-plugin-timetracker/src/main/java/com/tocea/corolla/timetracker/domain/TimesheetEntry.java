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
package com.tocea.corolla.timetracker.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.users.domain.User;
import java.util.Collections;

/**
 * This class defines the data required to manipulate a timesheet entry.
 * @author sleroy
 */
@Document
public class TimesheetEntry implements Serializable {

	@Id
	@Field("_id")
	private String id;
	
	@DBRef
	@Indexed
	private Project project;
	
	@DBRef
	@Indexed
	@NotNull
	private User user;

	@Column(length = 2048)
	private String comment;

	@NotNull
	private Activity			activity;

	@Indexed
	@NotNull
	private Date				entryDate;

	@NotNull
	private Date				updateDate;

	@NotNull
	private Float				hours;

	@Embedded
	private final List<String>	tags	= Lists.newArrayList();

	private String ticket;

	public Activity getActivity() {
		return activity;
	}

	public String getComment() {
		return comment;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public Float getHours() {
		return hours;
	}

	public String getId() {
		return id;
	}



	public Project getProject() {
		return project;
	}

	public List<String> getTags() {
		return Collections.unmodifiableList(tags);
	}

	public String getTicket() {
		return ticket;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public User getUser() {
		return user;
	}

	public void setActivity(final Activity _activity) {
		activity = _activity;
	}

	public void setComment(final String _comment) {
		comment = _comment;
	}

	public void setEntryDate(final Date _entryDate) {
		entryDate = _entryDate;
	}


	public void setHours(final Float _hours) {
		hours = _hours;
	}

	public void setId(final String _id) {
		id = _id;
	}

	public void setProject(final Project _project) {
		project = _project;
	}

	public void setTicket(final String _ticket) {
		ticket = _ticket;
	}

	public void setUpdateDate(final Date _updateDate) {
		updateDate = _updateDate;
	}

	public void setUser(final User _user) {
		user = _user;
	}

	@Override
	public String toString() {
		return "TimesheetEntry [id=" + id + ", project=" + project + ", user=" + user + ", comment=" + comment
				+ ", activity=" + activity + ", entryDate=" + entryDate + ", updateDate=" + updateDate + ", hours="
				+ hours + ", tags=" + tags + ", ticket=" + ticket + "]";
	}
}