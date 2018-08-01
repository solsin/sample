package test.selenium.pater;

import java.io.File;

class DummyContext implements IPaterContext {
	@Override
	public void registerResult(String description, String mimetype, File resource) throws Exception {}
}