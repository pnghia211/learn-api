package component.main;

import data.ComponentIndexOption;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import page.BasePage;

import java.util.function.Supplier;

public class BaseComp extends BasePage {
    protected String rootComponentXpath = "//*[@id='%s']/following-sibling::*[@class='my-5'][%s]/*[@class='relative group/component']";

    public BaseComp(WebDriver driver) {
        super(driver);
    }

    public WebDriver driver() {
        return this.driver;
    }

    public Actions actions() {
        return this.actions;
    }

    protected WebElement getRootComp(String compLabel, int index) {
        WebElement rootEle = driver.findElement(By.xpath(String.format(rootComponentXpath, compLabel, index)));
        actions.scrollToElement(rootEle).perform();
        return rootEle;
    }

    protected WebElement getRootComp(String compLabel) {
        return getRootComp(compLabel, ComponentIndexOption.PRIMARY.label());
    }

    protected WebElement getOrRefreshCached(WebElement cached, Supplier<WebElement> resolver) {
        if (cached == null) {
            return resolver.get();
        }
        try {
            cached.isDisplayed();
            return cached;
        } catch (StaleElementReferenceException e) {
            return resolver.get();
        }
    }
}
