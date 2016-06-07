package fr.echoes.labs.ksf.cc.plugins.dashboard.utils

import spock.lang.Specification;

class DashboardUrlBuilderTest extends Specification {

	def "it should build a complete url of a dashboard page"() {
		
		given:
			def baseUrl = "https://ksf-demo.metrixware.local"
			def displayPage = "/web/guest/performance-de-la-r-d"
			def projectType = "project"
			def projectKey = "komea"
			def customPeriod = "LAST_X_DAYS,1433282400000,1464818400000,365"
			
		when:
			def url = new DashboardUrlBuilder()
				.setBaseUrl(baseUrl)
				.setDisplayPage(displayPage)
				.setProjectType(projectType)
				.setProjectKey(projectKey)
				.setCustomPeriod(customPeriod)
				.build();
				
		then:
			url == baseUrl + displayPage + "#list_entities=project_komea;custom_period=LAST_X_DAYS,1433282400000,1464818400000,365"

	}
	
}
