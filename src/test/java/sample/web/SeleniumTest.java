package sample.web;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;
import sample.config.TestConfig;
import test.selenium.webdriver2.WebDriverPool;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
public class SeleniumTest {
	WebDriver driver;
	
	@BeforeAll
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "D:/Develop/workspace/sample/webdriver/chrome/chromedriver-win32-2.41.exe");
	}
	
	@BeforeEach
	public void startBrowser() {
//		driver = WebDriverPool.DEFAULT.getDriver(DesiredCapabilities.firefox());
		driver = WebDriverPool.DEFAULT.getDriver(DesiredCapabilities.chrome());
	}
	
	@AfterAll
	public void stopAllBrowsers() {
		WebDriverPool.DEFAULT.dismissAll();
	}

	@Test
	public void test1() {
		doSomething();
	}

	@Test
	public void test2() {
		doSomething();
	}

	@Test
	public void test3() {
		doSomething();
	}

	private void doSomething() {
		log.info("start: selenium test");
		
		driver.get("http://seleniumhq.org/");
		driver.findElement(By.name("q")).sendKeys("selenium");
		driver.findElement(By.id("submit")).click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.titleContains("Google Custom Search"));
		
		log.info("finish: selenium test");
	}
}
