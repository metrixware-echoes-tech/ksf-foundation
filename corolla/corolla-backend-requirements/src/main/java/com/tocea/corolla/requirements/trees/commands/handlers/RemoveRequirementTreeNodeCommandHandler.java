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
package com.tocea.corolla.requirements.trees.commands.handlers;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.exceptions.MissingProjectBranchInformationException;
import com.tocea.corolla.requirements.commands.DeleteRequirementCommand;
import com.tocea.corolla.requirements.trees.commands.RemoveRequirementTreeNodeCommand;
import com.tocea.corolla.requirements.trees.dao.IRequirementsTreeDAO;
import com.tocea.corolla.requirements.trees.domain.RequirementNode;
import com.tocea.corolla.requirements.trees.domain.RequirementsTree;
import com.tocea.corolla.requirements.trees.exceptions.RequirementTreeNodeNotFoundException;
import com.tocea.corolla.requirements.trees.exceptions.RequirementsTreeNotFoundException;
import com.tocea.corolla.requirements.trees.utils.RequirementsTreeUtils;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.exceptions.MissingTreeNodeInformationException;
import com.tocea.corolla.trees.predicates.FindNodeByIDPredicate;
import com.tocea.corolla.trees.services.ITreeManagementService;

@CommandHandler
@Transactional
public class RemoveRequirementTreeNodeCommandHandler implements ICommandHandler<RemoveRequirementTreeNodeCommand, RequirementsTree> {
	
	@Autowired
	private IRequirementsTreeDAO requirementsTreeDAO;

	@Autowired
	private Gate gate;

	@Autowired
	private ITreeManagementService treeManagementService;

	@Override
	public RequirementsTree handle(@Valid final RemoveRequirementTreeNodeCommand command) {

		final ProjectBranch branch = command.getBranch();

		if (branch == null) {
			throw new MissingProjectBranchInformationException("No project branch found");
		}

		final Integer nodeID = command.getNodeID();

		if (nodeID == null) {
			throw new MissingTreeNodeInformationException("No node ID found");
		}

		final RequirementsTree tree = requirementsTreeDAO.findByBranchId(branch.getId());

		if (tree == null) {
			throw new RequirementsTreeNotFoundException();
		}

		final TreeNode node = treeManagementService.findNode(tree, new FindNodeByIDPredicate(nodeID));

		if (node == null) {
			throw new RequirementTreeNodeNotFoundException();
		}

		cleanRequirements(node);

		treeManagementService.removeNode(tree, node.getId());

		requirementsTreeDAO.save(tree);

		return tree;
	}

	private void cleanRequirements(final TreeNode node)  {

		if (RequirementsTreeUtils.isRequirementNode(node)) {
			final String requirementID = ((RequirementNode) node).getRequirementId();
			gate.dispatchAsync(new DeleteRequirementCommand(requirementID));
		}

		final Collection<String> ids = RequirementsTreeUtils.getRequirementsIDs(node.getNodes());

		for(final String ID : ids) {
			gate.dispatchAsync(new DeleteRequirementCommand(ID));
		}

	}

}