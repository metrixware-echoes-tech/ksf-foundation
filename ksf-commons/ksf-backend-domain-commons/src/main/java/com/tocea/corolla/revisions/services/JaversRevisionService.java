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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.core.metamodel.property.Property;
import org.javers.repository.jql.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tocea.corolla.revisions.domain.Change;
import com.tocea.corolla.revisions.domain.Commit;
import com.tocea.corolla.revisions.domain.IChange;
import com.tocea.corolla.revisions.domain.ICommit;
import com.tocea.corolla.revisions.exceptions.InvalidCommitInformationException;
import com.tocea.corolla.revisions.exceptions.SnapshotBuildFailureException;

@Service
public class JaversRevisionService implements IRevisionService {

	private static final Logger	LOGGER = LoggerFactory.getLogger(JaversRevisionService.class);
	
	private static String DEFAULT_USER = "unknown";
	
	@Autowired
	private Javers javers;
	
	/**
	 * Retrieves the name of the current user
	 * @return
	 */
	private String getUsername() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null) {
			
			User user = (User) auth.getPrincipal();
			
			if (user != null) {
				
				return user.getUsername();
			}
			
		}
		
		return DEFAULT_USER;	
	}
	
	@Override
	public void commit(Object obj) {
		
		String username = getUsername();
		LOGGER.info("new commit transaction for user "+username);
		
		javers.commit(username, obj);
	}
	
	@Override
	public Collection<ICommit> getHistory(final String id, final Class<?> clazz) {
		
		List<CdoSnapshot> snapshots = javers.findSnapshots(
				QueryBuilder.byInstanceId(id, clazz).build()
		);
				
		if (snapshots == null) {
			return Lists.newArrayList();
		}
		
		return Lists.transform(snapshots, new Function<CdoSnapshot, ICommit>() {
			@Override
			public ICommit apply(CdoSnapshot snapshot) {				
				return new Commit(id, clazz, snapshot);
			}			
		});

	}
	
	@Override
	public ICommit findCommitByID(String objectID, Class<?> objectClass, String commitID) {
		
		Collection<ICommit> history = getHistory(objectID, objectClass);
		
		for(ICommit commit : history) {
			if (commit.getId().equals(commitID)) {
				return commit;
			}
		}
		
		return null;
	}
	
	@Override
	public ICommit getPreviousCommit(String objectID, Class<?> objectClass, String commitID) {
		
		Collection<ICommit> history = getHistory(objectID, objectClass);
		
		Iterator<ICommit> it = history.iterator();
		
		while(it.hasNext()) {
			if (it.next().getId().equals(commitID)) {
				return it.hasNext() ? it.next() : null;
			}
		}
		
		return null;
	}
	
	@Override
	public List<IChange> compare(final Object oldVersion, final Object currentVersion) {
		
		Diff diff = javers.compare(oldVersion, currentVersion);

		List<ValueChange> changes = diff.getChangesByType(ValueChange.class);
		
		List<IChange> results = Lists.newArrayList();
		
		if (changes != null) {
			results.addAll(Lists.transform(changes, new Function<ValueChange, IChange>() {
	
				@Override
				public IChange apply(ValueChange valueChange) {
					return new Change(valueChange);
				}
				
			}));
		}
		
		List<ListChange> listChanges = diff.getChangesByType(ListChange.class);
		
		if (listChanges != null) {
			results.addAll(Lists.transform(listChanges, new Function<ListChange, IChange>() {
	
				@Override
				public IChange apply(ListChange valueChange) {
					Change change = new Change();
					change.setPropertyName(valueChange.getPropertyName());
					change.setPropertyType(valueChange.getProperty().getType());
					try {
						change.setRightValue(PropertyUtils.getProperty(currentVersion, change.getPropertyName()));
					} catch (Exception e) {
						LOGGER.error("[RevisionService] Cannot set right value : {}", e);
						change.setRightValue(null);
					}
					try {
						change.setLeftValue(PropertyUtils.getProperty(oldVersion, change.getPropertyName()));
					} catch (Exception e) {
						LOGGER.error("[RevisionService] Cannot set left value : {}", e);
						change.setLeftValue(null);
					}
					return change;
				}
				
			}));
		}
		
		return results;
	}

	@Override
	public Object getSnapshot(ICommit commit) {
				
		CdoSnapshot cdoSnapshot = findCdoSnapshotByCommit((Commit)commit);
		
		if (cdoSnapshot == null) {
			throw new InvalidCommitInformationException("No data found for this commit [id="+commit.getId()+"]");
		}
		
		Class<?> clazz = ((Commit) commit).getObjectClass();
		
		Object object= null;
		
		try {
			
			object = clazz.newInstance();
				
			for(Property prop : cdoSnapshot.getProperties()) {							
				PropertyUtils.setProperty(object, prop.getName(), cdoSnapshot.getPropertyValue(prop.getName()));			
			}
		
		} catch (Exception e) {
			
			throw new SnapshotBuildFailureException(e);
		}
		
		return object;
		
	}
	
	
	@Override
	public Object getSnapshot(String objectID, Class<?> objectClass, String commitID) {
		
		Commit commit = new Commit();
		commit.setId(commitID);
		commit.setObjectID(objectID);
		commit.setObjectClass(objectClass);
		
		return getSnapshot(commit);
		
	}
	
	public CdoSnapshot findCdoSnapshotByCommit(Commit commit) {
		
		String id = commit.getObjectID();
		Class<?> clazz = commit.getObjectClass();
		
		List<CdoSnapshot> snapshots = javers.findSnapshots(
				QueryBuilder.byInstanceId(id, clazz).build()
		);
		
		if (snapshots != null) {		
			for(CdoSnapshot snapshot : snapshots) {
				String commitID = snapshot.getCommitId().valueAsNumber().toBigInteger().toString();
				if (commitID.equals(commit.getId())) {
					return snapshot;
				}
			}		
		}
		
		return null;
		
	}

}
