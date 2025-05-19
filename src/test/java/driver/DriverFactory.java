package driver;

import java.net.URL;
import java.util.HashMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

  // Get a new WebDriver Instance.
  // There are various implementations for this depending on browser. The required browser can be set as an environment variable.
  // Refer http://getgauge.io/documentation/user/current/managing_environments/README.html
  public static WebDriver getDriver() {
    try {
      String deviceType = System.getenv("DEVICE_TYPE");
      if (deviceType == null) {
        deviceType = "desktop"; // default
      }

      // Path to your Devices.json
      String jsonFilePath = "env/default/Devices.json";
      DesiredCapabilities capabilities = DeviceSelector.getRandomDeviceCapabilities(jsonFilePath,
          deviceType);

      // Get BrowserStack hub URL from env or use default
      String hubUrl = System.getenv("BROWSERSTACK_HUB_URL");
      if (hubUrl == null) {
        hubUrl = "https://hub-cloud.browserstack.com/wd/hub";
      }

      return new RemoteWebDriver(new URL(hubUrl), capabilities);
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize WebDriver", e);
    }
  }

}
