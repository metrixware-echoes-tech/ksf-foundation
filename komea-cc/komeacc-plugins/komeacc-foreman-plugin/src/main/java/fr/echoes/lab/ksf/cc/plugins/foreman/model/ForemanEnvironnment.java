package fr.echoes.lab.ksf.cc.plugins.foreman.model;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fr.echoes.lab.ksf.cc.plugins.foreman.ForemanValidationConstants;

@Document
public class ForemanEnvironnment implements Serializable {

	@Id
	@Field("_id")
	private String id;

	@NotEmpty
	@Size(min = ForemanValidationConstants.NAME_MIN_LENGTH, max = ForemanValidationConstants.NAME_MAX_LENGTH)
	@Pattern(regexp = ForemanValidationConstants.ENVIRONMENT_NAME_PATTERN, message=ForemanValidationConstants.INCORRECT_ENVIRONMENT_NAME)
	private String name;

	private String configuration;

	@NotEmpty
	private String projectId;

	public String getConfiguration() {

		return this.configuration;
	}

	public String getId() {

		return this.id;
	}

	public String getName() {

		return this.name;
	}

	public void setConfiguration(final String _configuration) {

		this.configuration = _configuration;
	}

	public void setId(final String _id) {

		this.id = _id;
	}

	public void setName(final String _name) {

		this.name = _name;
	}

	public String getProjectId() {

		return this.projectId;
	}

	public void setProjectId(String projectId) {

		this.projectId = projectId;
	}

	@Override
	public String toString() {
		return "ForemanEnvironnment [id=" + this.id + ", name=" + this.name + ", configuration=" + this.configuration + "]";
	}
}
