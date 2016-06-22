package com.tocea.corolla.revisions.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.commands.CreateProjectStatusCommand;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.products.dao.IProjectDAO;
import com.tocea.corolla.products.domain.Project;
import com.tocea.corolla.products.domain.ProjectStatus;
import com.tocea.corolla.revisions.domain.IChange;
import com.tocea.corolla.revisions.domain.ICommit;
import com.tocea.corolla.revisions.exceptions.InvalidCommitInformationException;
import com.tocea.corolla.ui.AbstractSpringTest;

public class RevisionServiceTest extends AbstractSpringTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private IProjectBranchDAO branchDAO;

    @Autowired
    private IRevisionService revisionService;

    @Autowired
    private Gate gate;

    private ProjectStatus projectStatus;

    @Before
    public void setUp() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();

        projectStatus = new ProjectStatus();
        projectStatus.setName("Active");
        projectStatus.setClosed(false);

        gate.dispatch(new CreateProjectStatusCommand(projectStatus));

    }

    @Test
    public void shouldCreateRevisionAndRetrieveIt() {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());

        projectDAO.save(project);

        revisionService.commit(project);

        Collection<ICommit> commits = revisionService.getHistory(project.getId(), Project.class);

        assertEquals(1, commits.size());

    }

    @Test
    public void shouldRetrieveCommitByItsID() {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());

        projectDAO.save(project);
        revisionService.commit(project);

        Collection<ICommit> commits = revisionService.getHistory(project.getId(), Project.class);

        String commitID = commits.iterator().next().getId();

        ICommit commit = revisionService.findCommitByID(project.getId(), Project.class, commitID);

        assertEquals(commitID, commit.getId());

    }

    @Test
    public void shouldReturnNullWhenTryingToRetrieveCommitByItsIDWithAnInvalidCommitID() {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());

        projectDAO.save(project);
        revisionService.commit(project);

        String commitID = "fgfgf";

        ICommit commit = revisionService.findCommitByID(project.getId(), Project.class, commitID);

        assertNull(commit);

    }

    @Test
    public void shouldRetrievePreviousCommit() {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());

        projectDAO.save(project);
        revisionService.commit(project);

        project.setName("blblbl");
        projectDAO.save(project);
        revisionService.commit(project);

        Collection<ICommit> commits = revisionService.getHistory(project.getId(), Project.class);

        Iterator<ICommit> it = commits.iterator();
        String commitID = it.next().getId();
        ICommit firstCommit = it.next();

        ICommit commit = revisionService.getPreviousCommit(project.getId(), Project.class, commitID);

        assertNotNull(commit);
        assertEquals(firstCommit.getId(), commit.getId());

    }

    @Test
    public void shouldNotFailWhenRetrievePreviousCommitOnTheFirstCommit() {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());

        projectDAO.save(project);
        revisionService.commit(project);

        Collection<ICommit> commits = revisionService.getHistory(project.getId(), Project.class);

        String commitID = commits.iterator().next().getId();

        ICommit commit = revisionService.getPreviousCommit(project.getId(), Project.class, commitID);

        assertNull(commit);

    }

    @Test
    public void shouldCompareVersionsOfSameObject() throws Exception {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());

        projectDAO.save(project);
        revisionService.commit(project);

        Project previousState = new Project();
        previousState.setId(project.getId());
        previousState.setKey(project.getKey());
        previousState.setName("OldName");
        previousState.setStatusId(projectStatus.getId());

        projectDAO.save(project);
        revisionService.commit(project);

        List<IChange> changes = revisionService.compare(previousState, project);

        assertNotNull(changes);
        assertEquals(1, changes.size());
        assertEquals(previousState.getName(), changes.iterator().next().getLeftValue());
        assertEquals(project.getName(), changes.iterator().next().getRightValue());

    }

    @Test
    public void shouldCompareVersionsOfSameObjectWithListChanges() throws Exception {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());
        project.setTags(Lists.newArrayList("test1", "test2"));

        projectDAO.save(project);
        revisionService.commit(project);

        Project previousState = new Project();
        previousState.setId(project.getId());
        previousState.setKey(project.getKey());
        previousState.setName(project.getName());
        previousState.setStatusId(projectStatus.getId());
        previousState.setTags(Lists.newArrayList("test1"));

        projectDAO.save(project);
        revisionService.commit(project);

        List<IChange> changes = revisionService.compare(previousState, project);

        assertNotNull(changes);
        assertEquals(1, changes.size());
        final IChange change = changes.iterator().next();
        assertEquals(previousState.getTags(), change.getLeftValue());
        assertEquals(project.getTags(), change.getRightValue());

    }

    @Test
    public void shouldRetrieveSnapshot() {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());

        projectDAO.save(project);
        revisionService.commit(project);

        project.setName("NewName");

        projectDAO.save(project);
        revisionService.commit(project);

        Collection<ICommit> commits = revisionService.getHistory(project.getId(), Project.class);
        Iterator<ICommit> it = commits.iterator();
        it.next();
        String commitID = it.next().getId();

        Project snapshot = (Project) revisionService.getSnapshot(project.getId(), Project.class, commitID);
        System.out.println("" + project.toString());
        assertNotNull(snapshot);
        assertEquals("Corolla", snapshot.getName());

    }

    @Test(expected = InvalidCommitInformationException.class)
    public void shouldThrowExceptionWhenTryingToRetrieveSnapshotWithInvalidCommitID() {

        Project project = new Project();
        project.setKey("COROLLA_TEST");
        project.setName("Corolla");
        project.setStatusId(projectStatus.getId());

        projectDAO.save(project);
        revisionService.commit(project);

        String commitID = "blblb";

        revisionService.getSnapshot(project.getId(), Project.class, commitID);

    }

}
