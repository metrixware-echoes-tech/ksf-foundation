package fr.echoes.labs.ksf.cc.plugins.dashboard.utils

import org.komea.organization.model.Entity

import spock.lang.Specification

class DashboardUtilsTest extends Specification {

	def "it should split entities by type"() {
		
		given:
			def entities = [
				new Entity().setKey("E1").setType("T1"),
				new Entity().setKey("E2").setType("T2"),
				new Entity().setKey("E3").setType("T1")	
			]
		
		when:
			def result = DashboardUtils.splitEntitiesByType(entities)
			
		then:
			result.size() == 2
			result.containsKey("T1")
			result.containsKey("T2")
			
		then:
			result.get("T1").size() == 2
			result.get("T1").containsAll(["E1", "E3"])
			
		then:
			result.get("T2").size() == 1
			result.get("T2").containsAll(["E2"])
			
	}
	
}
