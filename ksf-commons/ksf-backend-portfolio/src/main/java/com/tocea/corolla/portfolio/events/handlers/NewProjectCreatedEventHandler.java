package com.tocea.corolla.portfolio.events.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.portfolio.commands.CreateProjectNodeCommand;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventNewProjectCreated;

@EventHandler
public class NewProjectCreatedEventHandler {

	@Autowired
	private Gate gate;

	/**
	 * Triggers when a new project is created and creates a new node in the
	 * portfolio
	 *
	 * @param _projectCreated
	 *            the event
	 *
	 */
	@Subscribe
	public void suscribe(final EventNewProjectCreated _projectCreated) {
		final Project createdProject = _projectCreated.getCreatedProject();
		final Integer parentNodeID = _projectCreated.getParentNodeID();

		this.gate.dispatch(new CreateProjectNodeCommand(createdProject.getId(), parentNodeID));

	}
}
