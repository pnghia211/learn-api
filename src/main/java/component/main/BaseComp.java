package component.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import page.BasePage;

public class BaseComp extends BasePage {
    protected String rootComponentSel = "//*[@id='%s']/following-sibling::*[@class='my-5'][1]";

    public BaseComp(WebDriver driver) {
        super(driver);
    }

    protected WebElement getRootComp(String header) {
        return driver.findElement(By.xpath(String.format(rootComponentSel,header)));
    }

    protected WebElement getComponentBasedOnHeader(String header, String compSel) {
        WebElement rootEle = getRootComp(header);
        actions.moveToElement(rootEle).perform();
        return rootEle.findElement(By.xpath(compSel));
    }
}
