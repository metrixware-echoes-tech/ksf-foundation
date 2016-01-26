package fr.echoes.labs.foremanclient
import spock.lang.Specification
import fr.echoes.lab.foremanclient.Module
import fr.echoes.lab.foremanclient.PuppetClass;


class ModuleTest extends Specification {

	def "Module.puppetClasses should be empty"() {
		given:
			Module module = new Module()
		expect:
			module.puppetClasses.size() == 0
	}

	def "Should create and return a PuppetClass"() {
		given:
			Module module = new Module()
		when:
			PuppetClass puppetClass = module.getOrCreatePuppetClass("puppetClassName")
		then:
			module.puppetClasses.size() == 1
			module.puppetClasses.get("puppetClassName") == puppetClass
	}

	def "Several calls to the getOrCreatePuppetClass with the same puppetClass name should always return the same PuppetClass"() {
		given:
			Module module = new Module()
		when:
			PuppetClass puppetClass = module.getOrCreatePuppetClass("puppetClassName")
			puppetClass.getOrCreateParameter("puppetClassName")
		then:
			module.puppetClasses.size() == 1
			module.puppetClasses.get("puppetClassName") == puppetClass
	}
	
}
