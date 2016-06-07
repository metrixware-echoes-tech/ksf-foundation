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
			url.startsWith(baseUrl + displayPage)
			url.contains('#')
			
		then:
			def params = url.substring(url.indexOf('#')+1)
			params != null
			
		then:
			params.contains("list_entities=project_komea;")
			params.contains("list_metrics=;")
			params.contains("custom_period=LAST_X_DAYS,1433282400000,1464818400000,365")
			params.contains("boolean_open=true;");
			params.contains("boolean_in_menu_bar=true;")
	
	}
	
}
