package fr.echoes.labs.ksf.foundation.tests.smoke

import fr.echoes.labs.ksf.foundation.tests.SmokeTests;
import fr.echoes.labs.ksf.foundation.tests.conf.KomeaFoundationSpecification
import fr.echoes.labs.ksf.foundation.tests.conf.SSLUtilities
import fr.echoes.labs.ksf.foundation.tests.conf.SmokeTestSpecification;
import groovy.util.logging.Slf4j;
import spock.lang.Specification;

@Slf4j
class FoundationSmokeTest extends SmokeTestSpecification {

	def "Komea Foundation and all its modules should be working"() {
		
		given: "the health API of the deployed Komea Foundation server"
			def healthAPI = SERVER_URL+"/health"
			log.info "executing smoke test on {}", healthAPI	
			
		and: "this client can handle any SSL certificate"
			SSLUtilities.trustAllHostnames()
			SSLUtilities.trustAllHttpsCertificates()
			
		when: "sending a request to the health API"
			def url = new URL(healthAPI)
			def connection = url.openConnection()
			connection.requestMethod = "GET"
			connection.connect()
			
		then: "it should have successfully established a connection with the server"
			notThrown ConnectException.class
		
		then: "it should return a correct response code"			
			assert connection.responseCode in [200, 201, 302]
			log.info connection.content.text
			
	}
	
}
