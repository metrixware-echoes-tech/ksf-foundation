package com.tocea.corolla.portfolio.commands.handlers

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.portfolio.commands.CreatePortfolioCommand
import com.tocea.corolla.portfolio.commands.CreatePortfolioFolderNodeCommand
import com.tocea.corolla.portfolio.commands.CreateProjectNodeCommand
import com.tocea.corolla.portfolio.dao.IPortfolioDAO
import com.tocea.corolla.portfolio.domain.Portfolio
import com.tocea.corolla.portfolio.domain.ProjectNode
import com.tocea.corolla.portfolio.exceptions.ProjectNodeAlreadyExistException;
import com.tocea.corolla.products.exceptions.MissingProjectInformationException;
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.exceptions.InvalidTreeNodeInformationException;
import com.tocea.corolla.trees.services.ITreeManagementService
import com.tocea.corolla.trees.services.TreeManagementService
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "ADD_PROJECT_NODE")
class CreatePortfolioFolderNodeCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IPortfolioDAO portfolioDAO = Mock(IPortfolioDAO)	
	def CreatePortfolioFolderNodeCommandHandler handler
	def Gate gate = Mock(Gate)
	def ITreeManagementService treeManagementService = Mock(TreeManagementService)
	
	def setup() {
		handler = new CreatePortfolioFolderNodeCommandHandler(
				portfolioDAO : portfolioDAO,
				treeManagementService : treeManagementService,
				gate : gate
		)
	}
	
	def "it should invoke the command to create a new tree node in the portfolio"() {
		
		given:
			def text = "TEXT"
			def parentID = 2
			def portfolio = new Portfolio(nodes: [])
			def typeID = "1"
		
		when:
			handler.handle new CreatePortfolioFolderNodeCommand(text, typeID, parentID)
	
		then:
			notThrown(Exception.class)
			
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			1 * treeManagementService.insertNode(portfolio, parentID, { it.text == text && it.typeID == typeID })
				
		then:
			1 * portfolioDAO.save(_)
		
	}
	
	def "it should create a portfolio if it is not defined yet"() {
		
		given:
			def text = "TEXT"
			def parentID = 2
			def portfolio = null
			def typeID = "1"
		
		when:
			handler.handle new CreatePortfolioFolderNodeCommand(text, typeID, parentID)
	
		then:
			notThrown(Exception.class)
			
		then:
			portfolioDAO.find() >> portfolio
			
		then:
			1 * gate.dispatch { it instanceof CreatePortfolioCommand } >> new Portfolio(nodes: [])
		
	}
	
	def "it should throw an exception if the text is empty"() {
		
		given:
			def text = ""
			def parentID = 2
			def typeID = "1"
		
		when:
			handler.handle new CreatePortfolioFolderNodeCommand(text, typeID, parentID)
	
		then:
			thrown(InvalidTreeNodeInformationException.class)
			
	}
	
}