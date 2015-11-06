package com.tocea.corolla.trees.commands.handlers;

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.test.utils.FunctionalDocRule;
import com.tocea.corolla.trees.commands.DeleteFolderNodeTypeCommand
import com.tocea.corolla.trees.dao.IFolderNodeTypeDAO;
import com.tocea.corolla.trees.domain.FolderNodeType
import com.tocea.corolla.trees.exceptions.MissingFolderNodeTypeInformationException;
import com.tocea.corolla.trees.exceptions.InvalidFolderNodeTypeInformationException;
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "DELETE_FOLDER_NODE_TYPE")
public class DeleteFolderNodeTypeCommandHandlerTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IFolderNodeTypeDAO folderNodeTypeDAO = Mock(IFolderNodeTypeDAO)
	def DeleteFolderNodeTypeCommandHandler handler
	
	def setup() {
		handler = new DeleteFolderNodeTypeCommandHandler(
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
			handler.handle new DeleteFolderNodeTypeCommand(nodeType.id)
			
		then:
			notThrown(Exception.class)
			
		then:
			folderNodeTypeDAO.findOne(nodeType.id) >> nodeType
			
		then:
			1 * folderNodeTypeDAO.delete(nodeType)
		
	}
	
	def "it should throw an exception if the folder node type ID is missing"() {
		
		given:
			def nodeType = new FolderNodeType(
					id: "",
					name: "folder", 
					icon: "http://awesome-pictures.com/image.png"
			)
			
		when:
			handler.handle new DeleteFolderNodeTypeCommand(nodeType.id)
			
		then:
			thrown(MissingFolderNodeTypeInformationException.class)
	}
	
}