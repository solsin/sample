package test.selenium.webdriver;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IWebdriverScreenshotHelper {
	/**
	 * Take a screenshot and store it in the specified file.
	 */
	boolean createScreenshot(@Nonnull WebDriverConnector webDriverConnector, @Nonnull File screenshotFile) throws Exception;

	/**
	 * Take a screenshot and return it as a BufferedImage, to play with.
	 */
	@Nullable
	BufferedImage createScreenshot(@Nonnull WebDriverConnector webDriverConnector) throws Exception;
}