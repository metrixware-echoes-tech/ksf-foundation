package fr.echoes.labs.ksf.foundation.tests.ui

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions

import fr.echoes.labs.ksf.foundation.tests.conf.SeleniumSpecification;
import fr.echoes.labs.ksf.foundation.tests.ui.actions.LoginAction

class LoginPageTest extends SeleniumSpecification {
	
	def "the login form should authenticate the user in KSF and Foreman"() {

		given: "valid credentials"
			def username = props.defaultUsername()
			def password = props.defaultPassword()
		
		when: "the user submit the login form with valid credentials"
			executeAction new LoginAction(username, password)
			WebDriverWait wait = new WebDriverWait(driver, SeleniumSpecification.DEFAULT_LOOKUP_TIMEOUT);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("project-list")));
		
		then: "he should be redirected to the project list view"
			assert driver.getCurrentUrl().contains("/ui/projects");
						
		then: "he should also be connected in Foreman"
			driver.get props.foremanUrl()			
			WebElement element = driver.findElement(By.id("account_menu"));	
			assert element.getAttribute("innerText").contains(username);
			
	}
	
	def "invalid login should fail"() {
		
		when: "the user submit invalid credentials in the login form"
			executeAction new LoginAction("wrong", "wrong")
		
		then: "he should stay on the login page"
			assert driver.getCurrentUrl().contains("/login")
		
	}
	
}
