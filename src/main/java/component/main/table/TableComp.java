package component.main.table;

import actions.TableActions;
import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TableComp extends BaseComp {
    private By rowSel = By.cssSelector("tbody tr");
    private By rowSelectionSel = By.cssSelector("tr td [aria-label='Select row']");
    private By checkedRowsSel = By.cssSelector("tr td [aria-label='Select row'][data-state='checked']");
    private String cellsByColumnIndexXpath = ".//tbody/tr/td[%s]";

    public TableComp(WebDriver driver) {
        super(driver);
    }

    public HeaderComp headerDropdownComp(String tableLabel) {
        return new HeaderComp(driver, tableLabel);
    }

    public RowDropdownComp rowDropdownComp(String tableLabel) {
        return new RowDropdownComp(driver, tableLabel);
    }

    public FooterComp footerComp(String tableLabel) {
        return new FooterComp(driver, tableLabel);
    }

    public List<WebElement> tableRows(String tableLabel) {
        return tableByLabel(tableLabel).findElements(rowSel);
    }

    public List<WebElement> cellsByColumnIndex(String tableLabel, int headerIndex) {
        return tableByLabel(tableLabel).findElements(By.xpath(String.format(cellsByColumnIndexXpath, headerIndex)));
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
