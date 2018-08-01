package test.selenium.webdriver;

public class ElementNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ElementNotFoundException(String id) {
		super("The element " + id + " is not found");
	}
}