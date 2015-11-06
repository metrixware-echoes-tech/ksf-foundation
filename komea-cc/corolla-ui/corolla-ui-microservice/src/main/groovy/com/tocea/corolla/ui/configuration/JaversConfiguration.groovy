package com.tocea.corolla.ui.configuration

import com.mongodb.Mongo;
import com.mongodb.MongoClient

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.repository.mongo.MongoRepository
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ConfigurationProperties(prefix = "mongo")
class JaversConfiguration {

	def String dbname;
	
	@Autowired
	private Mongo mongo;
	
	@Bean
	public Javers javers() {
		
		MongoClient mongo = (MongoClient) mongo;
		MongoRepository javersRepo = new MongoRepository(mongo.getDatabase(dbname));
		
		return JaversBuilder.javers().registerJaversRepository(javersRepo).build();
		
	}
	
	@Bean
	public AuthorProvider authorProvider() {
		return new AuthorProvider() {
			public String provide() {
				return "unknown";
			}
		}
	}
	
}
