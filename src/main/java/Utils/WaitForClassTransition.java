package Utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class WaitForClassTransition implements ExpectedCondition<Boolean> {
    private final WebElement element;
    private final JavascriptExecutor js;
    private boolean armed = false;

    private static final String ARM_SCRIPT =
            "const el = arguments[0];" +
                    "el.__baselineClass = el.className;" +
                    "el.__transitionDetected = false;" +
                    "if (el.__classObserver) el.__classObserver.disconnect();" +
                    "el.__classObserver = new MutationObserver(() => {" +
                    "  if (el.className !== el.__baselineClass) { el.__transitionDetected = true; }" +
                    "});" +
                    "el.__classObserver.observe(el, { attributes: true, attributeFilter: ['class'] });";

    private static final String CHECK_SCRIPT = "return arguments[0].__transitionDetected === true;";

    public WaitForClassTransition(WebElement element, WebDriver driver) {
        this.element = element;
        this.js = (JavascriptExecutor) driver;
    }

    /** Call BEFORE the triggering action (e.g. scroll) — captures baseline class and starts observing. */
    public void arm() {
        js.executeScript(ARM_SCRIPT, element);
        armed = true;
    }

    @Override
    public Boolean apply(WebDriver driver) {
        if (!armed) {
            throw new IllegalStateException("Call arm() before the triggering action — observer must be watching first.");
        }
        return Boolean.TRUE.equals(js.executeScript(CHECK_SCRIPT, element));
    }

    @Override
    public String toString() {
        return "class transitioned away from baseline on element: " + element;
    }
}
