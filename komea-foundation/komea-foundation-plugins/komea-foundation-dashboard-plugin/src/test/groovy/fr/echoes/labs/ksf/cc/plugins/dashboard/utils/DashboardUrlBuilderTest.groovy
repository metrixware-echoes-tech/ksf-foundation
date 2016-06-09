package fr.echoes.labs.ksf.cc.plugins.dashboard.utils

import spock.lang.Specification

class DashboardUrlBuilderTest extends Specification {

	def "it should build a complete url of a dashboard page"() {
		
		given:
			def baseUrl = "https://ksf-demo.metrixware.local"
			def projectKey = "komea"
			
		when:
			def url = new DashboardUrlBuilder()
				.setBaseUrl(baseUrl)
				.setProjectKey(projectKey)
				.build();	
				
		then:
			url == baseUrl+"/web/"+projectKey

	}
	
}
