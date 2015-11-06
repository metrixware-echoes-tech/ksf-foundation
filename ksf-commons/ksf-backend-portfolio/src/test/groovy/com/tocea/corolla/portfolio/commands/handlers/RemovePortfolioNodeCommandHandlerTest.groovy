package com.tocea.corolla.portfolio.commands.handlers;

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.portfolio.commands.RemovePortfolioNodeCommand
import com.tocea.corolla.portfolio.dao.IPortfolioDAO;
import com.tocea.corolla.portfolio.domain.Portfolio
import com.tocea.corolla.portfolio.domain.ProjectNode
import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.portfolio.exceptions.PortfolioNotFoundException;
import com.tocea.corolla.products.commands.DeleteProjectCommand;
import com.tocea.corolla.utils.functests.FunctionalTestDoc;
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.trees.domain.TreeNode
import com.tocea.corolla.trees.predicates.FindNodeByIDPredicate;
import com.tocea.corolla.trees.services.ITreeManagementService;
import com.tocea.corolla.trees.services.TreeManagementService;


@FunctionalTestDoc(requirementName = "REMOVE_PORTFOLIO_TREE_NODE")
public class RemovePortfolioNodeCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IPortfolioDAO portfolioDAO = Mock(IPortfolioDAO)	
	def RemovePortfolioNodeCommandHandler handler
	def Gate gate = Mock(Gate)
	def ITreeManagementService treeManagementService = Mock(TreeManagementService)
	
	def setup() {
		handler = new RemovePortfolioNodeCommandHandler(
				portfolioDAO : portfolioDAO,
				gate : gate,
				treeManagementService : treeManagementService
		)
	}
	
	
	def "it should invoke the service to remove a node in the tree"() {
		
		given:
			def nodeId = 2
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
			handler.handle new RemovePortfolioNodeCommand(nodeId)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			notThrown(Exception.class)
		
		then:
			1 * treeManagementService.removeNode(portfolio, nodeId)	
			
		then:
			1 * portfolioDAO.save(_)
			
	}
	
	def "it should throw an exception if the portfolio cannot be found"() {
		
		given:
			def nodeId = 2
			def newParentId = 1
			def portfolio = null
		
		when:
			handler.handle new RemovePortfolioNodeCommand(nodeId)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			thrown(PortfolioNotFoundException.class)
		
	}
	
	def "it should invoke the command to delete a project on every deleted project nodes"() {
		
		given:
			def nodeId = 1
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
			handler.handle new RemovePortfolioNodeCommand(nodeId)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			notThrown(Exception.class)
			
		then:
			treeManagementService.findNode(portfolio, { it instanceof FindNodeByIDPredicate }) >> portfolio.nodes[0]
		
		then:
			1 * gate.dispatch { it instanceof DeleteProjectCommand && it.projectID == portfolio.nodes[0].projectId }	
			1 * gate.dispatch { it instanceof DeleteProjectCommand && it.projectID == portfolio.nodes[0].nodes[0].projectId }	
		
	}
	
}