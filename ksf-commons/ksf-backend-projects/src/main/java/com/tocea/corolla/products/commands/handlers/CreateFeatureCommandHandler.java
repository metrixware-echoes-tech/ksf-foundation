package com.tocea.corolla.products.commands.handlers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.CreateFeatureCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventFeatureCreated;
import com.tocea.corolla.products.exceptions.ProjectNotFoundException;


/**
 * @author dcollard
 *
 */
@CommandHandler
@Transactional
public class CreateFeatureCommandHandler implements ICommandHandler<CreateFeatureCommand, Project> {

	private static final Logger LOG = LoggerFactory.getLogger(CreateFeatureCommandHandler.class);

	@Autowired
	private IProjectDAO projectDAO;

	@Autowired
	private IProjectBranchDAO branchDAO;

	@Autowired
	private Gate gate;

	/**
	 * Treats the command "When a project feature is created"
	 *
	 * @param command
	 * @return
	 */
	@Override
	public Project handle(@Valid final CreateFeatureCommand command) {

		final Project project = command.getProject();
		final String username = command.getUsername();
		final String featureId = command.getFeatureId();
		final String featureSubject = command.getFeatureSubject();

		if (project == null) {
			throw new ProjectNotFoundException();
		}

		this.gate.dispatchEvent(new EventFeatureCreated(project, username, featureId, featureSubject));


		return project;

	}

}
