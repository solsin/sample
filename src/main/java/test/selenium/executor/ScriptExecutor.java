package test.selenium.executor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScriptExecutor {
//	@Autowired
//	ApplicationContext ctx;
	
	WebDriver driver;
	String url;
	
	public ScriptExecutor(WebDriver driver) {
		this.driver = driver;
	}
	
//	public void execute(String location) throws IOException {
//		Resource resource = ctx.getResource(location);
//		execute(resource.getFile());
//	}
	
	public void execute(File file) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		TestScript testScript = mapper.readValue(file, TestScript.class);
		execute(testScript);
	}
	
	public void execute(TestScript script) {
		log.info("start selenium TestScript: id:{}, name:{}, url:{}", script.id, script.name, script.url);
		
		this.url = script.url;
		for (Test test : script.tests) {
			execute(test);
		}
		
		log.info("finish selenium TestScript: id:{}, name:{}", script.id, script.name);
	}
	
	public void execute(Test test) {
		log.info("start selenium Test: id:{}, name:{}", test.id, test.name);
		
		for(Command command : test.commands) {
			execute(command);
		}
		
		log.info("finish selenium Test: id:{}, name:{}", test.id, test.name);
	}
	
	private void execute(@Nonnull Command command) {
		switch (command.command) {
		case "open" :
			driver.get(url+command.target);
			return;
		case "clickAt" :
			driver.findElement(command.fromTarget()).click();
			return;
		case "type" :
			driver.findElement(command.fromTarget()).sendKeys(command.sendKey());
			return;
		case "assertText" :
			assertEquals(driver.findElement(command.fromTarget()).getText(), command.value);
			return;
		case "verifyText" :
			String target = driver.findElement(command.fromTarget()).getText();
			if (!target.equals(command.value)) {
				log.warn("doesn't match:{}, {}", target, command.value);
			}
			return;
		default :
			log.warn("undefined command:{}", command.command);
			return;				
		}
	}
	
}
