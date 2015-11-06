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
package com.tocea.corolla.portfolio.commands.handlers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.portfolio.commands.CreatePortfolioCommand;
import com.tocea.corolla.portfolio.commands.CreatePortfolioFolderNodeCommand;
import com.tocea.corolla.portfolio.dao.IPortfolioDAO;
import com.tocea.corolla.portfolio.domain.Portfolio;
import com.tocea.corolla.trees.domain.FolderNode;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.exceptions.InvalidTreeNodeInformationException;
import com.tocea.corolla.trees.services.ITreeManagementService;

@CommandHandler
@Transactional
public class CreatePortfolioFolderNodeCommandHandler implements ICommandHandler<CreatePortfolioFolderNodeCommand, FolderNode> {

	@Autowired
	private IPortfolioDAO portfolioDAO;
	
	@Autowired
	private Gate gate;
	
	@Autowired
	private ITreeManagementService treeManagementService;
	
	@Override
	public FolderNode handle(@Valid CreatePortfolioFolderNodeCommand command) {
		
		Portfolio portfolio = portfolioDAO.find();
			
		if (portfolio == null) {
			gate.dispatch(new CreatePortfolioCommand());
		}
		
		String text = command.getText();
		
		if (StringUtils.isEmpty(text)) {
			throw new InvalidTreeNodeInformationException("The text of a text node cannot be empty");
		}
		
		Integer parentID = command.getParentID();
		
		String typeID = command.getTypeID();
		
		TreeNode node = new FolderNode(text, typeID);
		
		treeManagementService.insertNode(portfolio, parentID, node);
		
		portfolioDAO.save(portfolio);
		
		return (FolderNode) node;
	}

}
