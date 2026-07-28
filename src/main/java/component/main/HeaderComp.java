package component.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import page.BasePage;

public class HeaderComp extends BasePage {
    private By componentsHeaderSel = By.cssSelector("ul[data-slot='list'] a[href='/docs/components']");

    public HeaderComp(WebDriver driver) {
        super(driver);
    }

    public void clickComponentsComp() {
        driver.findElement(componentsHeaderSel).click();
    }
}
