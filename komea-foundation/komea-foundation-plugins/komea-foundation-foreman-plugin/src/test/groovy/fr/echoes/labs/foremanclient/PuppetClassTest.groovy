package fr.echoes.labs.foremanclient
import spock.lang.Specification


class PuppetClassTest extends Specification {

	def "ParameterClass.parameters should be empty"() {
		given:
			PuppetClass puppetClass = new PuppetClass()
		expect:
			puppetClass.parameters.size() == 0
	}

	def "Should create and return a Parameter"() {
		given:
			PuppetClass puppetClass = new PuppetClass()
		when:
			Parameter parameter = puppetClass.getOrCreateParameter("parameterName")
		then:
			puppetClass.parameters.size() == 1
			puppetClass.parameters.get("parameterName") == parameter
	}

	def "Several calls to the getOrCreateParameter with the same parameter name should always return the same Parameter"() {
		given:
			PuppetClass puppetClass = new PuppetClass()
		when:
			Parameter parameter = puppetClass.getOrCreateParameter("parameterName")
			puppetClass.getOrCreateParameter("parameterName")
		then:
			puppetClass.parameters.size() == 1
			puppetClass.parameters.get("parameterName") == parameter
	}

}
