package fr.echoes.labs.ksf.foundation.tests.utils

import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver

class SeleniumUtils {

	public static void acceptConfirmBox(WebDriver driver) {
		
		if (driver instanceof PhantomJSDriver) {
			
			PhantomJSDriver phantom = (PhantomJSDriver) driver;
			phantom.executeScript("window.alert = function(){}");
			phantom.executeScript("window.confirm = function(){return true;}");
			
		} else {
			
			driver.switchTo().alert().accept();
		}
	}
	
}
