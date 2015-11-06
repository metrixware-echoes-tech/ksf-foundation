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
		
		User user = new User();
		user.setLogin("jsnow");
		user.setFirstName("Jon");
		user.setLastName("Snow");
		
		userDAO.save(user);
		javers.commit("me", user);
		
		user.setLastName("Snoooow");
		
		userDAO.save(user);
		javers.commit("me", user);
		
		List<CdoSnapshot> snapshots = javers.findSnapshots(
				QueryBuilder.byInstanceId(user.getId(), User.class).build()
		);
				
		assertEquals(2, snapshots.size()); 	
		
		assertEquals("Snow", snapshots.get(1).getPropertyValue("lastName"));
		assertEquals("Jon", snapshots.get(1).getPropertyValue("firstName"));
		
		assertEquals("Snoooow", snapshots.get(0).getPropertyValue("lastName"));
		assertEquals("Jon", snapshots.get(0).getPropertyValue("firstName"));		
		
		List<String> changes = snapshots.get(0).getChangedPropertyNames();
		
		assertEquals(1, changes.size());
		assertEquals("lastName", changes.get(0));
		
	}
	
	@Test
	public void testGetSnapshot() throws Exception {
		
		Requirement req = new Requirement();
		req.setKey("GET_SNAPSHOT");
		req.setName("should get snapshot");
		
		requirementDAO.save(req);
		revisionService.commit(req);

		req.setKey("RETRIEVE_SNAPSHOT");
		req.setName("retrieve a snapshot");
		
		requirementDAO.save(req);
		revisionService.commit(req);
		
		req.setDescription("it should recreate a snapshot of an object");
		
		requirementDAO.save(req);
		revisionService.commit(req);
		
		List<ICommit> commits = (List<ICommit>) revisionService.getHistory(req.getId(), Requirement.class);
		
		assertEquals(3, commits.size());
		
		Requirement snapshot = (Requirement) revisionService.getSnapshot(commits.get(0));
		
		assertEquals("RETRIEVE_SNAPSHOT", snapshot.getKey());
		assertEquals("retrieve a snapshot", snapshot.getName());
		assertEquals("it should recreate a snapshot of an object", snapshot.getDescription());
		
		snapshot = (Requirement) revisionService.getSnapshot(commits.get(1));
		
		assertEquals("RETRIEVE_SNAPSHOT", snapshot.getKey());
		assertEquals("retrieve a snapshot", snapshot.getName());
		assertEquals(null, snapshot.getDescription());		
		
		snapshot = (Requirement) revisionService.getSnapshot(commits.get(2));
		
		assertEquals("GET_SNAPSHOT", snapshot.getKey());
		assertEquals("should get snapshot", snapshot.getName());
		assertEquals(null, snapshot.getDescription());
		
	}
	
	@Test
	public void testDiff() throws Exception {
		
		Requirement req = new Requirement();
		req.setKey("GET_SNAPSHOT");
		req.setName("should get snapshot");
		
		requirementDAO.save(req);
		revisionService.commit(req);

		req.setKey("RETRIEVE_SNAPSHOT");
		req.setName("retrieve a snapshot");
		req.setDescription("it should recreate a snapshot of an object");
		
		requirementDAO.save(req);
		revisionService.commit(req);
		
		List<ICommit> commits = (List<ICommit>) revisionService.getHistory(req.getId(), Requirement.class);
		
		Requirement currentVersion = (Requirement) revisionService.getSnapshot(commits.get(0));
		Requirement oldVersion = (Requirement) revisionService.getSnapshot(commits.get(1));
		
		List<IChange> changes = revisionService.compare(oldVersion, currentVersion);
		
		assertEquals(3, changes.size());
		
		Collection<String> propertiesChanged = Collections2.transform(changes, new Function<IChange, String>() {
			@Override
			public String apply(IChange change) {
				return change.getPropertyName();
			}			
		});
		
		assert(propertiesChanged.containsAll(Lists.newArrayList("key", "name", "description")));
		
		Collection<IChange> match = Collections2.filter(changes, new Predicate<IChange>() {
			@Override
			public boolean apply(IChange change) {
				return change.getPropertyName().equals("key");
			}		
		});
		
		IChange change = findChangeByPropertyName("key", changes);
		
		assertNotNull(change);
		assertEquals("GET_SNAPSHOT", (String) change.getLeftValue());
		assertEquals("RETRIEVE_SNAPSHOT", (String) change.getRightValue());
		
		change = findChangeByPropertyName("name", changes);
		
		assertNotNull(change);
		assertEquals("should get snapshot", (String) change.getLeftValue());
		assertEquals("retrieve a snapshot", (String) change.getRightValue());
		
	}
	
	private IChange findChangeByPropertyName(final String name, Collection<IChange> changes) {
		
		Collection<IChange> match = Collections2.filter(changes, new Predicate<IChange>() {
			@Override
			public boolean apply(IChange change) {
				return change.getPropertyName().equals(name);
			}		
		});
		
		if (match != null && match.size() > 0) {
			return Lists.newArrayList(match).get(0);
		}
		
		return null;
		
	}

}
