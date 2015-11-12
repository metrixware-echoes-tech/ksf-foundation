package fr.echoes.lab.ksf.cc.sf.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class SFProjectDTO {
	
	private String id;
	
	private String key;
	
	@NotEmpty
	private String name;
	
	private List<String> applications;

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
	
	public List<String> getApplications() {
		return applications;
	}

	public void setApplications(List<String> applications) {
		this.applications = applications;
	}
	
}
