gradle.allprojects {
	/** DEPENDENCIES */
	dependencies {

		compile "org.slf4j:slf4j-api:$LOG_SLF4J"
		compile "org.slf4j:jcl-over-slf4j:$LOG_SLF4J"
		compile "org.slf4j:log4j-over-slf4j:$LOG_SLF4J"

		compile "com.google.code.findbugs:jsr305:$FINDBUGS"
		compile "com.google.guava:guava:$GUAVA"
		compile "org.codehaus.groovy:groovy-all:${GROOVY_VERSION}"
		compile	"org.springframework.data:spring-data-mongodb:$SPRING_MONGO"

		compile "org.javers:javers-spring:$JAVERS"
		compile "org.javers:javers-persistence-mongo:$JAVERS"

		compile "org.mongodb:mongo-java-driver:$MONGO_DRIVER"
		compile "com.github.fakemongo:fongo:$FONGO"
		testCompile "com.lordofthejars:nosqlunit-mongodb:$TEST_NOSQLUNIT"

		testCompile LOGGING

		testCompile tests_frameworks // TEST FRAMEWORKS

		testCompile("org.easymock:easymock:${TEST_EASYMOCK}") { exclude group: 'org.objenesis' }
		testRuntime "org.apache.logging.log4j:log4j-core:${LOG4J}"

		testCompile("org.spockframework:spock-core:$TEST_SPOCK") { exclude group: 'junit' }
		testCompile("org.spockframework:spock-spring:$TEST_SPOCK") { exclude group: 'junit' }
		testCompile("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")

		testCompile "org.springframework.security:spring-security-test:$SPRING_SECU"

		testCompile "com.jayway.jsonpath:json-path:$TEST_JAYWAY_JSONPATH"
		testCompile "com.jayway.jsonpath:json-path-assert:$TEST_JAYWAY_JSONPATH"

	}


}