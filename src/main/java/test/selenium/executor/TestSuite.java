package test.selenium.executor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestSuite {
	String id;
	String name;
	int timeout;
}
