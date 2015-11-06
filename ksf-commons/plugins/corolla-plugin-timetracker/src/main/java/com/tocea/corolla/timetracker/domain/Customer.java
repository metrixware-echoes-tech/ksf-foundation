/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.tocea.corolla.timetracker.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tocea.corolla.products.domain.Project;

@Document
public class Customer {
	private static final long serialVersionUID = 7179070624535327915L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Field("_id")
	@Id
	private Integer customerId;

	@NotNull
	private String code;

	@NotNull
	private String name;


	@Column(length = 2048)
	private String description;


	// Constructors

	@Embedded
	private Set<Project> projects;

	/**
	 * default constructor
	 */
	public Customer() {
	}

	public String getCode() {
		return code;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setCode(final String _code) {
		code = _code;
	}

	public void setCustomerId(final Integer _customerId) {
		customerId = _customerId;
	}

	public void setDescription(final String _description) {
		description = _description;
	}

	public void setName(final String _name) {
		name = _name;
	}

	public void setProjects(final Set<Project> _projects) {
		projects = _projects;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", code=" + code + ", name=" + name + ", description="
				+ description + ", projects=" + projects + "]";
	}

}