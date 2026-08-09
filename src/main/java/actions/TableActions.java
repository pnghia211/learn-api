package actions;

import Utils.WaitForTableLoading;
import assertions.TableAssertions;
import component.main.table.FooterComp;
import component.main.table.HeaderDropdownComp;
import component.main.table.RowDropdownComp;
import component.main.table.TableComp;
import helpers.JsExecutorHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
    private final TableComp parent;
    private FooterComp footerComp;
    private RowDropdownComp rowDropdownComp;
    private HeaderDropdownComp headerDropdownComp;
    private final String tableLabel;

    public TableActions(TableComp parent, String tableLabel) {
        this.parent = parent;
        this.tableLabel = tableLabel;
    }

    private WebElement getTable() {
        return parent.tableByLabel(tableLabel);
    }

    public List<WebElement> getRows() {
        return parent.tableRows(tableLabel);
    }

    public FooterComp getFooterComp() {
        if (footerComp == null) footerComp = parent.footerComp(tableLabel);
        return footerComp;
    }

    public RowDropdownComp getDropDownComp() {
        if (rowDropdownComp == null) rowDropdownComp = parent.rowDropdownComp(tableLabel);
        return rowDropdownComp;
    }

    public HeaderDropdownComp getHeaderDropDownComp() {
        if (headerDropdownComp == null) headerDropdownComp = parent.headerDropdownComp(tableLabel);
        return headerDropdownComp;
    }

    public String getFooterTotalAmountTxt() {
        int index = getHeadersMap().get(HeaderColumnOption.AMOUNT.headerLabel);
        return getFooterComp().getFooterCellByIndex(index + 1).getText();
    }

    public List<WebElement> getRowsByCellValue(String cell) {
        return parent.rowsByCellText(tableLabel, cell);
    }

    public WebElement getActionBtnByCellValue(String tableLabel, String cell) {
        return getDropDownComp().actionBtnByCellText(tableLabel, cell);
    }

    public WebElement getDropdownBtn() {
        return getHeaderDropDownComp().headerDropdownButton(tableLabel);
    }

    public List<String> getCellsByColumn(String header) {
        Integer index = getHeadersMap().get(header);
        if (index == null) {
            return List.of();
        }

        List<WebElement> cellsEle = parent.cellsByColumnIndex(tableLabel, index + 1);
        if (cellsEle.isEmpty()) {
            throw new IllegalStateException("No cells found for column: " + header);
        }

        return cellsEle.stream().map(WebElement::getText).toList();
    }

    public Integer getCellsTotalAmount() {
        Integer index = getHeadersMap().get(HeaderColumnOption.AMOUNT.headerLabel());

        List<WebElement> cellsEle = parent.cellsByColumnIndex(tableLabel, index + 1);

        return cellsEle.stream().mapToInt(c -> {
            String text = c.getText().replace("€", "").replace(",", "").trim();
            return new BigDecimal(text).setScale(0, RoundingMode.HALF_UP).intValue();
        }).sum();
    }

    public Integer getFooterTotalAmount() {
        String result = getFooterTotalAmountTxt().replace("Total: €", "").replace(",", "");
        return new BigDecimal(result).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public TableActions clickActionButton(String cell) {
        parent.actions().moveToElement(getActionBtnByCellValue(tableLabel, cell)).perform();
        getActionBtnByCellValue(tableLabel, cell).click();
        return this;
    }

    public TableActions selectCopyPaymentIdOpt(ActionOption option) {
        parent.rowDropdownComp(tableLabel).menuItemByLabel(option.label()).click();
        return this;
    }

    public WebElement getCopyNotificationPopup() {
        return parent.rowDropdownComp(tableLabel).copyNotificationPopup();
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

    public enum DropdownOptionState {
        SELECTED, UNSELECTED
    }

    public TableActions selectDropdownOption(HeaderColumnOption option) {
        return setDropdownOption(option, DropdownOptionState.SELECTED);
    }

    public TableActions unselectDropdownOption(HeaderColumnOption option) {
        return setDropdownOption(option, DropdownOptionState.UNSELECTED);
    }

    private TableActions setDropdownOption(HeaderColumnOption option, DropdownOptionState desiredState) {
        selectDropdownButton();

        WebElement optionEle = parent.headerDropdownComp(tableLabel).headerDropdownOptions(tableLabel, option.dropdownLabel());
        boolean isChecked = "checked".equalsIgnoreCase(optionEle.getAttribute("data-state"));
        boolean shouldBeChecked = desiredState == DropdownOptionState.SELECTED;

        if (isChecked != shouldBeChecked) {
            optionEle.click();
            new WebDriverWait(parent.driver(), Duration.ofSeconds(5))
                    .until(d -> shouldBeChecked == "checked".equalsIgnoreCase(
                            parent.headerDropdownComp(tableLabel).headerDropdownOptions(tableLabel, option.dropdownLabel())
                                    .getAttribute("data-state")));
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
                headersMap.put(text, i);
            }
        }
        return headersMap;
    }

    public List<WebElement> getRowCheckboxesByCell(String cell) {
        return parent.rowCheckboxesByCell(tableLabel, cell);
    }

    public List<WebElement> getCheckedRows() {
        return parent.checkedRows(tableLabel);
    }

    public int[] getSelectedRowsAndTotalCounts() {
        String text = getFooterComp().getFooterSummary().getText();
        List<Integer> numbers = Pattern.compile("\\d+").matcher(text)
                .results()
                .map(m -> Integer.parseInt(m.group()))
                .toList();

        if (numbers.size() < 2) {
            throw new IllegalStateException("Cannot extract selected/total counts from text: " + text);
        }
        return new int[]{numbers.get(0), numbers.get(1)};
    }

    public WebElement getHeaderCheckbox() {
        return parent.headerCheckbox(tableLabel);
    }

    public Map<String, String> rowToMap(WebElement row, Map<String, Integer> headersMap) {
        List<WebElement> cells = row.findElements(By.cssSelector("td"));
        Map<String, String> rowMap = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : headersMap.entrySet()) {
            String columnLabel = entry.getKey();
            int index = entry.getValue();
            rowMap.put(columnLabel, cells.get(index).getText());
        }

        return rowMap;
    }

    public List<Map<String, String>> getActualRowsEle() {
        Map<String, Integer> headersMap = getHeadersMap();
        List<WebElement> rows = getRows();

        return rows.stream().map(r -> rowToMap(r, headersMap)).toList();
    }

    public Map<String, String> getActualRowEle(String cell) {
        Map<String, Integer> headersMap = getHeadersMap();
        List<WebElement> rows = getRowsByCellValue(cell);

        return rows.stream().map(r -> rowToMap(r, headersMap)).toList().get(0);
    }

    public TableActions setAllSelectionHeaderToDefaultState() {
        WebElement ele = parent.headerCheckbox(tableLabel);
        new WebDriverWait(parent.driver(), Duration.ofSeconds(5))
                .until(d -> {
                    ele.click();
                    return "unchecked".equalsIgnoreCase(ele.getAttribute("data-state"));
                });
        return this;
    }

    public TableActions selectCheckboxByCell(String cell) {
        getRowCheckboxesByCell(cell).forEach(checkbox -> {
            if ("checked".equals(checkbox.getDomAttribute("data-state"))) {
                return;
            }
            JsExecutorHelper.scrollIntoViewCentered(parent.driver(), checkbox);
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
        return new TableAssertions(this);
    }
}
