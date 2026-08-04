package component.main;

import actions.TableActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class TableComp extends BaseComp {
    private HeaderDropdownComp columnsDropdown;
    private RowDropdownComp rowDropdown;
    protected String tableSel = ".//*[@data-slot='root'][./table]";
    private By rowSel = By.cssSelector("tbody tr");
    private String rowByCellValue = ".//td[text()='%s']/..";
    private String cellsByColumnIndex = ".//tr/td[%s]";
    private By headersSel = By.cssSelector("thead tr th");

    public TableComp(WebDriver driver) {
        super(driver);
    }

    public HeaderDropdownComp headerDropdownComp() {
        if (columnsDropdown == null) {
            columnsDropdown = new HeaderDropdownComp(driver, tableSel);
        }
        return columnsDropdown;
    }

    public RowDropdownComp rowDropdownComp() {
        if (rowDropdown == null) {
            rowDropdown = new RowDropdownComp(driver, tableSel);
        }
        return rowDropdown;
    }

    public Actions actions() {
        return this.actions;
    }

    public WebDriver driver() {
        return this.driver;
    }

    public WebElement tableByTableLabel(String tableLabel) {
        return getComponentBasedOnHeader(tableLabel, tableSel);
    }

    public List<WebElement> rowByCellText(String tableLabel, String cell) {
        return tableByTableLabel(tableLabel).findElements(By.xpath(String.format(rowByCellValue, cell)));
    }

    public List<WebElement> tableRows(String tableLabel) {
        return tableByTableLabel(tableLabel).findElements(rowSel);
    }

    public List<WebElement> headerColumns(String tableLabel) {
        return tableByTableLabel(tableLabel).findElements(headersSel);
    }

    public List<WebElement> cellsByColumnIndex(String tableLabel, int headerIndex) {
        return tableByTableLabel(tableLabel).findElements(By.xpath(String.format(cellsByColumnIndex, headerIndex)));
    }

    public TableActions forTable(String tableLabel) {
        return new TableActions(this, tableLabel);
    }
}
