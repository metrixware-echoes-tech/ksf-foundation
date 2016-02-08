package fr.echoes.labs.ksf.foundation.tests.conf

import net.anthavio.phanbedder.Phanbedder

import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.DesiredCapabilities

import fr.echoes.labs.ksf.foundation.tests.SeleniumTests
import fr.echoes.labs.ksf.foundation.tests.ui.actions.AbstractSeleniumAction;
import groovy.util.logging.Slf4j;
import spock.lang.Shared
import spock.lang.Specification


@org.junit.experimental.categories.Category(SeleniumTests.class)
public abstract class SeleniumSpecification extends KomeaFoundationSpecification {

	@Shared
	protected WebDriver driver;
	
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
		
	}
	
	def cleanup() {
		driver.quit()
	}
	
	def navigateTo(String page) {		
		driver.get props.serverUrl() + page
	}
	
	def executeAction(AbstractSeleniumAction action) {
		
		action.setWebDriver driver
		action.setProperties props
		action.execute()
		
	}
	
}
