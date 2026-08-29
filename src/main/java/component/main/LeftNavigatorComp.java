package component.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import page.BasePage;

public class LeftNavigatorComp extends BasePage {
    private String leftNavigatorCompCss = "aside[data-slot='left'] a[href='/docs/components/%s']";
    private String compTitle = "//div[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]";

    public LeftNavigatorComp(WebDriver driver) {
        super(driver);
    }

    public void clickDataTableComp(String option) {
        WebElement element = driver.findElement(By.cssSelector(String.format(leftNavigatorCompCss, option)));
        actions.moveToElement(element).perform();
        element.click();
        wait.until(ExpectedConditions.urlContains("/" + option));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(compTitle,option.replaceAll("-","").toLowerCase()))));
    }
}
