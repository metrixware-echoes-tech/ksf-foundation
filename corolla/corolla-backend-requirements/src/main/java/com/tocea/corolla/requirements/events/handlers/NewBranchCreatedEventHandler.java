package com.tocea.corolla.requirements.events.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.events.EventNewBranchCreated;
import com.tocea.corolla.products.exceptions.ProjectBranchCloningException;
import com.tocea.corolla.requirements.commands.CreateRequirementCommand;
import com.tocea.corolla.requirements.dao.IRequirementDAO;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.requirements.trees.dao.IRequirementsTreeDAO;
import com.tocea.corolla.requirements.trees.domain.RequirementNode;
import com.tocea.corolla.requirements.trees.domain.RequirementsTree;
import com.tocea.corolla.requirements.trees.exceptions.RequirementsTreeNotFoundException;
import com.tocea.corolla.requirements.trees.utils.RequirementsTreeUtils;
import com.tocea.corolla.requirements.utils.RequirementUtils;
import com.tocea.corolla.trees.domain.TreeNode;

@EventHandler
public class NewBranchCreatedEventHandler {
	@Autowired
	private IRequirementsTreeDAO requirementsTreeDAO;

	@Autowired
	private IRequirementDAO requirementDAO;

	@Autowired
	private Gate gate;

	@Subscribe
	public void newBranchCreatedHandler(final EventNewBranchCreated _event) {
		final ProjectBranch branch = _event.getCreatedBranch();
		// Create requirements tree
		final RequirementsTree requirementsTree = new RequirementsTree();
		requirementsTree.setBranchId(branch.getId());
		requirementsTree.setNodes(new ArrayList<TreeNode>());

		requirementsTreeDAO.save(requirementsTree);

		final ProjectBranch originBranch = _event.getOriginBranch();
		
		if (originBranch != null) {
			cloneProjectBranch(originBranch, branch, requirementsTree);
		}
	}
	
	private void cloneProjectBranch(final ProjectBranch originBranch, final ProjectBranch newBranch,
			final RequirementsTree newRequirementsTree) {
		
		final HashMap<String, String> mapIDs = Maps.newHashMap();

		final Collection<Requirement> originRequirements = requirementDAO.findByProjectBranchId(originBranch.getId());

		if (originRequirements != null) {

			for (final Requirement requirement : originRequirements) {

				Requirement clone = null;

				try {
					clone = RequirementUtils.clone(requirement, newBranch.getId());
				} catch (final Exception e) {
					throw new ProjectBranchCloningException(e);
				}

				gate.dispatch(new CreateRequirementCommand(clone));

				mapIDs.put(requirement.getId(), clone.getId());

			}

		}

		final RequirementsTree originTree = requirementsTreeDAO.findByBranchId(originBranch.getId());

		if (originTree == null) {
			throw new RequirementsTreeNotFoundException(originBranch);
		}

		for (final TreeNode node : originTree.getNodes()) {

			newRequirementsTree.getNodes().add(node.clone());

		}

		for (final RequirementNode node : RequirementsTreeUtils.getRequirementsNodes(newRequirementsTree.getNodes())) {

			node.setRequirementId(mapIDs.get(node.getRequirementId()));

		}

		requirementsTreeDAO.save(newRequirementsTree);

	}
	
}
