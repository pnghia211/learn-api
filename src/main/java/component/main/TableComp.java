package component.main;

import actions.TableActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class TableComp extends BaseComp {
    private DropdownComp columnsDropdown;
    private String tableSel = ".//*[@data-slot='root'][./table]";
    private By rowSel = By.cssSelector("tbody tr");
    private String rowsByCellTxt = ".//td[text()='%s']/..";
    private String cellsByColumnIndex = ".//tr/td[%s]";
    private By headersSel = By.cssSelector("thead tr th");

    public TableComp(WebDriver driver) {
        super(driver);
    }

    public DropdownComp dropdownComp() {
        if (columnsDropdown == null) {
            columnsDropdown = new DropdownComp(driver, tableSel);
        }
        return columnsDropdown;
    }

    public Actions actions() {
        return this.actions;
    }

    public WebDriver driver() {
        return this.driver;
    }

    public WebElement getTableBasedOnHeader(String tableLabel) {
        return getComponentBasedOnHeader(tableLabel, tableSel);
    }

    public List<WebElement> rowsByCellText(String tableLabel, String cell) {
        return getTableBasedOnHeader(tableLabel).findElements(By.xpath(String.format(rowsByCellTxt, cell)));
    }

    public List<WebElement> tableRows(String tableLabel) {
        return getTableBasedOnHeader(tableLabel).findElements(rowSel);
    }

    public List<WebElement> headerColumns(String tableLabel) {
        return getTableBasedOnHeader(tableLabel).findElements(headersSel);
    }

    public List<WebElement> cellsByColumnIndex(String tableLabel, int headerIndex) {
        return getTableBasedOnHeader(tableLabel).findElements(By.xpath(String.format(cellsByColumnIndex, headerIndex)));
    }

    public TableActions forTable(String tableLabel) {
        return new TableActions(this, tableLabel);
    }
}
