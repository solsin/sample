/*
 * Copyright 2013 Alexei Barantsev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.selenium.webdriver2;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class LooseWebDriverPool extends AbstractWebDriverPool {

	private List<WebDriver> drivers = new ArrayList<>();

	public LooseWebDriverPool() {
		Runtime.getRuntime().addShutdownHook(new Thread(LooseWebDriverPool.this::dismissAll));
	}

	@Override
	public WebDriver getDriver(URL hub, Capabilities capabilities) {
		WebDriver driver = newDriver(hub, capabilities);
		drivers.add(driver);
		return driver;
	}

	@Override
	public void dismissDriver(WebDriver driver) {
		if (!drivers.contains(driver)) {
			throw new Error("The driver is not owned by the factory: " + driver);
		}
		driver.quit();
		drivers.remove(driver);
	}

	@Override
	public void dismissAll() {
		for (WebDriver driver : new ArrayList<>(drivers)) {
			driver.quit();
			drivers.remove(driver);
		}
	}

	@Override
	public boolean isEmpty() {
		return drivers.isEmpty();
	}

}