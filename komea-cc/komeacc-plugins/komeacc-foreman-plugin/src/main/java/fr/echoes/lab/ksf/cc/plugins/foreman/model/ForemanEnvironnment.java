package fr.echoes.lab.ksf.cc.plugins.foreman.model;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.tocea.corolla.utils.domain.ObjectValidation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class ForemanEnvironnment implements Serializable {

	@Id
	@Field("_id")
	private String id;

	@NotEmpty
	@Size(min = 3, max = 100)
	@Pattern(regexp = ObjectValidation.URL_SAFE_PATTERN)
	private String name;

	private String configuration;

	@NotEmpty
	private String projectId;

	public String getConfiguration() {

		return configuration;
	}

	public String getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	public void setConfiguration(final String _configuration) {

		configuration = _configuration;
	}

	public void setId(final String _id) {

		id = _id;
	}

	public void setName(final String _name) {

		name = _name;
	}

	public String getProjectId() {

		return projectId;
	}

	public void setProjectId(String projectId) {

		this.projectId = projectId;
	}

	@Override
	public String toString() {
		return "ForemanEnvironnment [id=" + id + ", name=" + name + ", configuration=" + configuration + "]";
	}
}
