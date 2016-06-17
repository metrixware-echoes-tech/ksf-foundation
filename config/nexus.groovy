gradle.allprojects {

	/**
	 * Nexus Rule Management
	 *
	 */

	ext.nexusURL = "http://zeus.tocea.local:8081"

	if (project.hasProperty('remote')) {
		ext.nexusURL = "http://labs.echoes.fr:90"
		println ("WARNING : Utilisation de Nexus en remote $nexusURL")
	} else {
		println ("WARNING : Utilisation de Nexus en local (Echoes/Lab) $nexusURL")
	}



	if (project.version.endsWith('-SNAPSHOT')) {
		ext.isRelease = false
	}else{
		ext.isRelease = true
	}


	/** REPOSITORIES */
	repositories {

		mavenLocal()
		if (project.hasProperty('remote')) {
			mavenCentral()
			jcenter()
		}

		maven {
			credentials {
				username mavenUser
				password mavenPassword
			}
			// Snapshots EchoesLabs
			url nexusURL + "/nexus/content/groups/echoes-lab-snapshot"

		}
		maven {
			credentials {
				username mavenUser
				password mavenPassword
			}
			// Release EchoesLabs (local)
			url nexusURL + "/nexus/content/groups/echoes-lab-release"

		}

		/* maven { url "http://dl.bintray.com/sleroy/maven" } */
	}


	/** UPLOAD ARCHIVES */
	uploadArchives {
		def publishURL = nexusURL+"/nexus/content/repositories/ksf"
		if (!isRelease) {
			publishURL += "-snapshots"
		}
		repositories {
			mavenDeployer {
				repository(url: publishURL) {
					authentication(userName: mavenUser, password: mavenPassword)
				}
			}
		}
	}
}