package com.tocea.corolla.products.commands.handlers;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.FinishReleaseCommand;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventReleaseFinished;
import com.tocea.corolla.products.exceptions.ProjectNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dcollard
 *
 */
@CommandHandler
@Transactional
public class FinishReleaseCommandHandler implements ICommandHandler<FinishReleaseCommand, Project> {

    private static final Logger LOG = LoggerFactory.getLogger(FinishReleaseCommandHandler.class);

    @Autowired
    private Gate gate;

    @Override
    public Project handle(@Valid final FinishReleaseCommand command) {

        final Project project = command.getProject();
        final String releaseVersion = command.getReleaseVersion();

        if (project == null) {
            throw new ProjectNotFoundException();
        }

        this.gate.dispatchEvent(new EventReleaseFinished(project, releaseVersion));

        return project;

    }

}
