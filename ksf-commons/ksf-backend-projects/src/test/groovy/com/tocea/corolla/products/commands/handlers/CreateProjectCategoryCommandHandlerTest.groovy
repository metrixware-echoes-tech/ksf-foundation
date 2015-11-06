package com.tocea.corolla.products.commands.handlers

import org.junit.Rule;
import org.mockito.Mockito;

import spock.lang.Specification;

import com.tocea.corolla.products.commands.CreateProjectCategoryCommand
import com.tocea.corolla.products.dao.IProjectCategoryDAO;
import com.tocea.corolla.products.domain.ProjectCategory
import com.tocea.corolla.products.exceptions.*;
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "ADD_PROJECT_CATEGORY")
class CreateProjectCategoryCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectCategoryDAO categoryDAO = Mock(IProjectCategoryDAO)	
	def CreateProjectCategoryCommandHandler handler
	
	def setup() {
		handler = new CreateProjectCategoryCommandHandler(
				categoryDAO : categoryDAO
		)
	}
	
	def "it should create a new project category"() {
		
		given:
			def category = new ProjectCategory()
			category.name = "Awesome"
		
		when:
			handler.handle new CreateProjectCategoryCommand(category)
	
		then:
			categoryDAO.findByName(category.name) >> null
			
		then:
			1 * categoryDAO.save(_)
			notThrown(Exception.class)
	}
	
	def "it should not create two project categories with the same name"() {
		
		given:
			def category = new ProjectCategory()
			category.name = "Awesome"
		
		when:
			handler.handle new CreateProjectCategoryCommand(category)
				
		then:
			categoryDAO.findByName(category.name) >> new ProjectCategory(name: category.name)
			
		then:
			0 * categoryDAO.save(_)
			thrown(ProjectCategoryAlreadyExistException.class)
		
	}
	
	def "it should throw an exception if the project status is null"() {
		
		given:
			def category = null
		
		when:
			handler.handle new CreateProjectCategoryCommand(category)
				
		then:
			0 * categoryDAO.save(_)
			thrown(MissingProjectCategoryInformationException.class)
			
	}
	
	def "it should throw an exception if the ID is already defined"() {
		
		given:
			def category = new ProjectCategory(id: "15", name: "Awesome")
		
		when:
			handler.handle new CreateProjectCategoryCommand(category)
			
		then:
			0 * categoryDAO.save(_)
			thrown(InvalidProjectCategoryInformationException.class)
			
	}
	
}