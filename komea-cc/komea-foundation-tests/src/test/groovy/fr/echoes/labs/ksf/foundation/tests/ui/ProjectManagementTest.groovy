package fr.echoes.labs.ksf.foundation.tests.ui

import fr.echoes.labs.ksf.foundation.tests.conf.SeleniumSpecification;
import fr.echoes.labs.ksf.foundation.tests.ui.actions.ClickOnProjectDeleteButton
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
		wait = new WebDriverWait(driver, props.DEFAULT_LOOKUP_TIMEOUT)
	}
	
	def "the user should be able to manage projects"() {
		
		given: "the user has successfully logged in"
			def username = props.defaultUsername()
			def password = props.defaultPassword()
			executeAction new LoginAction(username, password)
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(props.PROJECT_LIST_SELECTOR)));
	
		when: "he should be able to create a project"
			def projectName = "SeleniumProject"
			executeAction new CreateProjectAction(projectName)
			
		then: "he should be redirected to the project page"
			assert driver.getCurrentUrl().contains("/ui/projects/"+projectName)
			
		when: "he displays the project list"
			navigateTo "/ui/projects"
			def table = driver.findElement(By.className(props.PROJECT_LIST_SELECTOR))
			def projectRow = SeleniumUtils.extractRowWithText(table, projectName, 0)
		
		then: "the new project should appear in the project list"		
			assert projectRow != null
			
		when: "he clicks on the delete button"
			executeAction new ClickOnProjectDeleteButton(projectName)
			table = driver.findElement(By.className(props.PROJECT_LIST_SELECTOR))
			projectRow = SeleniumUtils.extractRowWithText(table, projectName, 0)
			
		then: "the project should have been deleted"
			assert projectRow == null
					
	}
	
	def "the user should not be able to create a project that already exists"() {
		
		given: "the user has successfully logged in"
			def username = props.defaultUsername()
			def password = props.defaultPassword()
			executeAction new LoginAction(username, password)
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(props.PROJECT_LIST_SELECTOR)))
			
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
			navigateTo "/ui/projects"
			executeAction new ClickOnProjectDeleteButton(projectName)
	
	}
	
}
