package test.selenium.executor;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestScript {
	String id;
	String name;
	String url;
	Collection<String> urls;
	Collection<Test> tests;
	Collection<TestSuite> suites;
}
