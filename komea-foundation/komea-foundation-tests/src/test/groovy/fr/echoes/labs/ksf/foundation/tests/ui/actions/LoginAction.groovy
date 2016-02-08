package fr.echoes.labs.ksf.foundation.tests.ui.actions

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait

import fr.echoes.labs.ksf.foundation.tests.conf.SeleniumSpecification

class LoginAction extends AbstractSeleniumAction {

	private String username;
	private String password;
	
	public LoginAction(String username, String password) {
		this.username = username
		this.password = password
	}
	
	@Override
	public void execute() {
		
		// Go to the login page
		driver.get props.serverUrl() + "/login"
		
		// Submit credentials in the login form
		WebElement form = driver.findElement(By.id("loginForm"))
		form.findElement(By.xpath("//input[@name='username']")).sendKeys(username)
		form.findElement(By.xpath("//input[@name='password']")).sendKeys(password)
		form.submit()
	
	}

}
