package com.tocea.corolla.portfolio.commands.handlers;

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.portfolio.commands.MovePortfolioNodeCommand
import com.tocea.corolla.portfolio.dao.IPortfolioDAO;
import com.tocea.corolla.portfolio.domain.Portfolio
import com.tocea.corolla.portfolio.domain.ProjectNode
import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.portfolio.exceptions.PortfolioNotFoundException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc;
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.trees.domain.TreeNode
import com.tocea.corolla.trees.services.ITreeManagementService;
import com.tocea.corolla.trees.services.TreeManagementService;

@FunctionalTestDoc(requirementName = "MOVE_PORTFOLIO_TREE_NODE")
public class MovePortfolioNodeCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IPortfolioDAO portfolioDAO = Mock(IPortfolioDAO)	
	def MovePortfolioNodeCommandHandler handler
	def Gate gate = Mock(Gate)
	def ITreeManagementService treeManagementService = Mock(TreeManagementService)
	
	def setup() {
		handler = new MovePortfolioNodeCommandHandler(
				portfolioDAO : portfolioDAO,
				treeManagementService : treeManagementService
		)
	}
	
	
	def "it should invoke the command to move a node in the tree"() {
		
		given:
			def nodeId = 2
			def newParentId = 1
			def portfolio = new Portfolio(
					nodes: [
					        new ProjectNode(
					        		id: 1, 
					        		projectId: "1", 
					        		nodes: [new ProjectNode(
							        		id: 2, 
							        		projectId: "2", 
							        		nodes: []
							        )]
					        )
					]
			)
		
		when:
			handler.handle new MovePortfolioNodeCommand(nodeId, newParentId)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			notThrown(Exception.class)
		
		then:
			1 * treeManagementService.moveNode(portfolio, nodeId, newParentId)
			
		then:
			1 * portfolioDAO.save(_)
			
	}
	
	def "it should throw an exception if the portfolio cannot be found"() {
		
		given:
			def nodeId = 2
			def newParentId = 1
			def portfolio = null
		
		when:
			handler.handle new MovePortfolioNodeCommand(nodeId, newParentId)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			thrown(PortfolioNotFoundException.class)
		
	}
	
}