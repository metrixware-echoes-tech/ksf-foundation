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

import org.springframework.beans.factory.annotation.Autowired;

import com.tocea.corolla.cqrs.annotations.CommandHandler;
import com.tocea.corolla.cqrs.handler.ICommandHandler;
import com.tocea.corolla.portfolio.commands.MovePortfolioNodeCommand;
import com.tocea.corolla.portfolio.dao.IPortfolioDAO;
import com.tocea.corolla.portfolio.domain.Portfolio;
import com.tocea.corolla.portfolio.exceptions.PortfolioNotFoundException;
import com.tocea.corolla.trees.services.ITreeManagementService;

@CommandHandler
@Transactional
public class MovePortfolioNodeCommandHandler implements ICommandHandler<MovePortfolioNodeCommand, Portfolio> {

	@Autowired
	private IPortfolioDAO portfolioDAO;
	
	@Autowired
	private ITreeManagementService treeManagementService;
	
	@Override
	public Portfolio handle(@Valid MovePortfolioNodeCommand command) {
		
		Portfolio portfolio = portfolioDAO.find();
		
		if (portfolio == null) {
			throw new PortfolioNotFoundException();
		}
		
		Integer nodeId = command.getNodeID();
		
		Integer newParentId = command.getNewParentID();
		
		treeManagementService.moveNode(portfolio, nodeId, newParentId);
		
		portfolioDAO.save(portfolio);
		
		return portfolio;
		
	}

}
