package fr.echoes.labs.foremanclient
import spock.lang.Specification


class ModulesTest extends Specification {

	def "Modules.puppetClasses should be empty"() {
		given:
			Modules modules = new Modules()
		expect:
			modules.modules.size() == 0
	}

	def "Should create and return a module"() {
		given:
			Modules modules = new Modules()
		when:
			Module module = modules.getOrCreateModule("moduleName")
		then:
			modules.modules.size() == 1
			modules.modules.get("moduleName") == module
	}

	def "Several calls to the getOrCreateModule with the same module name should always return the same module"() {
		given:
			Modules modules = new Modules()
		when:
			Module module = modules.getOrCreateModule("moduleName")
			modules.getOrCreateModule("moduleName")
		then:
			modules.modules.size() == 1
			modules.modules.get("moduleName") == module
	}

}
