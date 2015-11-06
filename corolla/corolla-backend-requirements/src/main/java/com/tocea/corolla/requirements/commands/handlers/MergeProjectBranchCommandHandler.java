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
package com.tocea.corolla.requirements.commands.handlers;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.products.commands.MergeProjectBranchCommand;
import com.tocea.corolla.products.domain.ProjectBranch;
import com.tocea.corolla.products.exceptions.MissingProjectBranchInformationException;
import com.tocea.corolla.products.exceptions.ProjectBranchMergeException;
import com.tocea.corolla.requirements.commands.CreateRequirementCommand;
import com.tocea.corolla.requirements.commands.EditRequirementCommand;
import com.tocea.corolla.requirements.dao.IRequirementDAO;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.requirements.trees.dao.IRequirementsTreeDAO;
import com.tocea.corolla.requirements.trees.domain.RequirementsTree;
import com.tocea.corolla.requirements.trees.exceptions.RequirementsTreeNotFoundException;
import com.tocea.corolla.requirements.trees.utils.RequirementsTreeUtils;
import com.tocea.corolla.requirements.utils.RequirementUtils;
import com.tocea.corolla.trees.domain.TreeNode;

@CommandHandler
@Transactional
public class MergeProjectBranchCommandHandler implements ICommandHandler<MergeProjectBranchCommand, ProjectBranch> {

	@Autowired
	private IRequirementDAO requirementDAO;
	
	@Autowired
	private IRequirementsTreeDAO requirementsTreeDAO;
	
	@Autowired
	private Gate gate;
	
	@Override
	public ProjectBranch handle(@Valid MergeProjectBranchCommand command) {
		
		ProjectBranch origin = command.getOrigin();
		
		if (origin == null) {
			throw new MissingProjectBranchInformationException("No origin branch defined");
		}
		
		ProjectBranch destination = command.getDestination();
		
		if (destination == null) {
			throw new MissingProjectBranchInformationException("No destination branch defined");
		}
		
		mergeRequirements(origin, destination);

		return destination;
	}
	
	private void mergeRequirements(ProjectBranch origin, ProjectBranch destination) {
		
		Collection<Requirement> originRequirements = requirementDAO.findByProjectBranchId(origin.getId());
		Collection<Requirement> destinationRequirements = requirementDAO.findByProjectBranchId(destination.getId());
		
		RequirementsTree originTree = getRequirementsTree(origin);
		RequirementsTree destinationTree = getRequirementsTree(destination);
		
		for(Requirement req : originRequirements) {
			
			Requirement destinationReq = RequirementUtils.findByKey(destinationRequirements, req.getKey());
			
			if (destinationReq == null) {
				
				// the requirement exists in the origin branch but not in the destination branch
				// So, we have to clone it and create it in the destination branch
				Requirement clone = null;
				try {
					clone = RequirementUtils.clone(req, destination.getId());
				} catch (Exception e) {
					throw new ProjectBranchMergeException(e);
				}				
				this.gate.dispatch(new CreateRequirementCommand(clone));			
				
			}else{
				
				// the requirement exists in both branches
				// So, we import the modifications done to the requirement in the origin branch
				String id = destinationReq.getId();
				BeanUtils.copyProperties(req, destinationReq);
				destinationReq.setId(id);
				destinationReq.setProjectBranchId(destination.getId());
				
				this.gate.dispatch(new EditRequirementCommand(destinationReq));				
				
				//TODO more advanced merge strategy
							
			}

		}
		
		// Clone the requirements tree
		Collection<TreeNode> nodes = Lists.newArrayList();
		for(TreeNode node : originTree.getNodes()) {			
			nodes.add(node.clone());			
		}
		
		// Make sure that if a requirement has not been imported into the destination branch
		// it will not be shown in the requirements tree
		destinationRequirements = requirementDAO.findByProjectBranchId(destination.getId());		
		nodes = RequirementsTreeUtils.removeRequirementsNotIn(nodes, destinationRequirements);
		
		// Save the requirements tree
		destinationTree.setNodes(nodes);
		requirementsTreeDAO.save(destinationTree);
		
		//TODO more advanced way of merging requirements trees
		
	}
	
	private RequirementsTree getRequirementsTree(ProjectBranch branch) {
		
		RequirementsTree tree = requirementsTreeDAO.findByBranchId(branch.getId());
		
		if (tree == null) {		
			throw new RequirementsTreeNotFoundException(branch);			
		}
		
		return tree;
	}
	
}