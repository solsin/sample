package test.selenium.executor;

import java.util.Collection;

import lombok.Data;

@Data
public class Test {
	String id;
	String name;
	Collection<Command> commands;
}
