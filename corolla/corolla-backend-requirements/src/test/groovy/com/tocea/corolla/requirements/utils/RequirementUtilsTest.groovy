package com.tocea.corolla.requirements.utils;

import com.tocea.corolla.requirements.domain.Requirement
import spock.lang.Specification;

public class RequirementUtilsTest extends Specification {

	def "it should clone a requirement object"() {
		
		given:
			def requirement = new Requirement(
					id: "1", 
					projectBranchId: "Branch1", 
					key:"clone_me",
					name: "Clone Me!"
			)
	
		when:
			def clone = RequirementUtils.clone requirement
	
		then:
			clone.id == null
			clone.projectBranchId == null
			clone.key == requirement.key
			clone.name == requirement.name
		
	}
	
	def "it should clone a requirement and set its branch id"() {
		
		given:
			def requirement = new Requirement(
					id: "1", 
					projectBranchId: "Branch1", 
					key:"clone_me",
					name: "Clone Me!"
			)
			def branchID = "Branch2"
			
		when:
			def clone = RequirementUtils.clone requirement, branchID
	
		then:
			clone.id == null
			clone.projectBranchId == branchID
			clone.key == requirement.key
			clone.name == requirement.name
		
	}
	
	def "it should find a requirement by its key"() {
		
		given:
			def requirements = [new Requirement(key: "K1"), new Requirement(key: 'K2')]
					
		when:
			def match = RequirementUtils.findByKey(requirements, requirements[0].key)
			
		then:
			match == requirements[0]
		
	}
	
	def "it should return null if the given key does not match any requirement"() {
		
		given:
			def requirements = [new Requirement(key: "K1"), new Requirement(key: 'K2')]
					
		when:
			def match = RequirementUtils.findByKey(requirements, "unknown_key")
			
		then:
			match == null
					
	}
	
	def "it should find a requirement by its ID"() {
		
		given:
			def requirements = [new Requirement(id: "K1"), new Requirement(id: 'K2')]
					
		when:
			def match = RequirementUtils.findByID(requirements, requirements[0].id)
			
		then:
			match == requirements[0]
		
	}
	
	def "it should return null if the given ID does not match any requirement"() {
		
		given:
			def requirements = [new Requirement(id: "K1"), new Requirement(id: 'K2')]
					
		when:
			def match = RequirementUtils.findByID(requirements, "0")
			
		then:
			match == null
					
	}
	
}