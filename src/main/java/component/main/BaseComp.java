package component.main;

import data.TableIndexOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import page.BasePage;

import java.util.List;

public class BaseComp extends BasePage {
    protected By tableSel = By.xpath(".//*[@data-slot='root'][./table]");
    private By calendarXpath = By.xpath(".//*[@data-slot='root']");
    private By datePickerXpath = By.xpath(".//button[./*[contains(@class,'calendar')]]");
    protected String rootComponentSel = "//*[@id='%s']/following-sibling::*[@class='my-5'][%s]";

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
        return driver.findElement(By.xpath(String.format(rootComponentSel, compLabel, index)));
    }

    protected WebElement getRootComp(String compLabel) {
        return getRootComp(compLabel, TableIndexOption.PRIMARY.label());
    }

    protected WebElement getComponentBasedOnHeader(String compLabel, By compSel, int index) {
        WebElement rootEle = getRootComp(compLabel, index);
        actions.scrollToElement(rootEle).perform();
        return rootEle.findElement(compSel);
    }

    protected WebElement getComponentBasedOnHeader(String compLabel, By compSel) {
        return getComponentBasedOnHeader(compLabel, compSel, TableIndexOption.PRIMARY.label());
    }

    public WebElement calendarByLabel(String calendarLabel) {
        return getComponentBasedOnHeader(calendarLabel, calendarXpath);
    }

    public WebElement datePickByLabel(String calendarLabel) {
        return getComponentBasedOnHeader(calendarLabel, datePickerXpath);
    }
}
