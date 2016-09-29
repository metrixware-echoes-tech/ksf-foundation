package com.tocea.corolla.products.commands.handlers;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.CancelFeatureCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.events.EventFeatureCanceled;
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
public class CancelFeatureCommandHandler implements ICommandHandler<CancelFeatureCommand, Project> {

    private static final Logger LOG = LoggerFactory.getLogger(CancelFeatureCommandHandler.class);

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private IProjectBranchDAO branchDAO;

    @Autowired
    private Gate gate;

    /**
     * Treats the command "When a project feature is canceled"
     *
     * @param command
     * @return
     */
    @Override
    public Project handle(@Valid final CancelFeatureCommand command) {

        final Project project = command.getProject();
        final String featureId = command.getFeatureId();
        final String featureSubject = command.getFeatureSubject();

        if (project == null) {
            throw new ProjectNotFoundException();
        }

        this.gate.dispatchEvent(new EventFeatureCanceled(project, featureId, featureSubject));

        return project;

    }

}
