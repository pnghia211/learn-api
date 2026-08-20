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
    private By rowSel = By.cssSelector("tbody > tr");
    private By rowSelectionSel = By.cssSelector("tr td [aria-label='Select row']");
    private By checkedRowsSel = By.cssSelector("tr td [aria-label='Select row'][data-state='checked']");
    private String cellsByColumnIndexXpath = "tbody tr td:nth-of-type(%s)";
    private By cellSel = By.cssSelector("tbody > tr > td");
    private By expandBtnRowSel = By.cssSelector("button[class]:not([class*='invisible']) span[class*='plus']");
    private By expandableRow = By.cssSelector("tbody tr:has(button[class]:not([class*='invisible']))");
    private By pinnedRows = By.cssSelector("tbody tr[data-pinned='top']");
    private By unpinRowBtn = By.cssSelector("td button[aria-label='Unpin row']");
    private By unpinRows = By.cssSelector("tbody tr:not([data-pinned='top'])");
    private By pinRowBtn = By.cssSelector("td button[aria-label='Pin row to top']");
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

    public PaginationComp paginationComp() {
        return new PaginationComp(driver, tableLabel, tableIndex);
    }

    public WebElement tableByLabel() {
        return getComponentBasedOnHeader(tableLabel, tableSel, tableIndex);
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

    public WebElement rowExpandedBtnByCell(String cell) {
        List<WebElement> rows = rowsByCellText(cell);
        if (rows.size() > 1) {
            throw new IllegalStateException(
                    "Expected exactly one row for cell '" + cell + "' but found " + rows.size());
        }
        return rows.get(0).findElement(expandBtnRowSel);
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
        return tableByLabel().findElements(By.cssSelector(String.format(cellsByColumnIndexXpath, headerIndex)));
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

    public List<WebElement> unpinnedRows(){
        return tableByLabel().findElements(unpinRows);
    }

    public List<WebElement> checkedRows() {
        return tableByLabel().findElements(checkedRowsSel);
    }
}