package component.main.table;

import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HeaderDropdownComp extends BaseComp {
    String tableLabel;
    private By toolbarXpath = By.xpath(".//*[@data-slot='root']/preceding-sibling::div");
    private By dropdownButtonSel = By.cssSelector("[id^='reka-dropdown-menu']");
    private String dropdownOptionsXpath = "//*[contains(@id,'reka-dropdown') and @dir='ltr']//*[@data-slot='item' and normalize-space(.)='%s']";

    public HeaderDropdownComp(WebDriver driver, String tableLabel) {
        super(driver);
        this.tableLabel = tableLabel;
    }

    public WebElement headerDropdownButton(String tableLabel) {
        return getComponentBasedOnHeader(tableLabel, toolbarXpath).findElement(dropdownButtonSel);
    }

    public WebElement headerDropdownOptions(String tableLabel, String option) {
        return tableByLabel(tableLabel)
                .findElement(By.xpath(String.format(dropdownOptionsXpath, option)));
    }
}
