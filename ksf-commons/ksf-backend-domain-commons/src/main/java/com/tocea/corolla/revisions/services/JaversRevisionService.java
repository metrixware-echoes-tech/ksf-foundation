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

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tocea.corolla.revisions.domain.Change;
import com.tocea.corolla.revisions.domain.Commit;
import com.tocea.corolla.revisions.domain.IChange;
import com.tocea.corolla.revisions.domain.ICommit;
import com.tocea.corolla.revisions.exceptions.InvalidCommitInformationException;
import com.tocea.corolla.revisions.exceptions.SnapshotBuildFailureException;
import fr.echoes.labs.ksf.users.security.api.CurrentUserService;
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
import org.springframework.stereotype.Service;

@Service
public class JaversRevisionService implements IRevisionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaversRevisionService.class);

    @Autowired
    private Javers javers;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    public void commit(final Object obj) {

        final String username = this.currentUserService.getCurrentUserLogin();
        LOGGER.info("new commit transaction for user " + username);

        javers.commit(username, obj);
    }

    @Override
    public List<IChange> compare(final Object oldVersion, final Object currentVersion) {

        final Diff diff = javers.compare(oldVersion, currentVersion);

        final List<ValueChange> changes = diff.getChangesByType(ValueChange.class);

        final List<IChange> results = Lists.newArrayList();

        if (changes != null) {
            results.addAll(Lists.transform(changes, new Function<ValueChange, IChange>() {

                @Override
                public IChange apply(final ValueChange valueChange) {
                    return new Change(valueChange);
                }

            }));
        }

        final List<ListChange> listChanges = diff.getChangesByType(ListChange.class);

        if (listChanges != null) {
            results.addAll(Lists.transform(listChanges, new Function<ListChange, IChange>() {

                @Override
                public IChange apply(final ListChange valueChange) {
                    final Change change = new Change();
                    change.setPropertyName(valueChange.getPropertyName());
                    change.setPropertyType(valueChange.getProperty().getType());
                    try {
                        change.setRightValue(PropertyUtils.getProperty(currentVersion, change.getPropertyName()));
                    } catch (final Exception e) {
                        LOGGER.error("[RevisionService] Cannot set right value : {}", e);
                        change.setRightValue(null);
                    }
                    try {
                        change.setLeftValue(PropertyUtils.getProperty(oldVersion, change.getPropertyName()));
                    } catch (final Exception e) {
                        LOGGER.error("[RevisionService] Cannot set left value : {}", e);
                        change.setLeftValue(null);
                    }
                    return change;
                }

            }));
        }

        return results;
    }

    public CdoSnapshot findCdoSnapshotByCommit(final Commit commit) {

        final String id = commit.getObjectID();
        final Class<?> clazz = commit.getObjectClass();

        final List<CdoSnapshot> snapshots = javers.findSnapshots(
                QueryBuilder.byInstanceId(id, clazz).build()
        );

        if (snapshots != null) {
            for (final CdoSnapshot snapshot : snapshots) {
                final String commitID = snapshot.getCommitId().valueAsNumber().toBigInteger().toString();
                if (commitID.equals(commit.getId())) {
                    return snapshot;
                }
            }
        }

        return null;

    }

    @Override
    public ICommit findCommitByID(final String objectID, final Class<?> objectClass, final String commitID) {

        final Collection<ICommit> history = getHistory(objectID, objectClass);

        for (final ICommit commit : history) {
            if (commit.getId().equals(commitID)) {
                return commit;
            }
        }

        return null;
    }

    @Override
    public Collection<ICommit> getHistory(final String id, final Class<?> clazz) {

        final List<CdoSnapshot> snapshots = javers.findSnapshots(
                QueryBuilder.byInstanceId(id, clazz).build()
        );

        if (snapshots == null) {
            return Lists.newArrayList();
        }

        return Lists.transform(snapshots, new Function<CdoSnapshot, ICommit>() {
            @Override
            public ICommit apply(final CdoSnapshot snapshot) {
                return new Commit(id, clazz, snapshot);
            }
        });

    }

    @Override
    public ICommit getPreviousCommit(final String objectID, final Class<?> objectClass, final String commitID) {

        final Collection<ICommit> history = getHistory(objectID, objectClass);

        final Iterator<ICommit> it = history.iterator();

        while (it.hasNext()) {
            if (it.next().getId().equals(commitID)) {
                return it.hasNext() ? it.next() : null;
            }
        }

        return null;
    }

    @Override
    public Object getSnapshot(final ICommit commit) {

        final CdoSnapshot cdoSnapshot = findCdoSnapshotByCommit((Commit) commit);

        if (cdoSnapshot == null) {
            throw new InvalidCommitInformationException("No data found for this commit [id=" + commit.getId() + "]");
        }

        final Class<?> clazz = ((Commit) commit).getObjectClass();

        Object object = null;

        try {

            object = clazz.newInstance();

            for (final Property prop : cdoSnapshot.getProperties()) {
                PropertyUtils.setProperty(object, prop.getName(), cdoSnapshot.getPropertyValue(prop.getName()));
            }

        } catch (final Exception e) {

            throw new SnapshotBuildFailureException(e);
        }

        return object;

    }

    @Override
    public Object getSnapshot(final String objectID, final Class<?> objectClass, final String commitID) {

        final Commit commit = new Commit();
        commit.setId(commitID);
        commit.setObjectID(objectID);
        commit.setObjectClass(objectClass);

        return getSnapshot(commit);

    }

}
