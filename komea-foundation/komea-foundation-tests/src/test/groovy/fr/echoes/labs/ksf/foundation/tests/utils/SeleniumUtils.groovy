package fr.echoes.labs.ksf.foundation.tests.utils

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement;
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
	
	public static WebElement extractRowWithText(WebElement table, String searchText, int column) {
		
		List<WebElement> rows = table.findElement(By.tagName("tbody")).findElements(By.tagName('tr'))
		
		for(WebElement row : rows) {
			WebElement cell = row.findElements(By.tagName('td')).get(column)
			if (cell.getAttribute("innerText").contains(searchText)) {
				return row
			}
		}
		
		return null
		
	}
	
	public static WebElement extractWidgetWithTitle(WebDriver driver, String searchTitle) {
		
		def widgets = driver.findElements(By.className("project-widget-panel"))
		for (WebElement widget : widgets) {
			def title = widget.findElement(By.className("panel-title"))
			if (title.getAttribute("innerText").contains(searchTitle)) {
				return widget
			}
		}
		
		return null
	}
	
}
