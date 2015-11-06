package com.tocea.corolla.products.utils;

import java.util.Collection;

import org.junit.Rule;

import com.tocea.corolla.products.domain.Project
import com.tocea.corolla.products.utils.ProjectUtils;

import spock.lang.Specification;


public class ProjectUtilsTest extends Specification {
	
	def "it should extract all tags found in a given collection of projects into a single iterable set"() {
		
		given:
			def projects = [new Project(tags: ["1", "2"]), new Project(tags:[]), new Project(tags:["3"])]
	
		when:
			def tags = ProjectUtils.extractTags(projects)
			
		then:
			notThrown(Exception.class)
			
		then:
			tags.size() == 3
		
	}
	
}