package fr.echoes.labs.ksf.foundation.tests.ui.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement
import groovy.util.logging.Slf4j;

@Slf4j
public class CreateProjectAction extends AbstractSeleniumAction {

	private String projectName
	
	public CreateProjectAction(String projectName) {
		this.projectName = projectName
	}
	
	@Override
	public void execute() {
		
		driver.get props.serverUrl() + "/ui/projects/new"
		log.info driver.getPageSource()
		
		WebElement form = driver.findElement(By.id("formCreateProject"))
		form.findElement(By.xpath("//input[@name='name']")).sendKeys(projectName)
		form.submit()
		
	}

}
