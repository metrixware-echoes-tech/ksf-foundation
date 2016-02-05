package fr.echoes.labs.ksf.database.configuration;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;

/**
 * Configuration of the embedded test database.
 *
 * @author sleroy
 */
@Profile("test")
@Configuration
@EnableMongoRepositories(basePackages = {"fr.echoes.lab", "com.tocea"})
public class FongoConfiguration extends AbstractMongoConfiguration {

	private static Random fongoRandomizer = new Random();
	private String databaseName;
	
	@PostConstruct
	public void init() {
		databaseName=	"corolla-test" + fongoRandomizer.nextLong();
	}
	
	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new Fongo(getDatabaseName()).getMongo();
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

}
