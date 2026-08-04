package actions;

import Utils.WaitForTableLoading;
import assertions.TableAssertions;
import component.main.TableComp;
import helpers.TableRecordNormalizer;
import model.TableRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableActions {
    private final TableComp parent;
    private final String tableLabel;

    public TableActions(TableComp parent, String tableLabel) {
        this.parent = parent;
        this.tableLabel = tableLabel;
    }

    private WebElement getTable() {
        return parent.tableByTableLabel(tableLabel);
    }

    public List<WebElement> getRows() {
        return parent.tableRows(tableLabel);
    }

    public List<WebElement> getRowsByCellValue(String cell) {
        return parent.rowByCellText(tableLabel, cell);
    }

    public WebElement getActionBtnByCellValue(String tableLabel, String cell) {
        return parent.rowDropdownComp().actionBtnByCellText(tableLabel, cell);
    }

    public WebElement getDropdownBtn() {
        return parent.headerDropdownComp().headerDropdownButton(tableLabel);
    }

    public List<String> getCellsByColumn(String header) {
        Integer index = getHeadersMap().get(header);
        if (index == null) {
            return List.of();
        }

        List<WebElement> cellsEle = parent.cellsByColumnIndex(tableLabel, index);
        if (cellsEle.isEmpty()) {
            throw new IllegalStateException("No cells found for column: " + header);
        }

        return cellsEle.stream().map(WebElement::getText).toList();
    }

    public TableActions clickActionButton(String cell) {
        getActionBtnByCellValue(tableLabel, cell).click();
        return this;
    }

    public TableActions selectCopyPaymentIdOpt(ActionOption option) {
        parent.rowDropdownComp().menuItemByLabel(option.label()).click();
        return this;
    }

    public WebElement getCopyNotificationPopup() {
        return parent.rowDropdownComp().copyNotificationPopup();
    }

    public TableActions scrollTillCelDisplayed(String cell) {
        int attempts = 0;
        int maxAttempts = 20;

        while (attempts < maxAttempts) {
            if (isCellDisplayed(cell)) return this;

            List<WebElement> rows = getRows();

            WebElement lastRow = rows.get(rows.size() - 1);
            parent.actions().scrollToElement(lastRow).perform();

            WebElement tableEle = getTable();

            new WebDriverWait(parent.driver(), Duration.ofSeconds(5))
                    .until(d -> new WaitForTableLoading(tableEle));

            attempts++;
        }
        return this;
    }

    public boolean isCellDisplayed(String cell) {
        List<WebElement> matches = getRowsByCellValue(cell);
        if (!matches.isEmpty() && matches.get(0).isDisplayed()) {
            parent.actions().scrollToElement(matches.get(0)).perform();
            return true;
        }
        return false;
    }

    private TableActions selectDropdownButton() {
        WebElement button = getDropdownBtn();
        if (!"open".equalsIgnoreCase(button.getAttribute("data-state"))) {
            button.click();
        }
        return this;
    }

    private TableActions unselectDropdownButton() {
        WebElement button = getDropdownBtn();
        if ("open".equalsIgnoreCase(button.getAttribute("data-state"))) {
            parent.actions().sendKeys(Keys.ESCAPE).perform();
        }
        return this;
    }

    public TableActions unselectDropdownOption(HeaderColumnOption option) {
        selectDropdownButton();
        WebElement optionEle = parent.headerDropdownComp().headerDropdownOptions(tableLabel, option.dropdownLabel());
        String state = optionEle.getAttribute("data-state");

        if (state.equalsIgnoreCase("checked")) {
            optionEle.click();
            new WebDriverWait(parent.driver(), Duration.ofSeconds(5))
                    .until(d -> !"checked".equalsIgnoreCase(
                            parent.headerDropdownComp().headerDropdownOptions(tableLabel, option.dropdownLabel()).getAttribute("data-state")));
        }
        unselectDropdownButton();
        return this;
    }

    public Map<String, Integer> getHeadersMap() {
        List<WebElement> headers = parent.headerColumns(tableLabel);
        Map<String, Integer> headersMap = new LinkedHashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            String text = headers.get(i).getText().trim();
            if (!text.isEmpty()) {
                headersMap.put(text, i + 1);
            }
        }

        return headersMap;
    }

    public List<TableRecord> rowsToRecords() {
        Map<String, Integer> headersMap = getHeadersMap();
        List<TableRecord> raws = getRows().stream().map(row -> rowToRecord(row, headersMap)).toList();
        return raws.stream().map(TableRecordNormalizer::normalizeActual).toList();
    }

    public TableRecord rowToRecord(WebElement row, Map<String, Integer> headersMap) {
        List<WebElement> cells = row.findElements(By.cssSelector("td"));

        String id = getCellTextOrNull(cells, headersMap, HeaderColumnOption.ID);
        String date = getCellTextOrNull(cells, headersMap, HeaderColumnOption.DATE);
        String status = getCellTextOrNull(cells, headersMap, HeaderColumnOption.STATUS);
        String email = getCellTextOrNull(cells, headersMap, HeaderColumnOption.EMAIL);
        String amount = getCellTextOrNull(cells, headersMap, HeaderColumnOption.AMOUNT);

        return new TableRecord(id, date, status, email, amount);
    }

    private String getCellTextOrNull(List<WebElement> cells, Map<String, Integer> headersMap, HeaderColumnOption column) {
        Integer index = headersMap.get(column.headerLabel);
        if (index == null) {
            return null;
        }
        return cells.get(index - 1).getText();
    }

    public enum HeaderColumnOption {
        ID("Id", "#"),
        DATE("Date", "Date"),
        STATUS("Status", "Status"),
        EMAIL("Email", "Email"),
        AMOUNT("Amount", "Amount");

        private final String dropdownLabel;
        private final String headerLabel;

        HeaderColumnOption(String dropdownLabel, String headerLabel) {
            this.dropdownLabel = dropdownLabel;
            this.headerLabel = headerLabel;
        }

        public String dropdownLabel() {
            return dropdownLabel;
        }

        public String headerLabel() {
            return headerLabel;
        }

        public static HeaderColumnOption fromString(String input) {
            for (HeaderColumnOption option : values()) {
                if (option.dropdownLabel.equalsIgnoreCase(input)
                        || option.headerLabel.equalsIgnoreCase(input)
                        || option.name().equalsIgnoreCase(input)) {
                    return option;
                }
            }
            throw new IllegalArgumentException("Unknown column option: " + input);
        }
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
        return new TableAssertions(parent, this);
    }
}
