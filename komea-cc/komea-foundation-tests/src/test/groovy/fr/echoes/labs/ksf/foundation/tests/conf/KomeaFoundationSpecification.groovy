package fr.echoes.labs.ksf.foundation.tests.conf

import java.util.Properties

import com.pholser.util.properties.PropertyBinder
import groovy.util.logging.Slf4j;

import spock.lang.Specification;

@Slf4j
abstract class KomeaFoundationSpecification extends Specification {

	protected TestProperties properties;
	protected TestProperties props;
	
	def setup() {
		
		// Load properties file
		URL url = this.getClass().getClassLoader().getResource("environment.properties")
		PropertyBinder<TestProperties> binder = PropertyBinder.forType(TestProperties.class);
		properties = binder.bind(new File(url.getPath()));
		
		printEnvironmentConfiguration()
		
		props = properties
		
	}
	
	def printEnvironmentConfiguration() {
		
		log.info "Scanning configuration environment..."
		log.info "Komea Foundation URL :" + properties.serverUrl()
		log.info "Foreman URL : " + properties.foremanUrl()
		log.info "Default username : " + properties.defaultUsername()
		log.info "Default password : " + properties.defaultPassword()
		
	}
	
}
