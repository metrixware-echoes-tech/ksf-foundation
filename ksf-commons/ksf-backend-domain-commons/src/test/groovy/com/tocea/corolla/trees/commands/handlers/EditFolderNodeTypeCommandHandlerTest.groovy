package com.tocea.corolla.trees.commands.handlers;

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.trees.commands.EditFolderNodeTypeCommand
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO;
import com.tocea.corolla.trees.domain.FolderNodeType
import com.tocea.corolla.trees.exceptions.MissingFolderNodeTypeInformationException;
import com.tocea.corolla.trees.exceptions.InvalidFolderNodeTypeInformationException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "EDIT_FOLDER_NODE_TYPE")
public class EditFolderNodeTypeCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IFolderNodeTypeDAO folderNodeTypeDAO = Mock(IFolderNodeTypeDAO)
	def EditFolderNodeTypeCommandHandler handler
	
	def setup() {
		handler = new EditFolderNodeTypeCommandHandler(
				folderNodeTypeDAO : folderNodeTypeDAO
		)
	}
	
	def "it should edit a folder node type"() {
		
		given:
			def nodeType = new FolderNodeType(
					id: "1",
					name: "folder", 
					icon: "http://awesome-pictures.com/image.png"
			)
			
		when:
			handler.handle new EditFolderNodeTypeCommand(nodeType)
			
		then:
			notThrown(Exception.class)
			
		then:
			1 * folderNodeTypeDAO.save(nodeType)
		
	}
	
	def "it should throw an exception if the folder node type is not defined"() {
			
		given:
			def nodeType = null
			
		when:
			handler.handle new EditFolderNodeTypeCommand(nodeType)
			
		then:
			thrown(MissingFolderNodeTypeInformationException.class)
	}
	
	def "it should throw an exception if the folder node type ID is missing"() {
		
		given:
			def nodeType = new FolderNodeType("folder", "http://awesome-pictures.com/image.png")
			
		when:
			handler.handle new EditFolderNodeTypeCommand(nodeType)
			
		then:
			thrown(InvalidFolderNodeTypeInformationException.class)
	}
	
}