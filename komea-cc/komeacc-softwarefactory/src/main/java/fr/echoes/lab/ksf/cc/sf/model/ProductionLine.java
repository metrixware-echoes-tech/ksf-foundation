package fr.echoes.lab.ksf.cc.sf.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.github.fakemongo.impl.aggregation.Project;

@Document
public class ProductionLine {
	
	@Id
	@Field("_id")
	private String id;

	@DBRef
	private Project project;
	
	@DBRef
	private List<SFApplication> applications;
	
	public Project getProject() {
		return project;
	}	

	public void setProject(Project project) {
		this.project = project;
	}
	
	public List<SFApplication> getApplications() {
		return applications;
	}
	
	public void setApplications(List<SFApplication> applications) {
		this.applications = applications;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
}
