package test.selenium.webdriver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;

public class ChromeScreenshotHelper implements IWebdriverScreenshotHelper {
	@Override public boolean createScreenshot(WebDriverConnector webDriverConnector, File screenshotFile) throws Exception {
		new ChromeExtender((MyChromeDriver) webDriverConnector.driver()).takeScreenshot(screenshotFile);
		return true;
	}

	@Nullable @Override public BufferedImage createScreenshot(@Nonnull WebDriverConnector webDriverConnector) throws Exception {
		return new ChromeExtender((MyChromeDriver) webDriverConnector.driver()).takeScreenshot();
	}
}