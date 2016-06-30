package fr.echoes.labs.ksf.cc.plugin.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class JsonPluginRepositoryTest {

	private JsonPluginRepository<Pojo> jsonPluginRepository;

	@Before
	public void before() throws IOException {
		final File tempFile = File.createTempFile("dao", ".json");
		tempFile.delete();
		tempFile.deleteOnExit();
		this.jsonPluginRepository = new JsonPluginRepository<>("plugin", Pojo.class, tempFile);

	}

	@Test
	public void testCount() throws Exception {

		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		assertThat(this.jsonPluginRepository.count(), equalTo(4L));

	}

	@Test
	public void testDeleteAll() throws Exception {

		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		assertThat(this.jsonPluginRepository.count(), not(equalTo(0L)));
		this.jsonPluginRepository.deleteAll();
		assertThat(this.jsonPluginRepository.count(), equalTo(0L));
	}

	@Test
	public void testDeleteInteger() throws Exception {
		final Pojo entity = new Pojo();
		this.jsonPluginRepository.save(entity);
		assertNotNull("Entity should have an ID", entity.getId());
		this.jsonPluginRepository.delete(entity.getId());
		assertNull("Should be deleted", this.jsonPluginRepository.findOne(entity.getId()));
	}

	@Test
	public void testDeleteT() throws Exception {
		final Pojo entity = new Pojo();
		this.jsonPluginRepository.save(entity);
		assertNotNull("Entity should have an ID", entity.getId());
		this.jsonPluginRepository.delete(entity);
		assertNull("Should be deleted", this.jsonPluginRepository.findOne(entity.getId()));
	}

	@Test
	public void testExists() throws Exception {
		final Pojo entity = new Pojo();
		this.jsonPluginRepository.save(entity);
		assertTrue("Should exists", this.jsonPluginRepository.exists(entity.getId()));
		assertNotNull("Entity should have an ID", entity.getId());
		this.jsonPluginRepository.delete(entity.getId());
		assertNull("Should be deleted", this.jsonPluginRepository.findOne(entity.getId()));
		assertFalse("Should not exists", this.jsonPluginRepository.exists(entity.getId()));
	}

	@Test
	public void testFindAll() throws Exception {
		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		this.jsonPluginRepository.save(new Pojo());
		assertThat(this.jsonPluginRepository.count(), not(equalTo(0L)));
		assertThat(Iterators.size(this.jsonPluginRepository.findAll().iterator()), equalTo(4));
	}

	@Test
	public void testFindAllEntity() throws Exception {
		final ArrayList<Pojo> entities = Lists.newArrayList(new Pojo(), new Pojo(), new Pojo(), new Pojo());
		this.jsonPluginRepository.save(entities);
		assertThat(this.jsonPluginRepository.count(), not(equalTo(0L)));
		assertThat(Iterators.size(this.jsonPluginRepository.findAll().iterator()), equalTo(4));
		this.jsonPluginRepository.delete(entities);
		assertThat(this.jsonPluginRepository.count(), equalTo(0L));
	}

	@Test
	public void testFindOne() throws Exception {
		final Pojo pojo = this.jsonPluginRepository.save(new Pojo());
		assertNotNull(pojo);
		assertEquals(pojo, this.jsonPluginRepository.findOne(pojo.getId()));

	}

	@Test
	public void testSaveS() throws Exception {
		final Pojo entity = new Pojo();
		final Pojo pojo = this.jsonPluginRepository.save(entity);
		assertEquals(entity, pojo);
		final Pojo pojo2 = this.jsonPluginRepository.save(pojo);
		assertEquals(pojo, pojo2);

	}

}
