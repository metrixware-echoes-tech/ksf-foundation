package fr.echoes.labs.ksf.cc.sf.domain;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class SFApplication {

	@Id
	@Field("_id")
	private String id;
	
	@NotNull
	private SFApplicationType type;
	
	@NotNull
	private SoftwareFactory softwareFactory;
	
	private String name;
	
	private String url;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public SFApplicationType getType() {
		return type;
	}
	
	public void setType(SFApplicationType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public SoftwareFactory getSoftwareFactory() {
		return softwareFactory;
	}
	
	public void setSoftwareFactory(SoftwareFactory softwareFactory) {
		this.softwareFactory = softwareFactory;
	}
	
}
