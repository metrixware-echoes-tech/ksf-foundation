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

	def "the user should be able to manage projects"() {
		
		given: "the users has successfully logged in"
			def username = props.defaultUsername()
			def password = props.defaultPassword()
			executeAction new LoginAction(username, password)
			WebDriverWait wait = new WebDriverWait(driver, SeleniumSpecification.DEFAULT_LOOKUP_TIMEOUT);
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
