package component.main.table;

import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HeaderComp extends BaseComp {
    String tableLabel;
    private By headersSel = By.cssSelector("thead tr th");
    private By allSelectionSel = By.cssSelector("tr th [aria-label='Select all']");
    private By toolbarXpath = By.xpath(".//*[@data-slot='root']/preceding-sibling::div");
    private By dropdownButtonSel = By.cssSelector("[id^='reka-dropdown-menu']");
    private String dropdownOptionsXpath = "//*[contains(@id,'reka-dropdown') and @dir='ltr']//*[@data-slot='item' and normalize-space(.)='%s']";

    public HeaderComp(WebDriver driver, String tableLabel) {
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

    public WebElement headerCheckbox(String tableLabel) {
        return tableByLabel(tableLabel).findElement(allSelectionSel);
    }

    public List<WebElement> headerColumns(String tableLabel) {
        return tableByLabel(tableLabel).findElements(headersSel);
    }
}
