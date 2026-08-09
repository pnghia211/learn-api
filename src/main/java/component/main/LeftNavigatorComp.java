package component.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import page.BasePage;

public class LeftNavigatorComp extends BasePage {
    private String leftNavigatorCompXpath = "aside[data-slot='left'] a[href='/docs/components/%s']";

    public LeftNavigatorComp(WebDriver driver) {
        super(driver);
    }

    public void clickDataTableComp(String option) {
        WebElement element = driver.findElement(By.cssSelector(String.format(leftNavigatorCompXpath, option)));
        actions.moveToElement(element).perform();
        element.click();
        wait.until(ExpectedConditions.urlContains("/" + option));
    }
}
