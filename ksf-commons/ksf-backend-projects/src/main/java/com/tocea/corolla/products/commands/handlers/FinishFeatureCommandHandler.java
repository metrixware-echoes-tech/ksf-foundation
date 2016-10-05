package com.tocea.corolla.products.commands.handlers;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.FinishFeatureCommand;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventFeatureFinished;
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
public class FinishFeatureCommandHandler implements ICommandHandler<FinishFeatureCommand, Project> {

    private static final Logger LOG = LoggerFactory.getLogger(FinishFeatureCommandHandler.class);

    @Autowired
    private Gate gate;

    @Override
    public Project handle(@Valid final FinishFeatureCommand command) {

        final Project project = command.getProject();
        final String featureId = command.getFeatureId();
        final String featureSubject = command.getFeatureSubject();

        if (project == null) {
            throw new ProjectNotFoundException();
        }

        this.gate.dispatchEvent(new EventFeatureFinished(project, featureId, featureSubject));

        return project;

    }

}
