package helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JsExecutorHelper {
    private final WebDriver driver;

    public JsExecutorHelper(WebDriver driver) {
        this.driver = driver;
    }

    public static void scrollIntoViewCentered(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'instant'});", element);
    }
}
