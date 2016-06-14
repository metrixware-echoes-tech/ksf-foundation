package fr.echoes.labs.ksf.cc.plugins.dashboard.utils

import org.komea.connectors.configuration.model.ConnectorProperty
import org.komea.organization.model.Entity

import spock.lang.Specification

import com.google.common.collect.Maps

import fr.echoes.labs.ksf.cc.plugins.dashboard.entities.GitRepository;

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
	
	def "it should extract git repository objects from a connector property"() {
		
		given:
			def values1 = Maps.newHashMap()
			values1.put("remoteURL", "ssh://ksf/testproject1.git")
			values1.put("name", "testProject1")
			
		and:
			def values2 = Maps.newHashMap()
			values2.put("remoteURL", "ssh://ksf/testproject2.git")
			values2.put("name", "testProject2")
			
		and:
			def property = new ConnectorProperty("gitRepositories", [values1, values2])
			
		when:
			def results = DashboardUtils.extractGitRepositories(property)
			
		then:
			results.size() == 2
			
		then:
			results.each { result ->
				assert result.class == GitRepository.class
			}
			
		then:
			results[0].remoteURL == values1.get("remoteURL")
			results[0].name == values1.get("name")
			
		then:
			results[1].remoteURL == values2.get("remoteURL")
			results[1].name == values2.get("name")
					
	}
	
}
