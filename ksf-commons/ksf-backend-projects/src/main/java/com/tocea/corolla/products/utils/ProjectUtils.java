/*
 * Corolla - A Tool to manage software requirements and test cases 
 * Copyright (C) 2015 Tocea
 * 
 * This file is part of Corolla.
 * 
 * Corolla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, 
 * or any later version.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Corolla.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.products.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tocea.corolla.products.domain.Project;
import java.util.Collection;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Define a list of utility methods
 * that can be applied on Project objects
 * @author dmichel
 *
 */
public class ProjectUtils {
	
	/**
	 * Retrieves a specific project from its ID
	 * in a collection of Project instances
	 * @param ID
	 * @param projects
	 * @return a Project instance of null if no project could have been found
	 */
	public static Project findByID(final String ID, Collection<Project> projects) {
		
		Collection<Project> match = Collections2.filter(projects, new Predicate<Project>() {
			@Override
			public boolean apply(Project project) {
				if (project != null && project.getId() != null) {
					return project.getId().equals(ID);
				}
				return false;
			}			
		});
		
		return match.isEmpty() ? null : Lists.newArrayList(match).get(0);
		
	}
	
	/**
	 * Extract all tags that can be found in a given collection of projects
	 * (remove duplicates)
	 * @param projects
	 * @return
	 */
	public static Iterable<String> extractTags(Collection<Project> projects) {
		
		Iterable<String> itTags = null;
		
		for(Project project : projects) {
			itTags = itTags != null ? Iterables.concat(itTags, project.getTags()) : project.getTags();
		}
		
		return ImmutableSet.copyOf(itTags).asList();
	}

	/**
	 * Predicate used to remove duplicates
	 * in a collection of projects
	 *
	 */
	public static class DuplicateRemover implements Predicate<Project> {
            private static final Logger LOG = LoggerFactory.getLogger(DuplicateRemover.class.getName());

            
            
		private final Set<String> ids = Sets.newHashSet();
		
		@Override
		public boolean apply(Project project) {
			LOG.info("Traitement {}", project.getId(), project);
			if (project != null) {
				if (!ids.contains(project.getId())) {
                                    
					ids.add(project.getId());
					return true;
				}
			}
			
			return false;
		}

	}
	
}