package com.tocea.corolla.products.commands.handlers

import org.junit.Rule;
import org.mockito.Mockito;

import spock.lang.Specification;

import com.tocea.corolla.products.exceptions.*;
import com.tocea.corolla.requirements.commands.CreateRequirementTypeCommand;
import com.tocea.corolla.requirements.commands.handlers.CreateRequirementTypeCommandHandler;
import com.tocea.corolla.requirements.dao.IRequirementTypeDAO;
import com.tocea.corolla.requirements.domain.RequirementType;
import com.tocea.corolla.requirements.exceptions.InvalidRequirementTypeInformationException;
import com.tocea.corolla.requirements.exceptions.MissingRequirementTypeInformationException;
import com.tocea.corolla.requirements.exceptions.RequirementTypeAlreadyExistException;
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "ADD_REQUIREMENT_TYPE")
class CreateRequirementTypeCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IRequirementTypeDAO typeDAO = Mock(IRequirementTypeDAO)	
	def CreateRequirementTypeCommandHandler handler
	
	def setup() {
		handler = new CreateRequirementTypeCommandHandler(
				typeDAO : typeDAO
		)
	}
	
	def "it should create a requirement type"() {
		
		given:
			def type = new RequirementType()
			type.key = "REQ"
			type.name = "Requirement"
		
		when:
			handler.handle new CreateRequirementTypeCommand(type)
	
		then:
			typeDAO.findByKey(type.key) >> null
			notThrown(Exception.class)
			1 * typeDAO.save(type)	
			
	}
	
	def "it should not create two requirement type with the same key"() {
		
		given:
			def type = new RequirementType()
			type.key = "REQ"
			type.name = "Requirement"
			def sameType = new RequirementType(key: type.key, name: type.name)
		
		when:
			handler.handle new CreateRequirementTypeCommand(type)
	
		then:
			typeDAO.findByKey(type.key) >> sameType
			0 * typeDAO.save(_)
			thrown(RequirementTypeAlreadyExistException.class)
		
	}
	
	def "it should throw an exception if the type is null"() {
		
		given:
			def type = null
		
		when:
			handler.handle new CreateRequirementTypeCommand(type)
	
		then:
			0 * typeDAO.save(_)
			thrown(MissingRequirementTypeInformationException.class)
			
	}
	
	def "it should throw an exception if the ID is already defined"() {
		
		given:
			def type = new RequirementType()
			type.id = "1"
			type.key = "REQ"
			type.name = "Requirement"
		
		when:
			handler.handle new CreateRequirementTypeCommand(type)
	
		then:
			0 * typeDAO.save(_)
			thrown(InvalidRequirementTypeInformationException.class)
			
	}
	
}