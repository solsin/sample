package test.selenium.executor;

import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;
import sample.config.TestConfig;
import test.selenium.webdriver2.WebDriverPool;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
class ScriptExecutorTest {
	@Autowired
	ApplicationContext ctx;

	WebDriver driver;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	
	@BeforeAll
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "D:/Develop/workspace/sample/webdriver/chrome/chromedriver-win32-2.41.exe");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		WebDriverPool.DEFAULT.dismissAll();
	}

	@BeforeEach
	void setUp() throws Exception {
		driver = WebDriverPool.DEFAULT.getDriver(DesiredCapabilities.chrome());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testExecuteFile() throws JsonParseException, JsonMappingException, IOException {
		ScriptExecutor executor = new ScriptExecutor(driver);
		Resource resource = ctx.getResource("classpath:selenium/script/sample.side");
		executor.execute(resource.getFile());
	}

}
