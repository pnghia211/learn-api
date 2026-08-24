package component.main.table;

import component.main.BaseComp;
import data.DropdownOption;
import data.HeaderColumnOption;
import data.SortingOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class HeaderComp extends BaseComp {
    TableComp tableComp;
    private By headerCellsSel = By.cssSelector("thead tr th");
    private By allSelectionSel = By.cssSelector("tr th [aria-label='Select all']");
    private By dropdownButtonSel = By.cssSelector("button[id^='reka-dropdown-menu']");
    private String dropdownOptionsXpath = "//*[contains(@id,'reka-dropdown') and @dir='ltr']//*[@data-slot='item' and normalize-space(.)='%s']";
    private String sortingHeaderSel = ".//thead/tr/th/button[normalize-space()='%s']";

    public HeaderComp(TableComp tableComp) {
        super(tableComp.driver());
        this.tableComp = tableComp;
    }

    public WebElement getTableComp() {
        return tableComp.tableByLabel();
    }
    public WebElement headerDropdownButton() {
        return getTableComp().findElement(dropdownButtonSel);
    }

    public WebElement btnDropdownOptions(DropdownOption option) {
        return getTableComp().findElement(By.xpath(String.format(dropdownOptionsXpath, option.label())));
    }

    public WebElement headerDropdownOptions(SortingOption option) {
        return getTableComp().findElement(By.xpath(String.format(dropdownOptionsXpath, option.label())));
    }

    public WebElement sortingHeader(HeaderColumnOption option) {
        List<WebElement> headers = getTableComp().findElements(headerCellsSel);

        String extracted = headers.stream()
                .map(h -> h.getText().trim())
                .filter(option::matchesHeader)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No header found matching " + option + " (aliases: " + option.getHeaderAliases() + ")"));

        return getTableComp().findElement(By.xpath(String.format(sortingHeaderSel, extracted)));
    }

    public WebElement headerCheckbox() {
        return getTableComp().findElement(allSelectionSel);
    }

    public List<WebElement> headerColumns() {
        return tableComp.tableByLabel().findElements(headerCellsSel);
    }
}
