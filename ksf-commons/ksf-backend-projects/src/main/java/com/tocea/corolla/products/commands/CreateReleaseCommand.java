package com.tocea.corolla.products.commands;

import javax.validation.constraints.NotNull;

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

	public CreateReleaseCommand() {

	}

	public CreateReleaseCommand(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
