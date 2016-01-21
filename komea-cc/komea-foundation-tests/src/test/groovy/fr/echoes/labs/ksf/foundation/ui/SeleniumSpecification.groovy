package fr.echoes.labs.ksf.foundation.ui

import net.anthavio.phanbedder.Phanbedder

import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.DesiredCapabilities

import fr.echoes.labs.ksf.foundation.tests.SeleniumTests;
import groovy.util.logging.Slf4j;
import spock.lang.Shared
import spock.lang.Specification

@Slf4j
@org.junit.experimental.categories.Category(SeleniumTests.class)
public abstract class SeleniumSpecification extends Specification {

	@Shared
	protected WebDriver driver;
	
	protected Properties properties;
	
	protected static String SERVER_URL;
	protected static String FOREMAN_URL;	
	protected static String USERNAME;
	protected static String PASSWORD;
	
	def setup() {
		
		// PhantomJS Driver Configuration
		File phantomjs = Phanbedder.unpack();
		DesiredCapabilities dcaps = new DesiredCapabilities();
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs.getAbsolutePath());
		dcaps.setJavascriptEnabled(true);
		dcaps.setCapability(
			PhantomJSDriverService.PHANTOMJS_CLI_ARGS, 
			["--web-security=no", "--ignore-ssl-errors=yes"]
		)
		driver = new PhantomJSDriver(dcaps);
		
		// Load properties file
		final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("environment.properties")
		properties = new Properties();
		properties.load(inputStream)
		
		// setting properties
		SERVER_URL = properties.getProperty "komea.foundation.url"
		FOREMAN_URL = properties.getProperty "foreman.url"
		USERNAME = properties.getProperty "komea.foundation.defaultUsername"
		PASSWORD = properties.getProperty "komea.foundation.defaultPassword"
		
		printEnvironmentConfiguration()
		
	}
	
	def cleanup() {
		driver.quit()
	}
	
	def printEnvironmentConfiguration() {
		
		log.info "Scanning configuration environment..."
		log.info "Komea Foundation URL :" + SERVER_URL
		log.info "Foreman URL : " + FOREMAN_URL
		log.info "Default username : " + USERNAME
		log.info "Default password : " + PASSWORD
		
	}
}
