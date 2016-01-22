package fr.echoes.labs.ksf.foundation.tests.conf

import java.util.Properties

import groovy.util.logging.Slf4j;
import spock.lang.Specification;

@Slf4j
abstract class KomeaFoundationSpecification extends Specification {

	protected Properties properties;
	
	protected static String SERVER_URL;
	protected static String FOREMAN_URL;
	protected static String USERNAME;
	protected static String PASSWORD;
	
	def setup() {
		
		// Load properties file
		final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("environment.properties")
		properties = new Properties();
		properties.load(inputStream)
		
		// setting properties
		SERVER_URL = properties.getProperty "komea.foundation.url"
		FOREMAN_URL = properties.getProperty "foreman.url"
		USERNAME = properties.getProperty "komea.foundation.defaultUsername"
		PASSWORD = properties.getProperty "komea.foundation.defaultPassword"
		
		printEnvironmentConfiguration()
		
	}
	
	def printEnvironmentConfiguration() {
		
		log.info "Scanning configuration environment..."
		log.info "Komea Foundation URL :" + SERVER_URL
		log.info "Foreman URL : " + FOREMAN_URL
		log.info "Default username : " + USERNAME
		log.info "Default password : " + PASSWORD
		
	}
	
}
