package fr.echoes.labs.ksf.foundation.tests.ui

import fr.echoes.labs.ksf.foundation.tests.conf.SeleniumSpecification;
import fr.echoes.labs.ksf.foundation.tests.ui.actions.CreateProjectAction;
import fr.echoes.labs.ksf.foundation.tests.ui.actions.LoginAction
import fr.echoes.labs.ksf.foundation.tests.utils.SeleniumUtils

import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.Alert;
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions

import spock.lang.Specification

class ProjectManagementTest extends SeleniumSpecification {

	def WebDriverWait wait
	
	def setup() {
		wait = new WebDriverWait(driver, SeleniumSpecification.DEFAULT_LOOKUP_TIMEOUT)
	}
	
	def "the user should be able to manage projects"() {
		
		given: "the user has successfully logged in"
			def username = props.defaultUsername()
			def password = props.defaultPassword()
			executeAction new LoginAction(username, password)
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("project-list")));
	
		when: "he should be able to create a project"
			def projectName = "SeleniumProject"
			executeAction new CreateProjectAction(projectName)
			
		then: "he should be redirected to the project page"
			assert driver.getCurrentUrl().contains("/ui/projects/"+projectName)
			
		when: "he displays the project list"
			driver.get props.serverUrl() + "/ui/projects"
		
		then: "the new project should appear in the project list"
			def projectRow = retrieveProjectRow(projectName)
			assert projectRow != null
			
		when: "he clicks on the delete button"
			def btnDelete = projectRow.findElement(By.className("btn-danger"))
			SeleniumUtils.acceptConfirmBox(driver)
			btnDelete.click()
			
		then: "the project should have been deleted"
			assert retrieveProjectRow(projectName) == null
					
	}
	
	def "the user should not be able to create a project that already exists"() {
		
		given: "the user has successfully logged in"
			def username = props.defaultUsername()
			def password = props.defaultPassword()
			executeAction new LoginAction(username, password)
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("project-list")))
			
		and: "a project has already been created"
			def projectName = "SeleniumProject"
			executeAction new CreateProjectAction(projectName)
			assert driver.getCurrentUrl().contains("/ui/projects/"+projectName)
			
		when: "the user create a project with the same name that the one that already exists"
			executeAction new CreateProjectAction(projectName)
		
		then: "the user should be warned that the project already exists"
			def selector = By.className("text-danger")
			wait.until(ExpectedConditions.visibilityOfElementLocated(selector))			
			assert driver.getCurrentUrl().contains("/ui/projects/new")
			WebElement element = driver.findElement(selector)
			assert element.getAttribute("innerText").contains("project already exists");
			
		cleanup: "delete project"
			driver.get props.serverUrl() + "/ui/projects"
			def projectRow = retrieveProjectRow(projectName)
			def btnDelete = projectRow.findElement(By.className("btn-danger"))
			SeleniumUtils.acceptConfirmBox(driver)
			btnDelete.click()
	
	}
	
	private WebElement retrieveProjectRow(String projectName) {
		
		WebElement table = driver.findElement(By.className("project-list"))
		
		List<WebElement> rows = table.findElement(By.tagName("tbody")).findElements(By.tagName('tr'))
		
		for(WebElement row : rows) {
			WebElement cell = row.findElements(By.tagName('td')).get(0)
			if (cell.getAttribute("innerText").contains(projectName)) {
				return row
			}
		}
		
		return null
	}
	
}
