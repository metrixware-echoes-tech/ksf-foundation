/**
 Specific parent plugin
 */
apply plugin: "sonar-runner"

sonarRunner {
	// toolVersion = "2.4"

	// Fine grained control over the runner process
	//    forkOptions {
	//        maxHeapSize = '1024m'
	//    }
	sonarProperties {
		property "sonar.host.url", sonarURL
		property "sonar.jdbc.url", sonarJDBC
		property "sonar.jdbc.driverClassName", "com.mysql.jdbc.Driver"
		property "sonar.jdbc.username", sonarUser
		property "sonar.jdbc.password", sonarPassword
		property "sonar.branch", getGitBranch()
	}
}