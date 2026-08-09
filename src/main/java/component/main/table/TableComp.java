package component.main.table;

import actions.TableActions;
import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

public class TableComp extends BaseComp {
    private By rowSel = By.cssSelector("tbody tr");
    private By headersSel = By.cssSelector("thead tr th");
    private By allSelectionSel = By.cssSelector("tr th [aria-label='Select all']");
    private By rowSelectionSel = By.cssSelector("tr td [aria-label='Select row']");
    private By checkedRowsSel = By.cssSelector("tr td [aria-label='Select row'][data-state='checked']");
    private String cellsByColumnIndexXpath = ".//tbody/tr/td[%s]";

    public TableComp(WebDriver driver) {
        super(driver);
    }

    public HeaderDropdownComp headerDropdownComp(String tableLabel) {
        return new HeaderDropdownComp(driver, tableLabel);
    }

    public RowDropdownComp rowDropdownComp(String tableLabel) {
        return new RowDropdownComp(driver, tableLabel);
    }

    public FooterComp footerComp(String tableLabel) {
        return new FooterComp(driver, tableLabel);
    }

    public Actions actions() {
        return this.actions;
    }

    public WebDriver driver() {
        return this.driver;
    }

    public List<WebElement> tableRows(String tableLabel) {
        return tableByLabel(tableLabel).findElements(rowSel);
    }

    public List<WebElement> headerColumns(String tableLabel) {
        return tableByLabel(tableLabel).findElements(headersSel);
    }

    public List<WebElement> cellsByColumnIndex(String tableLabel, int headerIndex) {
        return tableByLabel(tableLabel).findElements(By.xpath(String.format(cellsByColumnIndexXpath, headerIndex)));
    }

    public WebElement headerCheckbox(String tableLabel) {
        return tableByLabel(tableLabel).findElement(allSelectionSel);
    }

    public List<WebElement> rowCheckboxesByCell(String tableSel, String cell) {
        List<WebElement> checkboxes = new ArrayList<>();
        rowsByCellText(tableSel, cell).forEach(r -> {
            WebElement checkbox = r.findElement(rowSelectionSel);
            checkboxes.add(checkbox);
        });
        return checkboxes;
    }

    public List<WebElement> checkedRows(String tabelLabel) {
        return tableByLabel(tabelLabel).findElements(checkedRowsSel);
    }

    public TableActions forTable(String tableLabel) {
        return new TableActions(this, tableLabel);
    }
}
