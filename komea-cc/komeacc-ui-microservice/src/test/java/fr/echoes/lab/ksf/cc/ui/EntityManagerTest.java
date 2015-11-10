package fr.echoes.lab.ksf.cc.ui;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.tocea.corolla.users.dao.IUserDAO;

public class EntityManagerTest extends AbstractSpringTest {
	

	@Autowired
	private MongoTemplate manager;

	@Test
	public void test() {
		assertNotNull(manager != null);
		final MongoRepositoryFactory mongoRepositoryFactory = new MongoRepositoryFactory(manager);
		final IUserDAO repository = mongoRepositoryFactory.getRepository(IUserDAO.class);
		assertNotNull(repository);
	}
}
