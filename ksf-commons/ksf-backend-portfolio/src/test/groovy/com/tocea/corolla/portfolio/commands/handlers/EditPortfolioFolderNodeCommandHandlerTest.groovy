package com.tocea.corolla.portfolio.commands.handlers;

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.portfolio.commands.EditPortfolioFolderNodeCommand
import com.tocea.corolla.portfolio.dao.IPortfolioDAO;
import com.tocea.corolla.portfolio.domain.Portfolio
import com.tocea.corolla.portfolio.domain.ProjectNode
import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.portfolio.exceptions.PortfolioNotFoundException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc;
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.trees.domain.FolderNode
import com.tocea.corolla.trees.domain.ITree;
import com.tocea.corolla.trees.domain.TreeNode
import com.tocea.corolla.trees.exceptions.InvalidTreeNodeInformationException;
import com.tocea.corolla.trees.exceptions.MissingTreeNodeInformationException;
import com.tocea.corolla.trees.predicates.FindNodeByIDPredicate
import com.tocea.corolla.trees.services.ITreeManagementService
import com.tocea.corolla.trees.services.TreeManagementService

@FunctionalTestDoc(requirementName = "EDIT_PORTFOLIO_TEXT_NODE")
public class EditPortfolioFolderNodeCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IPortfolioDAO portfolioDAO = Mock(IPortfolioDAO)	
	def EditPortfolioFolderNodeCommandHandler handler
	def ITreeManagementService treeManagementService = Mock(TreeManagementService)
	
	def setup() {
		handler = new EditPortfolioFolderNodeCommandHandler(
				portfolioDAO : portfolioDAO,
				treeManagementService : treeManagementService
		)
	}
	
	
	def "it should edit a text node of the tree"() {
		
		given:
			def nodeId = 2
			def text = "my not so awesome text"
			def portfolio = new Portfolio(
					nodes: [
					        new FolderNode(
					        		id: 1, 
					        		text: "my awesome text", 
					        		nodes: [new ProjectNode(
							        		id: 2, 
							        		projectId: "2", 
							        		nodes: []
							        )]
					        )
					]
			)
		
		when:
			handler.handle new EditPortfolioFolderNodeCommand(nodeId, text)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			notThrown(Exception.class)
		
		then:
			treeManagementService.findNode(portfolio, { it instanceof FindNodeByIDPredicate }) >> portfolio.nodes[0]
					
		then:
			portfolio.nodes[0].text == text	
			
		then:
			1 * portfolioDAO.save(_)
			
	}
	
	def "it should throw an exception if the portfolio cannot be found"() {
		
		given:
			def nodeId = 2
			def text = "my not so awesome text"
			def portfolio = null
		
		when:
			handler.handle new EditPortfolioFolderNodeCommand(nodeId, text)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			thrown(PortfolioNotFoundException.class)
		
	}
	
	def "it should throw an exception if the node ID is not defined"() {
		
		given:
			def nodeId = null
			def text = "my not so awesome text"
			def portfolio = new Portfolio(nodes: [])
		
		when:
			handler.handle new EditPortfolioFolderNodeCommand(nodeId, text)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			thrown(MissingTreeNodeInformationException.class)
		
	}
	
	def "it should throw an exception if the node ID does not match any not in the tree"() {
		
		given:
			def nodeId = 2
			def text = "my not so awesome text"
			def portfolio = new Portfolio(nodes: [])
		
		when:
			handler.handle new EditPortfolioFolderNodeCommand(nodeId, text)
	
		then:
			portfolioDAO.find() >> portfolio
			treeManagementService.findNode(portfolio, { it instanceof FindNodeByIDPredicate }) >> null
			
		then:
			thrown(InvalidTreeNodeInformationException.class)
		
	}
	
	def "it should throw an exception if the node is not a FolderNode"() {
		
		given:
			def nodeId = 2
			def text = "my not so awesome text"
			def portfolio = new Portfolio(
					nodes: [
					        new FolderNode(
					        		id: 1, 
					        		text: "my awesome text", 
					        		nodes: [new ProjectNode(
							        		id: 2, 
							        		projectId: "2", 
							        		nodes: []
							        )]
					        )
					]
			)
		
		when:
			handler.handle new EditPortfolioFolderNodeCommand(nodeId, text)
	
		then:
			portfolioDAO.find() >> portfolio
			treeManagementService.findNode(portfolio, { it instanceof FindNodeByIDPredicate }) >> portfolio.nodes[0].nodes[0]
			
		then:
			thrown(InvalidTreeNodeInformationException.class)
		
	}
	
}