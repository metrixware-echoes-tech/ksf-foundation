package com.tocea.corolla.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.tocea.corolla.requirements.dao.IRequirementDAO;
import com.tocea.corolla.requirements.domain.Requirement;
import com.tocea.corolla.revisions.domain.IChange;
import com.tocea.corolla.revisions.domain.ICommit;
import com.tocea.corolla.revisions.services.IRevisionService;
import com.tocea.corolla.users.dao.IUserDAO;
import com.tocea.corolla.users.domain.User;

public class JaversApplicationTests extends AbstractSpringTest {

    @Autowired
    private Javers javers;

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IRevisionService revisionService;

    @Autowired
    private IRequirementDAO requirementDAO;

    @Test
    public void test() {

        final User user = new User();
        user.setLogin("jsnow");
        user.setFirstName("Jon");
        user.setLastName("Snow");

        this.userDAO.save(user);
        this.javers.commit("me", user);

        user.setLastName("Snoooow");

        this.userDAO.save(user);
        this.javers.commit("me", user);

        final List<CdoSnapshot> snapshots = this.javers.findSnapshots(QueryBuilder.byInstanceId(user.getId(), User.class).build());

        assertEquals(2, snapshots.size());

        assertEquals("Snow", snapshots.get(1).getPropertyValue("lastName"));
        assertEquals("Jon", snapshots.get(1).getPropertyValue("firstName"));

        assertEquals("Snoooow", snapshots.get(0).getPropertyValue("lastName"));
        assertEquals("Jon", snapshots.get(0).getPropertyValue("firstName"));

        final List<String> changes = snapshots.get(0).getChanged();

        assertEquals(1, changes.size());
        assertEquals("lastName", changes.get(0));

    }

    @Test
    public void testDiff() throws Exception {

        final Requirement req = new Requirement();
        req.setKey("GET_SNAPSHOT");
        req.setName("should get snapshot");

        this.requirementDAO.save(req);
        this.revisionService.commit(req);

        req.setKey("RETRIEVE_SNAPSHOT");
        req.setName("retrieve a snapshot");
        req.setDescription("it should recreate a snapshot of an object");

        this.requirementDAO.save(req);
        this.revisionService.commit(req);

        final List<ICommit> commits = (List<ICommit>) this.revisionService.getHistory(req.getId(), Requirement.class);

        final Requirement currentVersion = (Requirement) this.revisionService.getSnapshot(commits.get(0));
        final Requirement oldVersion = (Requirement) this.revisionService.getSnapshot(commits.get(1));

        final List<IChange> changes = this.revisionService.compare(oldVersion, currentVersion);

        assertEquals(3, changes.size());

        final Collection<String> propertiesChanged = Collections2.transform(changes, new Function<IChange, String>() {

            @Override
            public String apply(final IChange change) {
                return change.getPropertyName();
            }
        });

        assert propertiesChanged.containsAll(Lists.newArrayList("key", "name", "description"));

        Collections2.filter(changes, new Predicate<IChange>() {

            @Override
            public boolean apply(final IChange change) {
                return change.getPropertyName().equals("key");
            }
        });

        IChange change = this.findChangeByPropertyName("key", changes);

        assertNotNull(change);
        assertEquals("GET_SNAPSHOT", change.getLeftValue());
        assertEquals("RETRIEVE_SNAPSHOT", change.getRightValue());

        change = this.findChangeByPropertyName("name", changes);

        assertNotNull(change);
        assertEquals("should get snapshot", change.getLeftValue());
        assertEquals("retrieve a snapshot", change.getRightValue());

    }

    @Test
    public void testGetSnapshot() throws Exception {

        final Requirement req = new Requirement();
        req.setKey("GET_SNAPSHOT");
        req.setName("should get snapshot");

        this.requirementDAO.save(req);
        String commitID = this.revisionService.commit(req);

        final List<ICommit> commits0 = (List<ICommit>) this.revisionService.getHistory(req.getId(), Requirement.class);
        Object commit = this.revisionService.getSnapshot(commits0.get(0));

        req.setKey("RETRIEVE_SNAPSHOT");
        req.setName("retrieve a snapshot");

        this.requirementDAO.save(req);
        this.revisionService.commit(req);

        req.setDescription("it should recreate a snapshot of an object");

        this.requirementDAO.save(req);
        this.revisionService.commit(req);

        final List<ICommit> commits = (List<ICommit>) this.revisionService.getHistory(req.getId(), Requirement.class);
        assertEquals(3, commits.size());

        Requirement commit2 = (Requirement) this.revisionService.getSnapshot(commits.get(2));
        Requirement commit1 = (Requirement) this.revisionService.getSnapshot(commits.get(1));
        Requirement commit0 = (Requirement) this.revisionService.getSnapshot(commits.get(0));

        assertEquals("RETRIEVE_SNAPSHOT", commit0.getKey());
        assertEquals("retrieve a snapshot", commit0.getName());
        assertEquals("it should recreate a snapshot of an object", commit0.getDescription());

        assertEquals("RETRIEVE_SNAPSHOT", commit1.getKey());
        assertEquals("retrieve a snapshot", commit1.getName());
        assertEquals(null, commit1.getDescription());

        assertEquals("GET_SNAPSHOT", commit2.getKey());
        assertEquals("should get snapshot", commit2.getName());
        assertEquals(null, commit2.getDescription());

    }

    private IChange findChangeByPropertyName(final String name, final Collection<IChange> changes) {

        final Collection<IChange> match = Collections2.filter(changes, new Predicate<IChange>() {

            @Override
            public boolean apply(final IChange change) {
                return change.getPropertyName().equals(name);
            }
        });

        if (match != null && match.size() > 0) {
            return Lists.newArrayList(match).get(0);
        }

        return null;

    }

}
