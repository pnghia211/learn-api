package driver;

import org.apache.commons.exec.OS;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.time.Duration;

public class DriverFactory{
    private WebDriver driver;

    public static WebDriver getChromeDriver() {
        String currentProjectLocation = System.getProperty("user.dir");
        String chromeDriverLocation = "";
        if (OS.isFamilyMac()) {
            chromeDriverLocation = currentProjectLocation + "/drivers/chromedriver";
        }

        if (OS.isFamilyWindows()) {
            chromeDriverLocation = currentProjectLocation + "\\src\\main\\resources\\drivers\\chromedriver.exe";
        }

        if (chromeDriverLocation.isEmpty()) {
            throw new IllegalArgumentException("Can't detect OS type");
        }

        System.setProperty("webdriver.chrome.driver", chromeDriverLocation);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        return driver;
    }

    public WebDriver getDriver(String browserName) {
        if (driver == null) {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setPlatform(Platform.ANY);
            BrowserType browserType;

            try {
                browserType = BrowserType.valueOf(browserName);
            } catch (Exception e) {
                throw new IllegalArgumentException(browserName + " is not supported!!!");
            }

            switch (browserType) {
                case chrome -> desiredCapabilities.setBrowserName(BrowserType.chrome.getName());
                case firefox -> desiredCapabilities.setBrowserName(BrowserType.firefox.getName());
                case safari -> desiredCapabilities.setBrowserName(BrowserType.safari.getName());
            }
            try {
                String hub = "http://localhost:4444/wd/hub";
//                String hub = System.getProperty("hub").concat("/wd/hub");
                driver = new RemoteWebDriver(new URI(hub).toURL(), desiredCapabilities);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return driver;
    }
}
