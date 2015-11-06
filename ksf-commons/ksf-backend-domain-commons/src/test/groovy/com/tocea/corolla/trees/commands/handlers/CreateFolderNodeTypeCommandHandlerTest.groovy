package com.tocea.corolla.trees.commands.handlers;

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.trees.commands.CreateFolderNodeTypeCommand
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO;
import com.tocea.corolla.trees.domain.FolderNodeType
import com.tocea.corolla.trees.exceptions.MissingFolderNodeTypeInformationException;
import com.tocea.corolla.trees.exceptions.InvalidFolderNodeTypeInformationException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "ADD_FOLDER_NODE_TYPE")
public class CreateFolderNodeTypeCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IFolderNodeTypeDAO folderNodeTypeDAO = Mock(IFolderNodeTypeDAO)
	def CreateFolderNodeTypeCommandHandler handler
	
	def setup() {
		handler = new CreateFolderNodeTypeCommandHandler(
				folderNodeTypeDAO : folderNodeTypeDAO
		)
	}
	
	def "it should create a new folder node type"() {
		
		given:
			def nodeType = new FolderNodeType("folder", "http://awesome-pictures.com/image.png")
			
		when:
			handler.handle new CreateFolderNodeTypeCommand(nodeType)
			
		then:
			notThrown(Exception.class)
			
		then:
			1 * folderNodeTypeDAO.save(nodeType)
		
	}
	
	def "it should throw an exception if the folder node type is not defined"() {
			
		given:
			def nodeType = null
			
		when:
			handler.handle new CreateFolderNodeTypeCommand(nodeType)
			
		then:
			thrown(MissingFolderNodeTypeInformationException.class)
	}
	
	def "it should throw an exception if the folder node type ID is already defined"() {
		
		given:
			def nodeType = new FolderNodeType("folder", "http://awesome-pictures.com/image.png")
			nodeType.id = "1"
			
		when:
			handler.handle new CreateFolderNodeTypeCommand(nodeType)
			
		then:
			thrown(InvalidFolderNodeTypeInformationException.class)
	}
	
}