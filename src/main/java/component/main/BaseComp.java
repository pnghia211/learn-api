package component.main;

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
    protected String rootComponentSel = "//*[@id='%s']/following-sibling::*[@class='my-5'][1]";
    private String rowByCellValueXpath = ".//td[normalize-space()='%s']/..";

    public BaseComp(WebDriver driver) {
        super(driver);
    }

    protected WebElement getRootComp(String compLabel) {
        return driver.findElement(By.xpath(String.format(rootComponentSel,compLabel)));
    }

    public WebDriver driver() {
        return this.driver;
    }

    public Actions actions() {
        return this.actions;
    }

    protected WebElement getComponentBasedOnHeader(String compLabel, By compSel) {
        WebElement rootEle = getRootComp(compLabel);
        actions.scrollToElement(rootEle).perform();
        return rootEle.findElement((compSel));
    }

    public WebElement tableByLabel(String tableLabel) {
        return getComponentBasedOnHeader(tableLabel, tableSel);
    }

    public WebElement calendarByLabel(String calendarLabel) {
        return getComponentBasedOnHeader(calendarLabel, calendarXpath);
    }

    public WebElement datePickByLabel(String calendarLabel) {
        return getComponentBasedOnHeader(calendarLabel, datePickerXpath);
    }

    public List<WebElement> rowsByCellText(String tableLabel, String cell) {
        return tableByLabel(tableLabel).findElements(By.xpath(String.format(rowByCellValueXpath, cell)));
    }
}
