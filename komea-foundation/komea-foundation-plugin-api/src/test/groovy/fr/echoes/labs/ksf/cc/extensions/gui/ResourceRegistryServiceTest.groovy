package fr.echoes.labs.ksf.cc.extensions.gui

import org.apache.commons.io.FileUtils

import spock.lang.Specification;;

public class ResourceRegistryServiceTest extends Specification {
	
	def ResourceRegistryService service = new ResourceRegistryService();
	
	def "it should export a resource into the public folder"() {
		
		when:
			this.service.registerResource(this.getClass().getClassLoader(), "assets/test.txt")
		
		then:
			new File(service.EXTERNAL_RESOURCE_FOLDER).exists()
			
		then:
			def copiedFile = new File(service.EXTERNAL_RESOURCE_FOLDER + "test.txt")
			copiedFile.exists()
			FileUtils.readFileToString(copiedFile) == "test resource file"		
	}
	
	def "it should export a resource into a subfolder of the public folder"() {
		
		given:
			def subFolder = "pictures"
		
		when:
			this.service.registerResource(this.getClass().getClassLoader(), subFolder, "assets/test.txt")
		
		then:
			new File(service.EXTERNAL_RESOURCE_FOLDER + subFolder).exists()
			
		then:
			def copiedFile = new File(service.EXTERNAL_RESOURCE_FOLDER + subFolder + "/test.txt")
			copiedFile.exists()
			FileUtils.readFileToString(copiedFile) == "test resource file"
	}
	
	def "it should not fail if the resource file does not exist"() {
		
		when:
			this.service.registerResource(this.getClass().getClassLoader(), "assets/blblbl.txt")
			
		then:
			!new File(service.EXTERNAL_RESOURCE_FOLDER + "blblbl.txt").exists()
		
	}
	
	def "it should export multiple resources at once"() {
		
		given:
			def subFolder = "pictures"
		
		when:
			this.service.registerResources(this.getClass().getClassLoader(), subFolder, ["assets/test.txt", "assets/test2.txt"])
		
		then:
			new File(service.EXTERNAL_RESOURCE_FOLDER + subFolder).exists()
			
		then:
			def copiedFile = new File(service.EXTERNAL_RESOURCE_FOLDER + subFolder + "/test.txt")
			copiedFile.exists()
			FileUtils.readFileToString(copiedFile) == "test resource file"
			
		then:
			def copiedFile2 = new File(service.EXTERNAL_RESOURCE_FOLDER + subFolder + "/test2.txt")
			copiedFile2.exists()
			FileUtils.readFileToString(copiedFile2) == "test2 resource file"
		
	}
	
	def cleanup() {
		
		FileUtils.deleteDirectory(new File(service.EXTERNAL_RESOURCE_FOLDER))
		
	}
}
