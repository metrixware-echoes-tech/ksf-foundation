package com.tocea.corolla.portfolio.utils;

import java.util.Collection;

import org.junit.Rule;

import com.tocea.corolla.portfolio.domain.ProjectNode
import com.tocea.corolla.trees.domain.ITree;
import com.tocea.corolla.trees.domain.TreeNode;

import spock.lang.Specification;


public class PortfolioUtilsTest extends Specification {
	
	def "it should return the list of project IDs contained in a given tree"() {
		
		given:
			def nodes = [
			             new ProjectNode(
			            		 id: 1, 
			            		 projectId: "P1", 
			            		 nodes:[
			            		        new ProjectNode(id: 2, projectId: "P2", nodes:[]),
			            		        new TreeNode(id: 3, nodes:[])
			            		 ]
			             )
			]
		
					
		when:
			def result = PortfolioUtils.getProjectIDs nodes
			
		then:
			notThrown(Exception.class)
			
		then:
			result != null
			result.size() == 2
			result.contains "P1"
			result.contains "P2"
			
	}
	
	def "it should indicate if a node is a ProjectNode"() {
		
		given:
			def node = new ProjectNode(id: 2, projectId: "P2", nodes:[])
	
		when:
			def ok = PortfolioUtils.isProjectNode node
			
		then:
			notThrown(Exception.class)
			
		then:
			ok
		
	}
	
}