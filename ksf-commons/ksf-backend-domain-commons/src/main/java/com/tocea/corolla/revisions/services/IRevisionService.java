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
 * Corolla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Corolla.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tocea.corolla.revisions.services;

import java.util.Collection;
import java.util.List;

import com.tocea.corolla.revisions.domain.IChange;
import com.tocea.corolla.revisions.domain.ICommit;


public interface IRevisionService {

	/**
	 * Creates a new version of an object
	 * @param obj
	 */
	public String commit(Object obj);
	
	/**
	 * Retrieves the history of an object
	 * @param id
	 * @param clazz
	 * @return
	 */
	public Collection<ICommit> getHistory(String id, Class<?> clazz);
	
	/**
	 * Compares to objects and retrieves the list of changes
	 * @param oldVersion
	 * @param currentVersion
	 * @return
	 */
	public List<IChange> compare(Object oldVersion, Object currentVersion);
	
	/**
	 * Retrieve the state of an object at a given commit
	 * @param commit
	 * @return
	 */
	public Object getSnapshot(ICommit commit);
	
	/**
	 * Retrieve the state of an object at a given commit
	 * @param objectID
	 * @param objectClass
	 * @param commitID
	 * @return
	 */
	public Object getSnapshot(String objectID, Class<?> objectClass, String commitID);
	
	/**
	 * Retrieves the commit that was made before a specific commit
	 * @param commit
	 * @return
	 */
	public ICommit getPreviousCommit(String objectID, Class<?> objectClass, String commitID);
	
	/**
	 * Retrieves a commit from its ID
	 * @param objectID
	 * @param objectClass
	 * @param commitID
	 * @return
	 */
	public ICommit findCommitByID(String objectID, Class<?> objectClass, String commitID);
	
}
