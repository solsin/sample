package test.selenium.pater;

import java.io.File;

public interface IPaterContext {
	void registerResult(String description, String mimeType, File resource) throws Exception;
}
