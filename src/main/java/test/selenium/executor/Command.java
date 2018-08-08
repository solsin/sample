package test.selenium.executor;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import lombok.Data;

@Data
public class Command {
	String id;
	String comment;
	String command;
	String target;
	String value;
	
	public By fromTarget() {
		String[] splits = StringUtils.split(target, '=');
		String key = splits[0];
		By by = null;
		if ("id".equals(key)) {
			by = By.id(splits[1]);
		} else if ("name".equals(key)) {
			by = By.name(splits[1]);
		} else if ("css".equals(key)) {
			by = By.cssSelector(splits[1]);
		}
		
		return by;
	}
	
	public CharSequence sendKey() {
		if (value.charAt(0) != '$') {
			return value;
		}
		return Keys.valueOf(value);
	}
}
