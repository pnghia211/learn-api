package component.main.table;

import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TableComp extends BaseComp {
    protected String tableLabel;
    protected int tableIndex;
    protected By tableSel = By.xpath(".//*[@data-slot='root'][./table]");
    private By rowSel = By.cssSelector("tbody tr");
    private By rowSelectionSel = By.cssSelector("tr td [aria-label='Select row']");
    private By checkedRowsSel = By.cssSelector("tr td [aria-label='Select row'][data-state='checked']");
    private String cellsByColumnIndexXpath = ".//tbody/tr/td[%s]";
    private String rowByCellValueXpath = ".//td[normalize-space()='%s']/..";

    public TableComp(WebDriver driver, String tableLabel, int tableIndex) {
        super(driver);
        this.tableLabel = tableLabel;
        this.tableIndex = tableIndex;
    }

    public WebElement tableByLabel(String tableLabel, int tableIndex) {
        return getComponentBasedOnHeader(tableLabel, tableSel, tableIndex);
    }

    public List<WebElement> rowsByCellText(String tableLabel, String cell) {
        return tableByLabel(tableLabel, tableIndex).findElements(By.xpath(String.format(rowByCellValueXpath, cell)));
    }

    public HeaderComp headerDropdownComp(String tableLabel) {
        return new HeaderComp(driver, tableLabel, tableIndex);
    }

    public RowDropdownComp rowDropdownComp(String tableLabel) {
        return new RowDropdownComp(driver, tableLabel, tableIndex);
    }

    public FooterComp footerComp(String tableLabel) {
        return new FooterComp(driver, tableLabel, tableIndex);
    }

    public List<WebElement> tableRows(String tableLabel) {
        return tableByLabel(tableLabel, tableIndex).findElements(rowSel);
    }

    public List<WebElement> cellsByColumnIndex(String tableLabel, int headerIndex) {
        return tableByLabel(tableLabel, tableIndex).findElements(By.xpath(String.format(cellsByColumnIndexXpath, headerIndex)));
    }

    public List<WebElement> rowCheckboxesByCell(String tableSel, String cell) {
        List<WebElement> checkboxes = new ArrayList<>();
        rowsByCellText(tableSel, cell).forEach(r -> {
            WebElement checkbox = r.findElement(rowSelectionSel);
            checkboxes.add(checkbox);
        });
        return checkboxes;
    }

    public List<WebElement> checkedRows(String tableLabel) {
        return tableByLabel(tableLabel, tableIndex).findElements(checkedRowsSel);
    }
}