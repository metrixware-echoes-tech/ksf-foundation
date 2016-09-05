package fr.echoes.labs.ksf.foundation.tests.ui

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.echoes.labs.ksf.foundation.tests.conf.SeleniumSpecification;
import fr.echoes.labs.ksf.foundation.tests.ui.actions.ClickOnProjectDeleteButton
import fr.echoes.labs.ksf.foundation.tests.ui.actions.CreateProjectAction
import fr.echoes.labs.ksf.foundation.tests.ui.actions.LoginAction
import fr.echoes.labs.ksf.foundation.tests.utils.SeleniumUtils
import groovy.util.logging.Slf4j;

import spock.lang.Ignore;

// TODO : update the test for the CAS authentification
@Ignore
@Slf4j
class ForemanFunctionalTest extends SeleniumSpecification {

	private static final String FOREMAN_WIDGET_TITLE = "Foreman"
	
	private static final String FOREMAN_IFRAME_SELECTOR = "//iframe[@id='foremanFrame']"
	
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
			assert SeleniumUtils.extractWidgetWithTitle(driver, FOREMAN_WIDGET_TITLE) != null
		
		then: "the project page should contain a Foreman iframe"
			def iframe = driver.findElement(By.xpath(FOREMAN_IFRAME_SELECTOR))
			assert iframe.getAttribute("src").contains(props.foremanUrl())
			
		then: "a new host group should have been created in Foreman"
			driver.get props.foremanUrl()+'/hostgroups'
			def table = driver.findElement(By.tagName("table"))
			def row = SeleniumUtils.extractRowWithText(table, projectName, 0)
			assert row != null
			
		when: "the user deletes the project"
			navigateTo "/ui/projects"
			executeAction new ClickOnProjectDeleteButton(projectName)
		
		and: "he goes to the host group page in Foreman"
			driver.get props.foremanUrl()+'/hostgroups'
			table = driver.findElement(By.tagName("table"))
			row = SeleniumUtils.extractRowWithText(table, projectName, 0)
			
		then: "the host group should have been deleted in Foreman"
			assert row == null
		
	}
	
	def "the user should be able to create a host with the Foreman module"() {
		
		given: "the user is authenticated in KSF"
			def username = props.defaultUsername()
			def password = props.defaultPassword()
			executeAction new LoginAction(username, password)
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(props.PROJECT_LIST_SELECTOR)))
		
		and: "a project has been created and the user have been redirected to the project page"
			def projectName = "SeleniumForemanProject2"
			executeAction new CreateProjectAction(projectName)
			
		and: "a puppet environment configuration"
			def envrironmentName = "SeleniumEnvironment1"
			def puppetFileName = "puppet_environment_sample.json"
			def puppetConfiguration = new File(this.getClass().getClassLoader().getResource(puppetFileName).toURI()).text
		
		and: "a target name"
			def targetName = "SeleniumTarget1"
			
		and: "a host configuration"
			def hostName = "SeleniumHost1"
			def hostPassword = "selenium"
			
		when: "the user clicks on the button to create an environment"
			def widget = SeleniumUtils.extractWidgetWithTitle(driver, FOREMAN_WIDGET_TITLE)
			widget.findElement(By.xpath("//a[contains(text(), 'Actions')]")).click()
			widget.findElement(By.xpath("//a[contains(text(), 'Create Environment')]")).click()
			
		then: "the environment form should appear"
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("createEnvModal")))
			
		when: "the user submit the envrionment form"		
			def form = driver.findElement(By.id("new_env"))
			form.findElement(By.id("envName")).sendKeys(envrironmentName)
			def textarea = form.findElement(By.id("env_configuration"))
			textarea.clear()
			textarea.sendKeys(puppetConfiguration)
			form.submit()
		
		then: "the puppet environment should be created"
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("createEndModal")))
			
		when: "the user clicks on the button to create a target"
			widget = SeleniumUtils.extractWidgetWithTitle(driver, FOREMAN_WIDGET_TITLE)
			widget.findElement(By.xpath("//a[contains(text(), 'Actions')]")).click()
			widget.findElement(By.xpath("//a[contains(text(), 'Create Target')]")).click()
			
		then: "the target form should appear"
			wait.until ExpectedConditions.visibilityOfElementLocated(By.id("createTargetModal"))
			
		when: "the user submit the target form"
			form = driver.findElement(By.id("new_target"))
			form.findElement(By.name("name")).sendKeys(targetName)
			new Select(form.findElement(By.name("environment"))).selectByVisibleText(envrironmentName)
			form.submit()
			wait.until ExpectedConditions.invisibilityOfElementLocated(By.id("createTargetModal"))
			widget = SeleniumUtils.extractWidgetWithTitle(driver, FOREMAN_WIDGET_TITLE)
			def table = widget.findElement(By.tagName("table"))
			def rowTarget = SeleniumUtils.extractRowWithText(table, targetName, 0)
			
		then: "the target should be created"
			assert rowTarget != null
		
		when: "the user clicks on the button to instantiate a target"
			rowTarget.findElement(By.className("btn-instantiate")).click()
			
		then: "the instanciation form should appear"
			wait.until ExpectedConditions.visibilityOfElementLocated(By.id("instantiateHostModal"))
			
		when: "the user submit the form to instantiate a target"
			form = driver.findElement(By.id("instantiate_host"))
			form.findElement(By.id("hostName")).sendKeys(hostName)
			form.findElement(By.id("hostPass")).sendKeys(hostPassword)
			form.submit()
			
		then: "the host should have been created"
			def iframe = driver.findElement(By.xpath(FOREMAN_IFRAME_SELECTOR))
			assert iframe.getAttribute("src").contains("/hosts")
		
		cleanup: "delete the project"
			navigateTo "/ui/projects"
			executeAction new ClickOnProjectDeleteButton(projectName)
		
	}
	
}
