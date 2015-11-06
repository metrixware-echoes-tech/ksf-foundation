package com.tocea.corolla.portfolio.commands.handlers

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.portfolio.commands.CreatePortfolioCommand
import com.tocea.corolla.portfolio.commands.CreateProjectNodeCommand
import com.tocea.corolla.portfolio.dao.IPortfolioDAO
import com.tocea.corolla.portfolio.domain.Portfolio
import com.tocea.corolla.portfolio.domain.ProjectNode
import com.tocea.corolla.portfolio.exceptions.ProjectNodeAlreadyExistException;
import com.tocea.corolla.products.exceptions.MissingProjectInformationException;
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "CREATE_PORTFOLIO")
class CreatePortfolioCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IPortfolioDAO portfolioDAO = Mock(IPortfolioDAO)	
	def CreatePortfolioCommandHandler handler
	def Gate gate = Mock(Gate)
	
	def setup() {
		handler = new CreatePortfolioCommandHandler(
				portfolioDAO : portfolioDAO
		)
	}
	
	def "it should create a portfolio"() {
				
		when:
			handler.handle new CreatePortfolioCommand()
	
		then:
			notThrown(Exception.class)
				
		then:
			1 * portfolioDAO.save { it instanceof Portfolio && it.nodes == [] }
		
	}
	
}