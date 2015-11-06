package com.tocea.corolla.ui.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.stereotype.Component

import com.google.common.collect.Lists
import com.mongodb.Mongo
import com.mongodb.MongoClient
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress


@Profile("default")
@Component
@ConfigurationProperties(prefix = "mongo")
class DatabaseConfiguration extends AbstractMongoConfiguration  {
	
	def String host
	def String port
	def String dbname
	def String login
	def String password
	
	public String getDatabaseName() {
		return dbname
	}
	
	@Bean
	public Mongo mongo() throws Exception {
		
		def serverAdress = new ServerAddress(host, Integer.parseInt(port))
		
		if (login == null || login == "") {
			
			return new MongoClient(serverAdress)
			
		}else{
			
			return new MongoClient(
					Lists.newArrayList(serverAdress),
					Lists.newArrayList(MongoCredential.createCredential(login, dbname, password))
					)
			
		}
		
	}
	
}
