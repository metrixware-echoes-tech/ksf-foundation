package com.tocea.corolla.products.commands;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.tocea.corolla.cqrs.annotations.CommandOptions;
import com.tocea.corolla.products.domain.Project;

/**
 * @author dcollard
 *
 */
@CommandOptions
public class CloseFeatureCommand {

	@NotNull
	private Project project;

	@NotEmpty
	private String featureId;
	
	@NotEmpty
	private String featureSubject;	
	
	public CloseFeatureCommand(Project project, String featureId, String featureSubject) {
		this.project = project;
		this.featureId = featureId;
		this.featureSubject = featureSubject;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return this.project;
	}

	/**
	 * @return the featureId
	 */
	public String getFeatureId() {
		return featureId;
	}

	/**
	 * @return the featureSubject
	 */
	public String getFeatureSubject() {
		return featureSubject;
	}

}
