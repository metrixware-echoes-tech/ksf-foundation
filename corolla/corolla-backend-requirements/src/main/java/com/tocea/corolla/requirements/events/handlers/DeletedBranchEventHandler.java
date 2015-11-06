package com.tocea.corolla.requirements.events.handlers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.tocea.corolla.cqrs.annotations.EventHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.events.EventBranchDeleted;
import com.tocea.corolla.requirements.commands.DeleteRequirementCommand;
import com.tocea.corolla.requirements.dao.IRequirementDAO;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.requirements.trees.dao.IRequirementsTreeDAO;
import com.tocea.corolla.requirements.trees.domain.RequirementsTree;

@EventHandler
public class DeletedBranchEventHandler {
	@Autowired
	private IRequirementsTreeDAO requirementsTreeDAO;

	@Autowired
	private IRequirementDAO requirementDAO;

	@Autowired
	private Gate gate;

	@Subscribe
	public void deleteBranchHandler(final EventBranchDeleted _event) {
		final ProjectBranch branch = _event.getBranch();
		// Delete the requirements tree of the branch
		final RequirementsTree requirementsTree = requirementsTreeDAO.findByBranchId(branch.getId());
		if (requirementsTree != null) {
			requirementsTreeDAO.delete(requirementsTree);
		}
		
		final Collection<Requirement> requirements = requirementDAO.findByProjectBranchId(branch.getId());
		
		// Delete all the requirements attached to this branch
		for (final Requirement requirement : requirements) {
			gate.dispatch(new DeleteRequirementCommand(requirement.getId()));
		}
		
	}
	
}
