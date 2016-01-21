package fr.echoes.labs.ksf.foundation.ui

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class LoginGroovyPageTest extends SeleniumSpecification {

	private static final long DEFAULT_LOOKUP_TIMEOUT = 30;
	
	def "the login form should authenticate the user in KSF and Foreman"() {

		when: "the user submit the login form with valid credentials"
			driver.get SERVER_URL+"/login"
			WebElement form = driver.findElement(By.id("loginForm"));
			form.findElement(By.xpath("//input[@name='username']")).sendKeys(USERNAME);
			form.findElement(By.xpath("//input[@name='password']")).sendKeys(PASSWORD);
			form.submit();
		
		then: "he should be redirected to the project list view"
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_LOOKUP_TIMEOUT);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("project-list")));
			assert driver.getCurrentUrl().contains("/ui/projects");
						
		then: "he should also be connected in Foreman"
			driver.get FOREMAN_URL			
			WebElement element = driver.findElement(By.id("account_menu"));	
			assert element.getAttribute("innerText").contains(USERNAME);
			
	}
	
	def "invalid login should fail"() {
		
		when: "the user submit invalid credentials in the login form"
			driver.get SERVER_URL+'/login'		
			WebElement form = driver.findElement(By.id("loginForm"));
			form.findElement(By.xpath("//input[@name='username']")).sendKeys("wrong");
			form.findElement(By.xpath("//input[@name='password']")).sendKeys("wrong");
			form.submit();
		
		then: "he should stay on the login page"
			assert driver.getCurrentUrl().contains("/login")
		
	}
	
}
