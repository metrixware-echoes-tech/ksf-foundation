package com.tocea.corolla.products.commands.handlers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.CreateReleaseCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventReleaseCreated;
import com.tocea.corolla.products.exceptions.ProjectNotFoundException;


/**
 *
 * @author tsaquet
 */
@CommandHandler
@Transactional
public class CreateReleaseCommandHandler implements ICommandHandler<CreateReleaseCommand, Project> {

	private static final Logger LOG = LoggerFactory.getLogger(CreateReleaseCommandHandler.class);

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private IProjectBranchDAO branchDAO;

	@Autowired
	private Gate gate;

	/**
	 * Treats the command "When a project is deleted"
	 *
	 * @param command
	 * @return
	 */
	@Override
	public Project handle(@Valid final CreateReleaseCommand command) {

		final Project project = command.getProject();
		final String releaseVersion = command.getReleaseVersion();

		if (project == null) {
			throw new ProjectNotFoundException();
		}

		this.gate.dispatchEvent(new EventReleaseCreated(project, releaseVersion));


		return project;

	}

}
