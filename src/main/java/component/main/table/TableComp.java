package component.main.table;

import component.main.BaseComp;
import component.main.form.InputComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TableComp extends BaseComp {
    private WebElement cachedRoot;
    protected final String tableLabel;
    protected final int tableIndex;
    private static final By rowSel = By.cssSelector("tbody > tr");
    private static final By rowSelectionSel = By.cssSelector("tr td [aria-label='Select row']");
    private static final By checkedRowsSel = By.cssSelector("tr td [aria-label='Select row'][data-state='checked']");
    private static final String cellsByColumnIndexXpath = "tbody tr td:nth-of-type(%s)";
    private static final By cellSel = By.cssSelector("tbody > tr > td");
    private static final By expandBtnRowSel = By.cssSelector("button[class]:not([class*='invisible']) span[class*='plus']");
    private static final By expandableRow = By.cssSelector("tbody tr:has(button[class]:not([class*='invisible']))");
    private static final By pinnedRows = By.cssSelector("tbody tr[data-pinned='top']");
    private static final By unpinRowBtn = By.cssSelector("td button[aria-label='Unpin row']");
    private static final By unpinRows = By.cssSelector("tbody tr:not([data-pinned='top'])");
    private static final By pinRowBtn = By.cssSelector("td button[aria-label='Pin row to top']");

    public TableComp(WebDriver driver, String tableLabel, int tableIndex) {
        super(driver);
        this.tableLabel = tableLabel;
        this.tableIndex = tableIndex;
    }

    public FooterComp footerComp() {
        return new FooterComp(this);
    }

    public ToolbarComp toolbarComp() {
        return new ToolbarComp(this);
    }

    public RowDropdownComp rowDropdownComp() {
        return new RowDropdownComp(this);
    }

    public PaginationComp paginationComp() {
        return new PaginationComp(driver, tableLabel);
    }

    public InputComp inputComp() {
        return new InputComp(driver, tableLabel);
    }

    public WebElement tableByLabel() {
        cachedRoot = getOrRefreshCached(cachedRoot, () -> getRootComp(tableLabel, tableIndex));
        return cachedRoot;
    }

    public List<WebElement> rowsByCellText(String cell) {
        List<WebElement> allTds = tableByLabel().findElements(cellSel);
        List<WebElement> matchingRows = new ArrayList<>();

        for (WebElement td : allTds) {
            if (cell.equals(td.getDomProperty("textContent").trim())) {
                WebElement row = td.findElement(By.xpath(".."));
                matchingRows.add(row);
            }
        }
        return matchingRows;
    }

    public WebElement expandButton() {
        return tableByLabel().findElement(expandBtnRowSel);
    }

    public List<WebElement> expandableRows() {
        return tableByLabel().findElements(expandableRow);
    }

    public List<WebElement> tableRows() {
        return tableByLabel().findElements(rowSel);
    }

    public List<WebElement> cellsByColumnIndex(int headerIndex) {
        WebElement root = tableByLabel();
        actions().scrollToElement(root).perform();
        return root.findElements(By.cssSelector(String.format(cellsByColumnIndexXpath, headerIndex)));
    }

    public List<WebElement> rowCheckboxesByCell(String cell) {
        List<WebElement> checkboxes = new ArrayList<>();
        rowsByCellText(cell).forEach(r -> {
            WebElement checkbox = r.findElement(rowSelectionSel);
            checkboxes.add(checkbox);
        });
        return checkboxes;
    }

    public List<WebElement> pinnedRows() {
        return tableByLabel().findElements(pinnedRows);
    }

    public List<WebElement> unpinnedButtons() {
        return tableByLabel().findElements(unpinRowBtn);
    }

    public WebElement pinBtnRowByCell(String cell) {
        return rowsByCellText(cell).get(0).findElement(pinRowBtn);
    }

    public List<WebElement> unpinnedRows() {
        return tableByLabel().findElements(unpinRows);
    }

    public List<WebElement> checkedRows() {
        return tableByLabel().findElements(checkedRowsSel);
    }
}