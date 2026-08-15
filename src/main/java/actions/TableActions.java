package actions;

import utils.WaitForClassTransition;
import assertions.TableAssertions;
import component.main.table.TableComp;
import component.main.table.HeaderComp;
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

    public TableActions(WebDriver driver, String tableLabel, int tableIndex) {
        this.driver = driver;
        this.tableLabel = tableLabel;
        this.tableIndex = tableIndex;
        this.table = new TableComp(driver, tableLabel, tableIndex);
    }

    private WebElement getTable() {
        return table.tableByLabel(tableLabel, tableIndex);
    }

    public List<WebElement> getRows() {
        return table.tableRows(tableLabel);
    }

    public FooterActions footerActions() {
        if (footerActions == null) footerActions = new FooterActions(table.footerComp(tableLabel), tableLabel, this);
        return footerActions;
    }

    public HeaderActions headerActions() {
        if (headerActions == null)
            headerActions = new HeaderActions(new HeaderComp(driver, tableLabel, tableIndex), this);
        return headerActions;
    }

    public List<WebElement> getRowsByCellValue(String cell) {
        return table.rowsByCellText(tableLabel, cell);
    }

    public WebElement getActionBtnByCellValue(String tableLabel, String cell) {
        return table.rowDropdownComp(tableLabel).actionBtnByCellText(tableLabel, cell);
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

        List<WebElement> cellsEle = table.cellsByColumnIndex(tableLabel, index + 1);
        if (cellsEle.isEmpty()) {
            throw new IllegalStateException("No cells found for column: " + option);
        }

        return cellsEle.stream().map(WebElement::getText).toList();
    }

    public Integer getCellsTotalAmount() {
        Integer index = headerActions().getHeadersMap().get(HeaderColumnOption.AMOUNT.label());

        List<WebElement> cellsEle = table.cellsByColumnIndex(tableLabel, index + 1);

        return cellsEle.stream().mapToInt(c -> {
            String text = c.getText().replace("€", "").replace(",", "").trim();
            return new BigDecimal(text).setScale(0, RoundingMode.HALF_UP).intValue();
        }).sum();
    }

    public TableActions clickActionButton(String cell) {
        table.actions().moveToElement(getActionBtnByCellValue(tableLabel, cell)).perform();
        getActionBtnByCellValue(tableLabel, cell).click();
        return this;
    }

    public TableActions selectCopyPaymentIdOpt(ActionOption option) {
        table.rowDropdownComp(tableLabel).menuItemByLabel(option.label()).click();
        return this;
    }

    public WebElement getCopyNotificationPopup() {
        return table.rowDropdownComp(tableLabel).copyNotificationPopup();
    }

    public TableActions scrollTillCellDisplayed(String cell) {
        int attempts = 0;
        int maxAttempts = 20;

        while (attempts < maxAttempts) {
            List<WebElement> matches = getRowsByCellValue(cell);
            if (!matches.isEmpty()) {
                table.actions().scrollToElement(matches.get(0)).perform();
                return this;
            }

            List<WebElement> rows = getRows();

            WebElement lastRow = rows.get(rows.size() - 1);
            table.actions().scrollToElement(lastRow).perform();

            WebElement tableEle = getTable();

            new WebDriverWait(table.driver(), Duration.ofSeconds(5))
                    .until(new WaitForClassTransition(tableEle));

            attempts++;
        }
        return this;
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
        return table.rowCheckboxesByCell(tableLabel, cell);
    }

    public List<WebElement> getCheckedRows() {
        return table.checkedRows(tableLabel);
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

    public enum ActionOption {
        COPY_PAYMENT("Copy payment ID");
        private final String label;

        ActionOption(String label) {
            this.label = label;
        }

        public String label() {
            return label;
        }
    }

    public TableAssertions verify() {
        return new TableAssertions(this);
    }
}
