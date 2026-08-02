package component.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DropdownComp extends BaseComp {
    String tableSel;
    private String toolbarSel = ".//*[@data-slot='root']/preceding-sibling::div";
    private By dropdownButtonSel = By.cssSelector("[id^='reka-dropdown-menu']");
    private String dropdownOptionsSel = "//*[contains(@id,'reka-dropdown') and @dir='ltr']//*[@data-slot='item' and normalize-space(.)='%s']";

    public DropdownComp(WebDriver driver, String tableSel) {
        super(driver);
        this.tableSel = tableSel;
    }

    public WebElement getTableBasedOnHeader(String tableLabel) {
        return getComponentBasedOnHeader(tableLabel, tableSel);
    }

    public WebElement dropdownButton(String tableLabel) {
        return getComponentBasedOnHeader(tableLabel, toolbarSel).findElement(dropdownButtonSel);
    }

    public WebElement dropdownOptions(String tableLabel, String option) {
        return getTableBasedOnHeader(tableLabel)
                .findElement(By.xpath(String.format(dropdownOptionsSel, option)));
    }
}
