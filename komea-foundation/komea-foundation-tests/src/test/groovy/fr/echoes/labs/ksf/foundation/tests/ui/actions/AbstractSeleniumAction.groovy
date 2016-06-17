package fr.echoes.labs.ksf.foundation.tests.ui.actions

import fr.echoes.labs.ksf.foundation.tests.conf.TestProperties
import org.openqa.selenium.WebDriver;

public abstract class AbstractSeleniumAction {

	protected WebDriver driver;
	
	protected TestProperties props;
	
	public setWebDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	public setProperties(TestProperties props) {
		this.props = props;
	}
	
	public abstract void execute();
	
}
