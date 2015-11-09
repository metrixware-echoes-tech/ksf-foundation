package fr.echoes.lab.ksf.cc.sf.commands.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

import fr.echoes.lab.ksf.cc.sf.commands.CreateProductionLineCommand
import fr.echoes.lab.ksf.cc.sf.dao.IProductionLineDAO
import fr.echoes.lab.ksf.cc.sf.exceptions.*;
import fr.echoes.lab.ksf.cc.sf.model.ProductionLine

@FunctionalTestDoc(requirementName = "ADD_PRODUCTION_LINE")
class CreateProductionLineCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	
	def Gate gate = Mock(Gate)
	def IProductionLineDAO productionLineDAO = Mock(IProductionLineDAO)
	
	def CreateProductionLineCommandHandler handler
	
	def setup() {
		handler = new CreateProductionLineCommandHandler(
				productionLineDAO : productionLineDAO,
				gate : gate
		)
	}
	
	def "it should create a new production line"() {
		
		given:
			def project = new Project()
			def productionLine = new ProductionLine()
			productionLine.project = project
			
		when:
			handler.handle new CreateProductionLineCommand(productionLine)
		
		then:
			notThrown(Exception.class)
		
		then:
			productionLineDAO.findByProject(project) >> null
			1 * productionLineDAO.save(_)
		
	}
	
	def "it should throw an exception if the production line is null"() {
		
		given:
			def productionLine = null
		
		when:
			handler.handle new CreateProductionLineCommand(productionLine)
			
		then:
			0 * productionLineDAO.save(_)
			thrown(MissingProductionLineInformationException.class)
		
	}
	
	def "it should throw an exception if the ID is already defined"() {
		
		given:
			def productionLine = new ProductionLine()
			productionLine.id = "1"
		
		when:
			handler.handle new CreateProductionLineCommand(productionLine)
			
		then:
			0 * productionLineDAO.save(_)
			thrown(InvalidProductionLineInformationException.class)
			
	}
	
	def "it should throw an exception if the production line already exist"() {
		
		given:
			def project = new Project()
			def alreadyExists = new ProductionLine(project: project)
			def productionLine = new ProductionLine(project: project)
		
		when:
			handler.handle new CreateProductionLineCommand(productionLine)
			
		then:
			1 * productionLineDAO.findByProject(project) >> alreadyExists
		
		then:
			0 * productionLineDAO.save(_)
			thrown(ProductionLineAlreadyExistsException.class)
			
	}
	
}
