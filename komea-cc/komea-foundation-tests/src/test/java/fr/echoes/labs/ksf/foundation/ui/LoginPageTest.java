package fr.echoes.labs.ksf.foundation.ui;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.anthavio.phanbedder.Phanbedder;

public class LoginPageTest {
	
	private static final String SERVER_URL = "http://ksf-ads.metrixware.local/komea";
	private static final String FOREMAN_URL = "http://ksf-ads.metrixware.local";
	
	private static final String USERNAME = "echoes";
	private static final String PASSWORD = "echoes";
	
	private WebDriver driver;

	@Before
	public void setUp() {
		
		File phantomjs = Phanbedder.unpack();
        DesiredCapabilities dcaps = new DesiredCapabilities();
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs.getAbsolutePath());  
        dcaps.setJavascriptEnabled(true);
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {"--web-security=no", "--ignore-ssl-errors=yes"});
		driver = new PhantomJSDriver(dcaps);
		
	}
	
	@Test
	@Ignore
	public void testLogin() throws InterruptedException {
		
		driver.get(SERVER_URL+"/login");
		
		WebElement form = driver.findElement(By.id("loginForm"));
		form.findElement(By.xpath("//input[@name='username']")).sendKeys(USERNAME);
		form.findElement(By.xpath("//input[@name='password']")).sendKeys(PASSWORD);
		form.submit();
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("project-list")));
		
		assert(driver.getCurrentUrl().contains("/ui/projects"));
		
		driver.get(FOREMAN_URL);		
		WebElement element = driver.findElement(By.id("account_menu"));
		
		assert(element.getText().contains(USERNAME));
	}
	
	@Test
	@Ignore
	public void testLoginWithWrongCredentials() {
		
		driver.get(SERVER_URL+"/login");
		
		WebElement form = driver.findElement(By.id("loginForm"));
		form.findElement(By.xpath("//input[@name='username']")).sendKeys("wrong");
		form.findElement(By.xpath("//input[@name='password']")).sendKeys("wrong");
		form.submit();
		
		assert(driver.getCurrentUrl().contains("/login?error"));
		
	}
	
	@After
	public void tearDown() {
		
		driver.quit();
	}
}
