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
package com.tocea.corolla.requirements.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.tocea.corolla.requirements.domain.Requirement;

public class RequirementUtils {

	/**
	 * Clones a requirement instance
	 * @param requirement
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public static Requirement clone(Requirement requirement) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		
		Requirement clone = (Requirement) BeanUtils.cloneBean(requirement);
		clone.setId(null);
		clone.setProjectBranchId(null);
		
		return clone;
		
	}
	
	/**
	 * Clones a requirement instance and sets a new project branch ID
	 * @param requirement
	 * @param projectBranchID
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public static Requirement clone(Requirement requirement, String projectBranchID) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		
		Requirement clone = clone(requirement);
		
		if (clone != null) {
			clone.setProjectBranchId(projectBranchID);
		}
		
		return clone;
	}
	
	
	/**
	 * Finds a requirement from its key in a collection of requirements
	 * @param requirements
	 * @param key
	 * @return
	 */
	public static Requirement findByKey(Collection<Requirement> requirements, final String key) {
		
		Collection<Requirement> match = Collections2.filter(requirements, new Predicate<Requirement>() {

			@Override
			public boolean apply(Requirement requirement) {
				if (requirement != null) {
					return requirement.getKey().equals(key);
				}
				return false;
			}
			
		});
		
		return match.isEmpty() ? null : match.iterator().next();	
	}
	
	/**
	 * Finds a requirements from its ID in a collection of requirements
	 * @param requirements
	 * @param ID
	 * @return
	 */
	public static Requirement findByID(Collection<Requirement> requirements, final String ID) {
		
		Collection<Requirement> match = Collections2.filter(requirements, new Predicate<Requirement>() {

			@Override
			public boolean apply(Requirement requirement) {
				if (requirement != null) {
					return requirement.getId().equals(ID);
				}
				return false;
			}
			
		});
		
		return match.isEmpty() ? null : match.iterator().next();	
	}
	
}