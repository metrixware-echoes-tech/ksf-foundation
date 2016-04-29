package com.tocea.corolla.ui;

import org.junit.After;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tocea.corolla.CorollaGuiApplication;
import com.tocea.corolla.cqrs.gate.spring.api.IAsynchronousTaskPoolService;

@ActiveProfiles({ "test,internalAuth" })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { CorollaGuiApplication.class })
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0"})
public abstract class AbstractSpringTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringTest.class);

	@Autowired
	private MongoTemplate mongoTemplate;


	@Autowired
	private IAsynchronousTaskPoolService taskPoolService;

	@After
	public void cleanDB() {
		waitTask();
		//		for (String collectionName : mongoTemplate.getCollectionNames()) {
		//            if (!collectionName.startsWith("system.")) {
		//                mongoTemplate.getCollection(collectionName).findAndRemove(null);
		//            }
		//        }
		this.mongoTemplate.getDb().dropDatabase();
	}

	protected void waitTask() {
		while(this.taskPoolService.getExecutorService().getActiveCount() > 0  ) {
			try {
				LOGGER.info("Thread active={} pool={}", this.taskPoolService.getExecutorService().getActiveCount(), this.taskPoolService.getExecutorService().getPoolSize());
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
