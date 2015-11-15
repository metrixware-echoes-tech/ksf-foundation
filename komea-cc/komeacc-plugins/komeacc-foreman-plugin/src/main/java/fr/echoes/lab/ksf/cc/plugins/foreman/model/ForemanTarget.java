package fr.echoes.lab.ksf.cc.plugins.foreman.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.tocea.corolla.products.domain.Project;


@Document
public class ForemanTarget {
	@Id
	@Field("_id")
	private String				id;

	@NotEmpty
	@Size(min = 3, max = 100)
	private String name;

	@DBRef
	private ForemanEnvironnment	environment;

	@DBRef
	private Project project;

	@NotBlank
	private String	operatingSystemName;

	@NotBlank
	private String	computeProfile;

	private String operatingSystemId;

	public String getComputeProfile() {
		return this.computeProfile;
	}

	public ForemanEnvironnment getEnvironment() {
		return this.environment;
	}

	public String getId() {
		return this.id;
	}

	public String getOperatingSystemName() {
		return this.operatingSystemName;
	}

	public void setComputeProfile(final String _computeProfile) {
		this.computeProfile = _computeProfile;
	}

	public void setEnvironment(final ForemanEnvironnment _environment) {
		this.environment = _environment;
	}

	public void setId(final String _id) {
		this.id = _id;
	}

	public void setOperatingSystemName(final String _operatingSystemName) {
		this.operatingSystemName = _operatingSystemName;
	}

	public void setOperatingSystemId(final String _operatingSystemId) {
		this.operatingSystemId = _operatingSystemId;
	}

	public String getOperationSystemId() {
		return this.operatingSystemId;
	}

	@Override
	public String toString() {
		return "ForemanTarget [id=" + this.id + ", environment=" + this.environment + ", operatingSystem=" + this.operatingSystemName
				+ ", computeProfile=" + this.computeProfile + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
