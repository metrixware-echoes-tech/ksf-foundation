/*
 * Corolla - A Tool to manage software requirements and test cases
 * Copyright (C) 2015 Tocea
 *
 * This file is part of Corolla.
 *
 * Corolla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License,
 * or any later version.
 *
 * Corolla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Corolla.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.products.commands.handlers;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.DeleteProjectBranchCommand;
import com.tocea.corolla.products.commands.DeleteProjectCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.events.EventProjectDeleted;
import com.tocea.corolla.products.exceptions.MissingProjectInformationException;
import com.tocea.corolla.products.exceptions.ProjectNotFoundException;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles the command when a project has to be deleted.
 * @author sleroy
 */
@CommandHandler
@Transactional
public class DeleteProjectCommandHandler implements ICommandHandler<DeleteProjectCommand, Project> {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteProjectCommandHandler.class);

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
    public Project handle(@Valid final DeleteProjectCommand command) {

        final String id = command.getProjectID();

        if (isEmpty(id)) {
            throw new MissingProjectInformationException("No ID found");
        }

        final Project project = projectDAO.findOne(id);

        if (project == null) {
            throw new ProjectNotFoundException();
        }

        // Delete the project from the database
        projectDAO.delete(project);

        // Invoke the commands to delete the project branches associated to this project
        final Collection<ProjectBranch> branches = branchDAO.findByProjectId(project.getId());
        for (final ProjectBranch branch : branches) {
            gate.dispatch(new DeleteProjectBranchCommand(branch));
        }
        gate.dispatchEvent(new EventProjectDeleted(project));

        return project;

    }

}
