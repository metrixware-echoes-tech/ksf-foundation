package com.tocea.corolla.trees.utils

import org.junit.Rule;

import spock.lang.Specification;

import com.tocea.corolla.trees.domain.FolderNode
import com.tocea.corolla.trees.domain.ITree
import com.tocea.corolla.trees.domain.TreeNode;

class TreeNodeUtilsTest extends Specification {

	class BasicTree implements ITree {
		def Collection<TreeNode> nodes;
	}
	
	class AwesomeFolderNode extends FolderNode {
		
	}

	def "it should retrieve the max ID in a collection of nodes"() {
		
		given:
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes:[
					        		     new TreeNode(
					        		    		 id: 2, 
					        		    		 nodes:[
					        		    		        new TreeNode(id: 3, nodes: [])
					        		    		 ]
					        		     )  
					        		]
					       )
					]
			)
	
		when:
			def maxID = TreeNodeUtils.getMaxNodeId tree.nodes
			
		then:
			notThrown(Exception.class)
			
		then:
			maxID == 3
			
	}
	
	def "it should not fail when trying to get the max ID of an empty tree"() {
		
		given:
			def tree = new BasicTree(nodes: [])
	
		when:
			def maxID = TreeNodeUtils.getMaxNodeId tree.nodes
			
		then:
			notThrown(Exception.class)
			
		then:
			maxID == 0
			
	}
	
	def "it should retrieve the parent node of a node with a given ID"() {
		
		given:
			def nodeID = 3
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes:[
					        		     new TreeNode(
					        		    		 id: 2, 
					        		    		 nodes:[
					        		    		        new TreeNode(id: 3, nodes: [])
					        		    		 ]
					        		     )  
					        		]
					       )
					]
			)
	
		when:
			def parent = TreeNodeUtils.getParentNodeOf nodeID, tree.nodes
			
		then:
			notThrown(Exception.class)
			
		then:
			parent == tree.nodes[0].nodes[0]
		
	}
	
	def "it should not fail if the given node is at the root of the tree when trying to get its parent node"() {
		
		
		given:
			def nodeID = 1
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes:[]
					       )
					]
			)
	
		when:
			def parent = TreeNodeUtils.getParentNodeOf nodeID, tree.nodes
			
		then:
			notThrown(Exception.class)
			
		then:
			parent == null
		
	}
	
	def "it should not fail if the given node ID does not match any node of the tree when trying to get its parent node"() {
		
		given:
			def nodeID = 155
			def tree = new BasicTree(
					nodes: [
					        new TreeNode(
					        		id: 1, 
					        		nodes:[]
					       )
					]
			)
	
		when:
			def parent = TreeNodeUtils.getParentNodeOf nodeID, tree.nodes
			
		then:
			notThrown(Exception.class)
			
		then:
			parent == null
		
	}
	
	def "it should indicate if a node contains a node with a given ID"() {
		
		given:
			def nodeID = 2
			def parent =  new TreeNode(
	        		id: 1, 
	        		nodes:[
	        		     new TreeNode(
	        		    		 id: 2, 
	        		    		 nodes:[]
	        		     )  
	        		]
	       )
	
		when:
			def found = TreeNodeUtils.hasNodeWithId nodeID, parent, false
			
		then:
			notThrown(Exception.class)
			
		then:
			found == true
		
	}
	
	def "it should indicate if a node does not contains a node with a given ID"() {
		
		given:
			def nodeID = 135
			def parent =  new TreeNode(
	        		id: 1, 
	        		nodes:[
	        		     new TreeNode(
	        		    		 id: 2, 
	        		    		 nodes:[]
	        		     )  
	        		]
	       )
	
		when:
			def found = TreeNodeUtils.hasNodeWithId nodeID, parent, false
			
		then:
			notThrown(Exception.class)
			
		then:
			found == false
		
	}
	
	def "it should indicate if a node or any of its children contains a node with a given ID"() {
		
		given:
			def nodeID = 3
			def parent =  new TreeNode(
	        		id: 1, 
	        		nodes:[
	        		     new TreeNode(
	        		    		 id: 2, 
	        		    		 nodes:[new TreeNode(id: 3, nodes: [])]
	        		     )  
	        		]
	       )
	
		when:
			def found = TreeNodeUtils.hasNodeWithId nodeID, parent, true
			
		then:
			notThrown(Exception.class)
			
		then:
			found == true
			
	}
	
	def "it should indicate if a node is a FolderNode instance"() {
		
		given:
			def node = new FolderNode()
	
		when:
			def ok = TreeNodeUtils.isFolderNode node
			
		then:
			notThrown(Exception.class)
				
		then:
			ok
			
	}
	
	def "it should indicate if a node is a FolderNode instance even if it is an instance of a subclass of FolderNode"() {
		
		given:
			def node = new AwesomeFolderNode()
	
		when:
			def ok = TreeNodeUtils.isFolderNode node
			
		then:
			notThrown(Exception.class)
				
		then:
			ok
			
	} 
	
}