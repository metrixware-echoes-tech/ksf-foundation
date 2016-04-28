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
public class CreateReleaseCommand {

	@NotNull
	private Project project;

	@NotEmpty
	private String releaseVersion;

	public CreateReleaseCommand(Project project, String releaseVersion) {
		this.project = project;
		this.releaseVersion = releaseVersion;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getReleaseVersion() {
		return this.releaseVersion;
	}

}
