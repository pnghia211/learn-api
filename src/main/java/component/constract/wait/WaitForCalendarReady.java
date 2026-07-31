package component.constract.wait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;

public class WaitForCalendarReady implements ExpectedCondition<WebElement> {
    private final By calendarSel;

    public WaitForCalendarReady(By calendarSel) {
        this.calendarSel = calendarSel;
    }

    @Override
    public WebElement apply(WebDriver driver) {
        WebElement calendar = driver.findElement(calendarSel);
        List<WebElement> elements = calendar.findElements(By.cssSelector("[data-slot='label']"));
        return !elements.isEmpty() && elements.get(0).isDisplayed() ? calendar : null;
    }
}
