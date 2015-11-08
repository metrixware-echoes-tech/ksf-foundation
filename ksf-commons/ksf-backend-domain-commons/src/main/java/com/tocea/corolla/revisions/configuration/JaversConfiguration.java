package com.tocea.corolla.revisions.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.repository.mongo.MongoRepository;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@EnableAspectJAutoProxy
@ConfigurationProperties(prefix = "mongo")
public class JaversConfiguration {

	private String dbname;
	

	
	@Bean
	public Javers javers(Mongo mongo) {
		
		MongoClient mongoc = (MongoClient) mongo;
		MongoRepository javersRepo = new MongoRepository(mongoc.getDatabase(dbname));
		
		return JaversBuilder.javers().registerJaversRepository(javersRepo).build();
		
	}
	
	@Bean
	public AuthorProvider authorProvider() {
		return new AuthorProvider() {
			public String provide() {
				return "unknown";
			}
		};
	}

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    
	
}
