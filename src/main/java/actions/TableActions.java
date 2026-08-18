package actions;

import data.RowActionOption;
import utils.WaitForClassTransition;
import assertions.TableAssertions;
import component.main.table.TableComp;
import data.HeaderColumnOption;
import helpers.JsExecutorHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TableActions {
    private final WebDriver driver;
    private final String tableLabel;
    private final int tableIndex;
    private final TableComp table;
    private HeaderActions headerActions;
    private FooterActions footerActions;
    private PaginationActions paginationActions;

    public TableActions(WebDriver driver, String tableLabel, int tableIndex) {
        this.driver = driver;
        this.tableLabel = tableLabel;
        this.tableIndex = tableIndex;
        this.table = new TableComp(driver, tableLabel, tableIndex);
    }

    private WebElement getTable() {
        return table.tableByLabel();
    }

    public List<WebElement> getRows() {
        return table.tableRows();
    }

    public FooterActions footerActions() {
        if (footerActions == null) footerActions = new FooterActions(table.footerComp(), this);
        return footerActions;
    }

    public HeaderActions headerActions() {
        if (headerActions == null) headerActions = new HeaderActions(table.headerComp(), this);
        return headerActions;
    }

    public PaginationActions paginationActions() {
        if (paginationActions == null) paginationActions = new PaginationActions(table.paginationComp(), this);
        return paginationActions;
    }

    public List<WebElement> getRowsByCellValue(String cell) {
        return table.rowsByCellText(cell);
    }

    public WebElement getActionBtnByCellValue(String cell) {
        return table.rowDropdownComp().actionBtnByCellText(cell);
    }

    public List<String> getCellsByColumn(HeaderColumnOption option) {
        Map<String, Integer> headersMap = headerActions().getHeadersMap();

        Integer index = headersMap.entrySet().stream()
                .filter(entry -> option.matchesHeader(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (index == null) {
            return List.of();
        }

        List<WebElement> cellsEle = table.cellsByColumnIndex(index + 1);
        if (cellsEle.isEmpty()) {
            throw new IllegalStateException("No cells found for column: " + option);
        }

        return cellsEle.stream().map(WebElement::getText).toList();
    }

    public Integer getCellsTotalAmount() {
        Integer index = headerActions().getHeadersMap().get(HeaderColumnOption.AMOUNT.label());

        List<WebElement> cellsEle = table.cellsByColumnIndex(index + 1);

        return cellsEle.stream().mapToInt(c -> {
            String text = c.getText().replace("€", "").replace(",", "").trim();
            return new BigDecimal(text).setScale(0, RoundingMode.HALF_UP).intValue();
        }).sum();
    }

    public TableActions clickActionButton(String cell) {
        table.actions().moveToElement(getActionBtnByCellValue(cell)).perform();
        getActionBtnByCellValue(cell).click();
        return this;
    }

    public TableActions selectCopyPaymentIdOpt(RowActionOption option) {
        table.rowDropdownComp().menuItemByLabel(option.label()).click();
        return this;
    }

    public WebElement getCopyNotificationPopup() {
        return table.rowDropdownComp().copyNotificationPopup();
    }

    public TableActions scrollTillCellDisplayed(String cell) {
        int attempts = 0;
        int maxAttempts = 25;
        int lastPosition = 0;

        while (attempts < maxAttempts) {
            WebElement tableEle = getTable();
            WebElement match = findMatchInNewRows(getRows(), lastPosition, cell);

            if (match != null) {
                table.actions().scrollToElement(match).perform();
                return this;
            }

            int currentRowCount = getRows().size();

            WebElement lastRow = tableEle.findElement(By.cssSelector("tbody > tr:nth-of-type(" + currentRowCount + ")"));
            table.actions().scrollToElement(lastRow).perform();

            new WebDriverWait(table.driver(), Duration.ofSeconds(5))
                    .until(new WaitForClassTransition(tableEle));

            lastPosition = currentRowCount;
            attempts++;
        }
        return this;
    }

    private WebElement findMatchInNewRows(List<WebElement> allRows, int lastPosition, String cell) {
        for (int i = lastPosition; i < allRows.size(); i++) {
            WebElement row = allRows.get(i);
            List<WebElement> cells = row.findElements(By.cssSelector("td"));
            for (WebElement cellEl : cells) {
                if (cell.equals(cellEl.getText().trim())) {
                    return row;
                }
            }
        }
        return null;
    }

    public boolean isCellDisplayed(String cell) {
        List<WebElement> matches = getRowsByCellValue(cell);
        if (!matches.isEmpty() && matches.get(0).isDisplayed()) {
            table.actions().scrollToElement(matches.get(0)).perform();
            return true;
        }
        return false;
    }

    public List<WebElement> getRowCheckboxesByCell(String cell) {
        return table.rowCheckboxesByCell(cell);
    }

    public List<WebElement> getCheckedRows() {
        return table.checkedRows();
    }

    public int[] getSelectedRowsAndTotalCounts() {
        String text = footerActions().getFooterSummaryTxt();
        List<Integer> numbers = Pattern.compile("\\d+").matcher(text)
                .results()
                .map(m -> Integer.parseInt(m.group()))
                .toList();

        if (numbers.size() < 2) {
            throw new IllegalStateException("Cannot extract selected/total counts from text: " + text);
        }
        return new int[]{numbers.get(0), numbers.get(1)};
    }

    public Map<String, String> rowToMap(WebElement row, Map<String, Integer> headersMap) {
        List<WebElement> cells = row.findElements(By.cssSelector("td"));
        Map<String, String> rowMap = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : headersMap.entrySet()) {
            String columnLabel = entry.getKey();
            int index = entry.getValue();
            rowMap.put(columnLabel, cells.get(index).getDomProperty("textContent").trim());
        }

        return rowMap;
    }

    private List<Map<String, String>> toRowsData(List<WebElement> rows) {
        Map<String, Integer> headersMap = headerActions().getHeadersMap();
        return rows.stream().map(r -> rowToMap(r, headersMap)).toList();
    }

    public List<Map<String, String>> getAllRowsData() {
        return toRowsData(getRows());
    }

    public Map<String, String> getRowData(String cell) {
        List<Map<String, String>> rows = toRowsData(getRowsByCellValue(cell));
        if (rows.isEmpty()) {
            throw new IllegalStateException("No row found matching cell value: " + cell);
        }
        return rows.get(0);
    }

    public TableActions selectCheckboxByCell(String cell) {
        getRowCheckboxesByCell(cell).forEach(checkbox -> {
            if ("checked".equals(checkbox.getDomAttribute("data-state"))) {
                return;
            }
            JsExecutorHelper.scrollIntoViewCentered(table.driver(), checkbox);
            checkbox.click();
        });
        return this;
    }

    public TableActions selectCheckboxesByCells(List<String> cells) {
        for (String cell : cells) {
            selectCheckboxByCell(cell);
        }
        return this;
    }

    public TableActions clickExpandBtnByCell(String... cells) {
        for (String cell : cells) {
            table.rowExpandedBtnByCell(cell).click();

            new WebDriverWait(table.driver(), Duration.ofSeconds(5))
                    .until(driver -> "true".equalsIgnoreCase(table.rowsByCellText(cell).get(0).getAttribute("data-expanded")));
        }
        return this;
    }

    public TableActions clickExpandBtn() {
        table.expandButtons().stream()
                .findFirst().ifPresent(WebElement::click);
        return this;
    }

    public boolean isCellDisplayedInTree(String cell) {
        int maxAttempts = 10;
        while (!isCellDisplayed(cell) && maxAttempts-- > 0) {
            clickExpandBtn();
        }
        return isCellDisplayed(cell);
    }

    public List<Map<String, String>> getPinnedRowsData() {
        return toRowsData(table.pinnedRows());
    }

    public List<Map<String, String>> getUnpinnedRowsData() {
        return toRowsData(table.unpinnedRows());
    }
    public TableActions unpinAllRows() {
        table.unpinnedButtons().forEach(WebElement::click);
        return this;
    }

    public TableActions pinRowByCell(String cell) {
        table.pinBtnRowByCell(cell).click();
        return this;
    }

    public TableActions pinRowsByCells(List<String> cells) {
        cells.forEach(this::pinRowByCell);
        List<WebElement> pinnedRows = table.pinnedRows();
        table.actions().scrollToElement(pinnedRows.get(pinnedRows.size() - 1));
        return this;
    }

    public TableAssertions verify() {
        return new TableAssertions(this);
    }
}