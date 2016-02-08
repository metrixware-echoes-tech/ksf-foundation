package fr.echoes.labs.ksf.foundation.tests.ui.actions

import org.openqa.selenium.By

import fr.echoes.labs.ksf.foundation.tests.utils.SeleniumUtils;;

class ClickOnProjectDeleteButton extends AbstractSeleniumAction {

	private String projectName;
	
	public ClickOnProjectDeleteButton(String projectName) {
		this.projectName = projectName
	}
	
	public void execute() {
		
		def table = driver.findElement(By.className(props.PROJECT_LIST_SELECTOR))
		def projectRow = SeleniumUtils.extractRowWithText(table, projectName, 0)
		def btnDelete = projectRow.findElement(By.className("btn-danger"))
		SeleniumUtils.acceptConfirmBox(driver)
		btnDelete.click()
		
	}
	
}
