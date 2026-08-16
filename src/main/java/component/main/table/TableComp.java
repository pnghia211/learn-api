package component.main.table;

import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TableComp extends BaseComp {
    protected String tableLabel;
    protected int tableIndex;
    protected By tableSel = By.xpath(".//*[@data-slot='root'][./table]");
    private By rowSel = By.cssSelector("tbody tr");
    private By rowSelectionSel = By.cssSelector("tr td [aria-label='Select row']");
    private By checkedRowsSel = By.cssSelector("tr td [aria-label='Select row'][data-state='checked']");
    private String cellsByColumnIndexXpath = ".//tbody/tr/td[%s]";
    private String rowByCellValueXpath = ".//tbody/tr/td[normalize-space()='%s']/..";
    private By expandBtnRowSel = By.cssSelector("button[class]:not([class*='invisible']) span[class*='i-lucide:plus']");

    public TableComp(WebDriver driver, String tableLabel, int tableIndex) {
        super(driver);
        this.tableLabel = tableLabel;
        this.tableIndex = tableIndex;
    }

    public FooterComp footerComp() {
        return new FooterComp(driver, tableLabel, tableIndex);
    }

    public HeaderComp headerComp() {
        return new HeaderComp(driver, tableLabel, tableIndex);
    }

    public RowDropdownComp rowDropdownComp() {
        return new RowDropdownComp(driver, tableLabel, tableIndex);
    }

    public WebElement tableByLabel() {
        return getComponentBasedOnHeader(tableLabel, tableSel, tableIndex);
    }

    public List<WebElement> rowsByCellText(String cell) {
        return tableByLabel().findElements(By.xpath(String.format(rowByCellValueXpath, cell)));
    }

    public WebElement rowExpandedBtnByCell(String cell) {
        List<WebElement> rows = rowsByCellText(cell);
        if (rows.size() > 1) {
            throw new IllegalStateException(
                    "Expected exactly one row for cell '" + cell + "' but found " + rows.size());
        }
        return rows.get(0).findElement(expandBtnRowSel);
    }

    public List<WebElement> expandButtons() {
        return tableByLabel().findElements(expandBtnRowSel);
    }

    public List<WebElement> tableRows() {
        return tableByLabel().findElements(rowSel);
    }

    public List<WebElement> cellsByColumnIndex(int headerIndex) {
        return tableByLabel().findElements(By.xpath(String.format(cellsByColumnIndexXpath, headerIndex)));
    }

    public List<WebElement> rowCheckboxesByCell(String cell) {
        List<WebElement> checkboxes = new ArrayList<>();
        rowsByCellText(cell).forEach(r -> {
            WebElement checkbox = r.findElement(rowSelectionSel);
            checkboxes.add(checkbox);
        });
        return checkboxes;
    }

    public List<WebElement> checkedRows() {
        return tableByLabel().findElements(checkedRowsSel);
    }
}