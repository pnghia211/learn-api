package component.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HeaderDropdownComp extends TableComp {
    String tableSel;
    private String toolbarSel = ".//*[@data-slot='root']/preceding-sibling::div";
    private By dropdownButtonSel = By.cssSelector("[id^='reka-dropdown-menu']");
    private String dropdownOptionsSel = "//*[contains(@id,'reka-dropdown') and @dir='ltr']//*[@data-slot='item' and normalize-space(.)='%s']";

    public HeaderDropdownComp(WebDriver driver, String tableSel) {
        super(driver);
        this.tableSel = tableSel;
    }

    public WebElement headerDropdownButton(String tableLabel) {
        return getComponentBasedOnHeader(tableLabel, toolbarSel).findElement(dropdownButtonSel);
    }

    public WebElement headerDropdownOptions(String tableLabel, String option) {
        return tableByTableLabel(tableLabel)
                .findElement(By.xpath(String.format(dropdownOptionsSel, option)));
    }
}
