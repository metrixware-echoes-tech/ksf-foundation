package com.tocea.corolla

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableMongoRepositories
@EnableMongoAuditing
//@EnableWebMvc
class CorollaGuiApplication {
	
	public static void main(final String[] args) {
		SpringApplication.run(CorollaGuiApplication.class, args)
	}
}
