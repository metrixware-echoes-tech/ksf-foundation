package com.tocea.corolla.trees.services

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.trees.domain.ITree
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.trees.exceptions.InvalidTreeNodeInformationException;
import com.tocea.corolla.trees.exceptions.MissingTreeInformationException;
import com.tocea.corolla.trees.exceptions.MissingTreeNodeInformationException;
import com.tocea.corolla.trees.predicates.ITreeNodePredicate
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "TREE_MANAGEMENT_SERVICE")
class TreeManagementServiceTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def ITreeManagementService service
	
	class BasicTree implements ITree {
		def Collection<TreeNode> nodes;
	}
	
	def setup() {
		service = new TreeManagementService()
	}
	
	def "it should insert a new node in an empty tree"() {
		
		given:
			def tree = new BasicTree(nodes: [])
			def node = new TreeNode();
		
		when:
			service.insertNode(tree, null, node)
	
		then:
			notThrown(Exception.class)
			
		then:
			tree.nodes.size() == 1
			tree.nodes[0].id == 1
		
	}
	
	def "it should insert a new tree node in a non empty tree"() {
		
		given:
			def node = new TreeNode();
			def tree = new BasicTree(nodes: [new TreeNode(id: 1, nodes: [])])
		
		when:
			service.insertNode(tree, null, node)
	
		then:
			notThrown(Exception.class)
			
		then:
			tree.nodes.size() == 2
			tree.nodes[1].id == 2
		
	}
	
	def "it should insert a new tree node inside another node of the tree"() {
		
		given:
			def node = new TreeNode();
			def parentID = 1
			def tree = new BasicTree(nodes: [new TreeNode(id: 1, nodes: [])])
		
		when:
			service.insertNode(tree, parentID, node)
	
		then:
			notThrown(Exception.class)
			
		then:
			tree.nodes.size() == 1
			tree.nodes[0].nodes.size() == 1
			tree.nodes[0].nodes[0].id == 2
	}
	
	def "it should throw an exception if the tree cannot be found when inserting a node in a tree"() {
		
		given:
			def node = new TreeNode();
			def tree = null
		
		when:
			service.insertNode(tree, null, node)
		
		then:
			thrown(MissingTreeInformationException.class)
		
	}
	
	def "it should throw an exception if the node to insert is null"() {
		
		given:
			def node = null
			def parentID = null
			def tree = new BasicTree(nodes: [])
		
		when:
			service.insertNode(tree, parentID, node)
	
		then:
			thrown(MissingTreeNodeInformationException.class)
			
	}
	
	def "it should throw an exception if the parent ID is invalid when inserting a node in a tree"() {
		
		given:
			def node = new TreeNode()
			def parentID = 1
			def tree = new BasicTree(nodes: [])
		
		when:
			service.insertNode(tree, parentID, node)
	
		then:
			thrown(InvalidTreeNodeInformationException.class)
			
	}
	
	def "it should move a tree node to the root of the tree"() {
		
		given:
			def nodeID = 2
			def newParentID = null
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [new TreeNode(id: 2, nodes: [])]
					        )
					]
			)
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			notThrown(Exception.class)
			
		then:
			tree.nodes.size() == 2
			tree.nodes[1].id == nodeID
			tree.nodes[0].nodes.isEmpty()
			
	}
	
	def "it should move a tree node in a tree"() {
		
		given:
			def nodeID = 3
			def newParentID = 2
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [new TreeNode(id: 2, nodes: [])]
					        ),
					        new TreeNode(id: 3, nodes: [])
					]
			)
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			notThrown(Exception.class)

		then:
			tree.nodes.size() == 1
			tree.nodes[0].nodes.size() == 1
			tree.nodes[0].nodes[0].nodes.size() == 1
			tree.nodes[0].nodes[0].nodes[0].id == nodeID		
			
	}
	
	def "it should move a tree node that has children in a tree"() {
		
		given:
			def nodeID = 1
			def newParentID = 3
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [new TreeNode(id: 2, nodes: [])]
					        ),
					        new TreeNode(id: 3, nodes: [])
					]
			)
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			notThrown(Exception.class)
	
		then:
			tree.nodes.size() == 1
			tree.nodes[0].nodes.size() == 1		
			tree.nodes[0].nodes[0].nodes.size() == 1		
			tree.nodes[0].id == 3
			tree.nodes[0].nodes[0].id == 1
			tree.nodes[0].nodes[0].nodes[0].id == 2
					
	}
	
	def "it should not move a tree node inside one of its children"() {
		
		given:
			def nodeID = 1
			def newParentID = 2
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [new TreeNode(id: 2, nodes: [])]
					        ),
					        new TreeNode(id: 3, nodes: [])
					]
			)
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			thrown(InvalidTreeNodeInformationException.class)	
		
	}
	
	def "it should throw an exception if the tree is not defined when moving a node in a tree"() {
		
		given:
			def nodeID = 1
			def newParentID = 2
			def tree = null
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			thrown(MissingTreeInformationException.class)	
		
	}
	
	def "it should throw an exception if the node ID is not defined when moving a node in a tree"() {
		
		given:
			def nodeID = null
			def newParentID = 2
			def tree = new BasicTree(nodes: [])
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			thrown(MissingTreeNodeInformationException.class)
		
	}
	
	def "it should throw an exception if the node ID is invalid when moving a node in a tree"() {
		
		given:
			def nodeID = 5
			def newParentID = 2
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [new TreeNode(id: 2, nodes: [])]
					        ),
					        new TreeNode(id: 3, nodes: [])
					]
			)
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			thrown(InvalidTreeNodeInformationException.class)
		
	}
	
	def "it should throw an exception if the new parent ID is invalid when moving a node in a tree"() {
		
		given:
			def nodeID = 2
			def newParentID = 5
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [new TreeNode(id: 2, nodes: [])]
					        ),
					        new TreeNode(id: 3, nodes: [])
					]
			)
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			thrown(InvalidTreeNodeInformationException.class)
		
	}
	
	def "it should throw an exception if the new parent node ID is invalid when moving a node in a tree"() {
		
		given:
			def nodeID = 2
			def newParentID = 5
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [new TreeNode(id: 2, nodes: [])]
					        ),
					        new TreeNode(id: 3, nodes: [])
					]
			)
		
		when:
			service.moveNode(tree, nodeID, newParentID)
	
		then:
			thrown(InvalidTreeNodeInformationException.class)
		
	}
	
	def "it should remove a leaf node in a tree"() {
		
		given:		
			def nodeID = 1
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(id: 1, nodes: []), 
					        new TreeNode(id: 2, nodes: [])
					]
			)
		
		when:
			service.removeNode(tree, nodeID)
	
		then:
			notThrown(Exception.class)
			
		then:
			tree.nodes.size() == 1
			tree.nodes[0].id == 2
		
	}
	
	def "it should remove a node that contains children in a tree"() {
		
		given:		
			def nodeID = 1
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [
					        		        new TreeNode(id: 3, nodes: [])
					        		]
					        ), 
					        new TreeNode(id: 2, nodes: [])
					]
			)
		
		when:
			service.removeNode(tree, nodeID)
	
		then:
			notThrown(Exception.class)
			
		then:
			tree.nodes.size() == 1
			tree.nodes[0].id == 2
		
	}
	
	def "it should throw an exception if the tree is not defined when removing a node"() {
		
		given:		
			def nodeID = 1
			def tree = null
		
		when:
			service.removeNode(tree, nodeID)
	
		then:
			thrown(MissingTreeInformationException.class)
		
	}
	
	def "it should throw an exception if the node ID is null when removing a node"() {
		
		given:		
			def nodeID = null
			def tree = new BasicTree(nodes: [new TreeNode(id: 1, nodes: [])])
		
		when:
			service.removeNode(tree, nodeID)
	
		then:
			thrown(MissingTreeNodeInformationException.class)
		
	}
	
	def "it should throw an exception if the node ID is invalid when removing a node"() {
		
		given:		
			def nodeID = 2
			def tree = new BasicTree(nodes: [new TreeNode(id: 1, nodes: [])])
		
		when:
			service.removeNode(tree, nodeID)
	
		then:
			thrown(InvalidTreeNodeInformationException.class)
		
	}
	
	def "it should apply a predicate on a tree to find a specific node"() {
		
		given:
			def predicate = new ITreeNodePredicate() {
				@Override
				public boolean test(TreeNode node) {
					return node.id == 2
				}
			}
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes: [new TreeNode(id: 2, nodes: [])]
					        ),
					        new TreeNode(id: 3, nodes: [])
					]
			)
	
		when:
			def node = service.findNode(tree, predicate)
			
		then:
			notThrown(Exception.class)
			
		then:
			node != null
			node.id == 2
		
	}
	
}