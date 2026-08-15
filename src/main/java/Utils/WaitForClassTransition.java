package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class WaitForTableLoading implements ExpectedCondition<Boolean> {
    private final WebElement rootEle;

    public WaitForTableLoading(WebElement rootEle) {
        this.rootEle = rootEle;
    }

    @Override
    public Boolean apply(WebDriver driver) {
        String classAttr = rootEle.findElement(By.cssSelector("tr[data-slot='separator']")).getAttribute("class");
        return !classAttr.contains("animate-[carousel");
    }
}
