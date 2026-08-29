package actions;

import assertions.TableAssertions;
import component.main.table.TableComp;
import data.HeaderColumnOption;
import data.RowActionOption;
import helpers.JsExecutorHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitForClassTransition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableActions {
    private final TableComp tableComp;
    private HeaderActions headerActions;
    private FooterActions footerActions;
    private PaginationActions paginationActions;

    public TableActions(TableComp tableComp) {
        this.tableComp = tableComp;
    }

    private WebElement getTable() {
        return tableComp.tableByLabel();
    }

    public List<WebElement> getRows() {
        return tableComp.tableRows();
    }

    public FooterActions footerActions() {
        if (footerActions == null) footerActions = new FooterActions(tableComp.footerComp(), this);
        return footerActions;
    }

    public HeaderActions headerActions() {
        if (headerActions == null) headerActions = new HeaderActions(tableComp.headerComp(), this);
        return headerActions;
    }

    public PaginationActions paginationActions() {
        if (paginationActions == null) paginationActions = new PaginationActions(tableComp.paginationComp(), this);
        return paginationActions;
    }

    public List<WebElement> getRowsByCellValue(String cell) {
        return tableComp.rowsByCellText(cell);
    }

    public WebElement getActionBtnByCellValue(String cell) {
        return tableComp.rowDropdownComp().actionBtnByCellText(cell);
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

        List<WebElement> cellsEle = tableComp.cellsByColumnIndex(index + 1);
        if (cellsEle.isEmpty()) {
            throw new IllegalStateException("No cells found for column: " + option);
        }

        return cellsEle.stream().map(WebElement::getText).toList();
    }

    public Integer getCellsTotalAmount() {
        Integer index = headerActions().getHeadersMap().get(HeaderColumnOption.AMOUNT.label());

        List<WebElement> cellsEle = tableComp.cellsByColumnIndex(index + 1);

        return cellsEle.stream().mapToInt(c -> {
            String text = c.getText().replace("€", "").replace(",", "").trim();
            return new BigDecimal(text).setScale(0, RoundingMode.HALF_UP).intValue();
        }).sum();
    }

    public TableActions clickActionButton(String cell) {
        tableComp.actions().moveToElement(getActionBtnByCellValue(cell)).perform();
        getActionBtnByCellValue(cell).click();
        return this;
    }

    public TableActions selectCopyPaymentIdOpt(RowActionOption option) {
        tableComp.rowDropdownComp().menuItemByLabel(option.label()).click();
        return this;
    }

    public WebElement getCopyNotificationPopup() {
        return tableComp.rowDropdownComp().copyNotificationPopup();
    }

    public TableActions scrollTillCellDisplayed(String cell) {
        int attempts = 0;
        int maxAttempts = 25;
        int lastPosition = 0;

        while (attempts < maxAttempts) {
            WebElement tableEle = getTable();
            List<WebElement> tableRows = getRows();
            WebElement match = findMatchInNewRows(tableRows, lastPosition, cell);

            if (match != null) {
                tableComp.actions().scrollToElement(match).perform();
                return this;
            }

            int currentRowCount = tableRows.size();

            WebElement lastRow = tableEle.findElement(By.cssSelector("tbody > tr:nth-of-type(" + currentRowCount + ")"));
            tableComp.actions().scrollToElement(lastRow).perform();

            new WebDriverWait(tableComp.driver(), Duration.ofSeconds(5))
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
            tableComp.actions().scrollToElement(matches.get(0)).perform();
            return true;
        }
        return false;
    }

    public List<WebElement> getRowCheckboxesByCell(String cell) {
        return tableComp.rowCheckboxesByCell(cell);
    }

    public List<WebElement> getCheckedRows() {
        return tableComp.checkedRows();
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
            JsExecutorHelper.scrollIntoViewCentered(tableComp.driver(), checkbox);
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

    public TableActions clickExpandBtn() {
        tableComp.expandButton().click();
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
        return toRowsData(tableComp.pinnedRows());
    }

    public List<Map<String, String>> getUnpinnedRowsData() {
        return toRowsData(tableComp.unpinnedRows());
    }

    public TableActions unpinAllRows() {
        tableComp.unpinnedButtons().forEach(WebElement::click);
        return this;
    }

    public TableActions pinRowByCell(String cell) {
        tableComp.pinBtnRowByCell(cell).click();
        return this;
    }

    public TableActions pinRowsByCells(List<String> cells) {
        cells.forEach(this::pinRowByCell);
        List<WebElement> pinnedRows = tableComp.pinnedRows();
        tableComp.actions().scrollToElement(pinnedRows.get(pinnedRows.size() - 1));
        return this;
    }

    public TableActions expandUntilCellDisplayed(String targetCell) {
        if (isCellDisplayed(targetCell)) {
            return this;
        }

        List<WebElement> topLevelRows = tableComp.expandableRows();
        boolean found = expandBranch(topLevelRows, targetCell);

        if (!found) {
            throw new NoSuchElementException(targetCell + " is not displayed!!!");
        }

        return this;
    }

    private boolean expandBranch(List<WebElement> candidateRows, String targetCell) {
        for (WebElement row : candidateRows) {
            clickExpandBtn();

            if (isCellDisplayed(targetCell)) {
                return true;
            }

            List<WebElement> children = getExpandableRows(row);

            if (!children.isEmpty()) {
                boolean foundDeeper = expandBranch(children, targetCell);
                if (foundDeeper) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<WebElement> getExpandableRows(WebElement anchorRow) {
        int anchorDepth = getRowDepth(anchorRow);
        List<WebElement> children = new ArrayList<>();

        List<WebElement> siblings = anchorRow.findElements(By.xpath("following-sibling::tr[@data-expanded][.//button]"));
        for (WebElement sibling : siblings) {
            int siblingDepth = getRowDepth(sibling);

            if (siblingDepth <= anchorDepth) {
                break;
            }

            if (siblingDepth > anchorDepth + 1) {
                continue;
            }

            if (isExpandable(sibling)) {
                children.add(sibling);
            };
        }

        return children;
    }

    private int getRowDepth(WebElement row) {
        String style = row.findElement(By.cssSelector("[style*='rem']")).getAttribute("style");
        Matcher matcher = Pattern.compile("(\\d+)rem").matcher(style);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalStateException("Cannot determine row depth from style: " + style);
    }

    private boolean isExpandable(WebElement row) {
        return row.findElement(By.cssSelector("button[class]:not([class*='invisible'])")).isEnabled();
    }

    public TableAssertions verify() {
        return new TableAssertions(this);
    }
}