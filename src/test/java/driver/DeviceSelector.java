package driver;

import com.google.gson.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DeviceSelector {

  public static final String BROWSERSTACK_USER = "browserstack.user";
  public static final String BROWSERSTACK_KEY = "browserstack.key";

  public static DesiredCapabilities getRandomDeviceCapabilities(String jsonFilePath, String type) throws Exception {
    JsonObject root = JsonParser.parseReader(new FileReader(jsonFilePath)).getAsJsonObject();
    JsonArray devices = root.getAsJsonObject("DriverSettings").getAsJsonArray(type);

    JsonObject device = devices.get(new Random().nextInt(devices.size())).getAsJsonObject();
    DesiredCapabilities capabilities = new DesiredCapabilities();
    Map<String, Object> browserstackOptions = new HashMap<>();

    // Add BrowserStack options
    if (device.has("RemoteOptionsCapabilities")) {
      device.getAsJsonObject("RemoteOptionsCapabilities").entrySet()
          .forEach(entry -> browserstackOptions.put(entry.getKey(), entry.getValue().getAsString()));
    }

    // Add BrowserStack credentials
    JsonObject driverSettings = root.getAsJsonObject("DriverSettings");
    capabilities.setCapability(BROWSERSTACK_USER, driverSettings.get(BROWSERSTACK_USER).getAsString());
    capabilities.setCapability(BROWSERSTACK_KEY, driverSettings.get(BROWSERSTACK_KEY).getAsString());

    // Add device-specific capabilities
    if (device.has("Capabilities")) {
      device.getAsJsonObject("Capabilities").entrySet()
          .forEach(entry -> capabilities.setCapability(entry.getKey(), entry.getValue().getAsString()));
    }

    capabilities.setCapability("bstack:options", browserstackOptions);
    return capabilities;
  }
}
