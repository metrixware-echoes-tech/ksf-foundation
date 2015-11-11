package fr.echoes.lab.ksf.cc.plugins.foreman.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document
public class ForemanTarget {
	@Id
	@Field("_id")
	private String				id;
	@DBRef
	private ForemanEnvironnment	environment;
	
	@NotBlank
	private String	operatingSystem;
	
	@NotBlank
	private String	computeProfile;
	public String getComputeProfile() {
		return computeProfile;
	}
	
	public ForemanEnvironnment getEnvironment() {
		return environment;
	}
	
	public String getId() {
		return id;
	}
	
	public String getOperatingSystem() {
		return operatingSystem;
	}
	
	public void setComputeProfile(final String _computeProfile) {
		computeProfile = _computeProfile;
	}
	
	public void setEnvironment(final ForemanEnvironnment _environment) {
		environment = _environment;
	}
	
	public void setId(final String _id) {
		id = _id;
	}
	
	public void setOperatingSystem(final String _operatingSystem) {
		operatingSystem = _operatingSystem;
	}
	
	@Override
	public String toString() {
		return "ForemanTarget [id=" + id + ", environment=" + environment + ", operatingSystem=" + operatingSystem
				+ ", computeProfile=" + computeProfile + "]";
	}
}
