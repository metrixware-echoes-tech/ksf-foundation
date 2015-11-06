package com.tocea.corolla.portfolio.commands.handlers;

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.portfolio.commands.ChangePortfolioFolderNodeTypeCommand
import com.tocea.corolla.portfolio.dao.IPortfolioDAO;
import com.tocea.corolla.portfolio.domain.Portfolio
import com.tocea.corolla.portfolio.domain.ProjectNode
import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.portfolio.exceptions.PortfolioNotFoundException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc;
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO
import com.tocea.corolla.trees.domain.FolderNode
import com.tocea.corolla.trees.domain.FolderNodeType
import com.tocea.corolla.trees.domain.ITree;
import com.tocea.corolla.trees.domain.TreeNode
import com.tocea.corolla.trees.exceptions.FolderNodeTypeNotFoundException;
import com.tocea.corolla.trees.exceptions.InvalidFolderNodeTypeInformationException;
import com.tocea.corolla.trees.exceptions.InvalidTreeNodeInformationException;
import com.tocea.corolla.trees.exceptions.MissingFolderNodeTypeInformationException;
import com.tocea.corolla.trees.exceptions.MissingTreeNodeInformationException;
import com.tocea.corolla.trees.predicates.FindNodeByIDPredicate
import com.tocea.corolla.trees.services.ITreeManagementService
import com.tocea.corolla.trees.services.TreeManagementService

@FunctionalTestDoc(requirementName = "PORTFOLIO_CHANGE_FOLDER_NODE_TYPE")
public class ChangePortfolioFolderNodeTypeCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IPortfolioDAO portfolioDAO = Mock(IPortfolioDAO)	
	def ChangePortfolioFolderNodeTypeCommandHandler handler
	def ITreeManagementService treeManagementService = Mock(TreeManagementService)
	def IFolderNodeTypeDAO folderNodeTypeDAO = Mock(IFolderNodeTypeDAO)
	
	def setup() {
		handler = new ChangePortfolioFolderNodeTypeCommandHandler(
				portfolioDAO : portfolioDAO,
				folderNodeTypeDAO : folderNodeTypeDAO,
				treeManagementService : treeManagementService
		)
	}
	
	def "it should change the type of a folder node in the tree"() {
		
		given:
			def nodeID = 2
			def newTypeID = "T2"
			def portfolio = new Portfolio(
					nodes: [
					        new FolderNode(
					        		id: 1, 
					        		text: "my awesome text", 
					        		typeID: "T1",
					        		nodes: []
					        )
					]
			)
		
		when:
			handler.handle new ChangePortfolioFolderNodeTypeCommand(nodeID, newTypeID)
	
		then:
			portfolioDAO.find() >> portfolio
			treeManagementService.findNode(portfolio, { it instanceof FindNodeByIDPredicate }) >> portfolio.nodes[0]
			folderNodeTypeDAO.findOne(newTypeID) >> new FolderNodeType(id: newTypeID)
			
		then:
			notThrown(Exception.class)

		then:
			portfolio.nodes[0].typeID == newTypeID
			
		then:
			1 * portfolioDAO.save(_)
			
	}
	
	def "it should throw an exception if the portfolio cannot be found"() {
		
		given:
			def nodeID = 2
			def newTypeID = "T2"
			def portfolio = null
		
		when:
			handler.handle new ChangePortfolioFolderNodeTypeCommand(nodeID, newTypeID)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			thrown(PortfolioNotFoundException.class)
		
	}
	
	def "it should throw an exception if the node ID is not defined"() {
		
		given:
			def nodeID = null
			def newTypeID = "T2"
			def portfolio = new Portfolio(nodes: [])
		
		when:
			handler.handle new ChangePortfolioFolderNodeTypeCommand(nodeID, newTypeID)
	
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			thrown(MissingTreeNodeInformationException.class)
		
	}
	
	def "it should throw an exception if the node ID does not match any not in the tree"() {
		
		given:
			def nodeID = 1
			def newTypeID = "T2"
			def portfolio = new Portfolio(nodes: [])
		
		when:
			handler.handle new ChangePortfolioFolderNodeTypeCommand(nodeID, newTypeID)
	
		then:
			portfolioDAO.find() >> portfolio
			treeManagementService.findNode(portfolio, { it instanceof FindNodeByIDPredicate }) >> null
			
		then:
			thrown(InvalidTreeNodeInformationException.class)
		
	}
	
	def "it should throw an exception if the node is not a FolderNode"() {
		
		given:
			def nodeID = 1
			def newTypeID = "T2"
			def portfolio = new Portfolio(nodes: [])
		
		when:
			handler.handle new ChangePortfolioFolderNodeTypeCommand(nodeID, newTypeID)
	
		then:
			portfolioDAO.find() >> portfolio
			treeManagementService.findNode(portfolio, { it instanceof FindNodeByIDPredicate }) >> new ProjectNode(id: nodeID)
			
		then:
			thrown(InvalidTreeNodeInformationException.class)
	
	}
	
	def "it should throw an exception if the given type ID is invalid"() {
		
		given:
			def nodeID = 1
			def newTypeID = "T2"
			def portfolio = new Portfolio(nodes: [new FolderNode(id: 1, nodes:[])])
		
		when:
			handler.handle new ChangePortfolioFolderNodeTypeCommand(nodeID, newTypeID)
	
		then:
			portfolioDAO.find() >> portfolio
			treeManagementService.findNode(portfolio, { it instanceof FindNodeByIDPredicate }) >> portfolio.nodes[0]
			folderNodeTypeDAO.findOne(newTypeID) >> null
			
		then:
			thrown(FolderNodeTypeNotFoundException.class)
		
	}
	
}