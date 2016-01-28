package fr.echoes.labs.ksf.foundation.tests.ui

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.echoes.labs.ksf.foundation.tests.conf.SeleniumSpecification;
import fr.echoes.labs.ksf.foundation.tests.ui.actions.CreateProjectAction
import fr.echoes.labs.ksf.foundation.tests.ui.actions.LoginAction
import fr.echoes.labs.ksf.foundation.tests.utils.SeleniumUtils;

class ForemanFunctionalTest extends SeleniumSpecification {

	def WebDriverWait wait
	
	def setup() {
		wait = new WebDriverWait(driver, props.DEFAULT_LOOKUP_TIMEOUT)
	}
	
	def "the user should be able to create a project with the Foreman module"() {
		
		given: "the user is authenticated in KSF"
			def username = props.defaultUsername()
			def password = props.defaultPassword()
			executeAction new LoginAction(username, password)
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(props.PROJECT_LIST_SELECTOR)));
		
		when: "the user creates a new project"
			def projectName = "SeleniumForemanProject"
			executeAction new CreateProjectAction(projectName)
		
		then: "he should be redirected to the project page"
			assert driver.getCurrentUrl().contains("/ui/projects/"+projectName)
			
		then: "the project page should contain a Foreman widget"
			assert SeleniumUtils.extractWidgetWithTitle(driver, "Foreman") != null
			
		then: "a new host group should have been created in Foreman"
			driver.get props.foremanUrl()+'/hostgroups'
			def table = driver.findElement(By.tagName("table"))
			def row = SeleniumUtils.extractRowWithText(table, projectName, 0)
			assert row != null
			
		when: "the user deletes the project"
			driver.get props.serverUrl() + "/ui/projects"
			table = driver.findElement(By.className(props.PROJECT_LIST_SELECTOR))
			def projectRow = SeleniumUtils.extractRowWithText(table, projectName, 0)
			def btnDelete = projectRow.findElement(By.className("btn-danger"))
			SeleniumUtils.acceptConfirmBox(driver)
			btnDelete.click()
		
		and: "he goes to the host group page in Foreman"
			driver.get props.foremanUrl()+'/hostgroups'
			table = driver.findElement(By.tagName("table"))
			row = SeleniumUtils.extractRowWithText(table, projectName, 0)
			
		then: "the host group should have been deleted in Foreman"
			assert row == null
		
	}
	
}
