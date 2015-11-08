package com.echoeslab.ksf.database.configuration;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuration of the embedded test database.
 *
 * @author sleroy
 */
@Profile("test")
@Configuration
@EnableMongoRepositories(basePackages = {"com.echoeslab", "com.tocea"})
public class FongoConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "corolla-test";
    }

    @Bean
    public Mongo mongo() throws Exception {
        return new Fongo("corolla-test").getMongo();
    }

}
