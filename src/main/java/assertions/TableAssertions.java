package assertions;

import actions.PaginationActions;
import actions.TableActions;
import data.HeaderColumnOption;
import data.SortingOption;
import helpers.TableRecordNormalizer;
import model.TableRecord;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static org.testng.Assert.*;

public class TableAssertions {
    private final TableActions actions;

    public TableAssertions(TableActions actions) {
        this.actions = actions;
    }

    private PaginationActions paginationActions() {
        return actions.paginationActions();
    }

    public TableAssertions headerColumnNotDisplayed(HeaderColumnOption option) {
        List<String> actual = new ArrayList<>(actions.headerActions().getHeadersMap().keySet());
        assertFalse(actual.contains(option.label()));
        return this;
    }

    public TableAssertions cellsByColumnDisplayed(HeaderColumnOption option, List<String> expected) {
        List<String> actual = new ArrayList<>(actions.getCellsByColumn(option));
        assertEquals(expected, actual);
        return this;
    }

    public TableAssertions cellsByColumnNotDisplayed(HeaderColumnOption column) {
        List<String> cells = actions.getCellsByColumn(column);
        assertTrue(cells.isEmpty(), "Expected no cells for hidden column: " + column.label());
        return this;
    }

    public TableAssertions footerTotalAmount() {
        assertEquals(actions.footerActions().getFooterTotalAmount(), actions.getCellsTotalAmount());
        return this;
    }

    public TableAssertions rowsByTableDisplayed(List<TableRecord> expected) {
        List<Map<String, String>> actual = actions.getAllRowsData();
        TableRecordNormalizer.verify(expected, actual);
        return this;
    }

    public TableAssertions rowByCelDisplayed(TableRecord expected, String cell) {
        Map<String, String> actual = actions.getRowData(cell);
        TableRecordNormalizer.matches(expected, actual);
        return this;
    }

    public TableAssertions cellDisplayed(String cell) {
        assertTrue(actions.isCellDisplayed(cell));
        return this;
    }

    public TableAssertions copyNotificationPopupDisplayed() {
        assertTrue(actions.getCopyNotificationPopup().isDisplayed());
        return this;
    }

    public TableAssertions checkboxesAreSelected(String cell) {
        List<String> states = actions.getRowCheckboxesByCell(cell)
                .stream()
                .map(e -> e.getAttribute("data-state")).toList();

        boolean allSelected = states.stream()
                .noneMatch("unchecked"::equals);

        if (!allSelected) {
            throw new AssertionError(
                    "Expected all checkboxes to be checked/indeterminate for cell: " + cell + ", but got states: " + states);
        }

        return this;
    }

    public TableAssertions checkedRowsFooter() {
        int checkedRows = actions.getCheckedRows().size();
        int[] counts = actions.getSelectedRowsAndTotalCounts();

        if (counts[0] > counts[1]) {
            throw new AssertionError("Selected count exceeds total: " + counts[0] + " of " + counts[1]);
        }
        if (counts[0] != checkedRows) {
            throw new AssertionError("Summary total (" + counts[1] + ") does not match rendered row count (" + checkedRows + ")");
        }
        return this;
    }

    public TableAssertions checkboxesAreSelected(List<String> cells) {
        for (String cell : cells) {
            checkboxesAreSelected(cell);
        }
        assertNotEquals("unchecked", actions.headerActions().getHeaderCheckbox().getAttribute("data-state").toLowerCase());

        return this;
    }

    public TableAssertions groupsAreContiguous(HeaderColumnOption groupColumn) {
        List<Map<String, String>> rows = actions.getAllRowsData();
        String columnKey = groupColumn.label();

        Set<String> seenGroups = new HashSet<>();
        String currentGroup = null;

        for (Map<String, String> row : rows) {
            String group = row.get(columnKey);

            if (!group.equals(currentGroup)) {
                assertFalse(seenGroups.contains(group),
                        "Category '" + group + "' appears in multiple separate blocks");
                seenGroups.add(group);
                currentGroup = group;
            }
        }
        return this;
    }

    public TableAssertions columnValuesEqual(HeaderColumnOption option, List<String> expected) {
        List<String> actual = actions.getCellsByColumn(option);
        assertEquals(expected, actual, "Mismatch for column: " + option.label());
        return this;
    }

    public TableAssertions cellsByColumnIsSorted(HeaderColumnOption option, SortingOption sortingOption) {
        actions.headerActions().setHeaderDropdownOption(option, sortingOption);
        List<String> actual = actions.getCellsByColumn(option);

        Comparator<String> comparator = sortingOption == SortingOption.ASC
                ? Comparator.naturalOrder() : Comparator.reverseOrder();

        List<String> expected = new ArrayList<>(actual);
        expected.sort(comparator);

        assertEquals(expected, actual);
        return this;
    }

    public TableAssertions cellDisplayedInTree(String cell) {
        assertTrue(actions.isCellDisplayedInTree(cell));
        return this;
    }

    public TableAssertions paginationDefaultState() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertFalse(actions.paginationActions().getFirstPageBtn().isEnabled());
        softAssert.assertFalse(actions.paginationActions().getPreviousPageBtn().isEnabled());
        softAssert.assertTrue(actions.paginationActions().getNextPageBtn().isEnabled());
        softAssert.assertTrue(actions.paginationActions().getLastPageBtn().isEnabled());
        softAssert.assertEquals(actions.paginationActions().getCurrentPageBtn().getText().trim(), "1",
                "Page 1 should be selected");

        softAssert.assertAll(); // MUST call this, or nothing actually fails
        return this;
    }

    public TableAssertions rowsDisplayedEachPage(List<TableRecord> expectedData) {
        int expectedTotal = expectedData.size();
        int numberOfPages = paginationActions().getListPageBtn().size();
        int expectedRows = expectedTotal/numberOfPages;
        int remainder = expectedTotal % numberOfPages;

        List<WebElement> buttons = actions.paginationActions().getListPageBtn();
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).click();
            int expected = (i == buttons.size() - 1 && remainder != 0) ? remainder : expectedRows;

            assertEquals(expected, actions.getRows().size());
        }

        return this;
    }

    public TableAssertions rowContentEachPage(List<TableRecord> expectedData) {
        paginationActions().backToFirstPage();

        int numberOfPages = paginationActions().getListPageBtn().size();
        for (int page = 0; page < numberOfPages; page++) {
            List<TableRecord> expectedSlice = paginationActions().sliceForPage(expectedData, page, numberOfPages);
            paginationActions().getListPageBtn().get(page).click();
            List<Map<String, String>> actual = actions.getAllRowsData();

            TableRecordNormalizer.verify(expectedSlice, actual);
        }
        return this;
    }

    public TableAssertions pinnedRowsOrder(List<String> expected) {
        List<String> actual = actions.getPinnedRowsData().stream()
                .map(r -> r.get(HeaderColumnOption.EMAIL.label())).toList();

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            String actualValue = actual.get(i);

            assertEquals(expected.get(i), actualValue);
        }

        return this;
    }

    public TableAssertions unpinnedRowsByCells(String... expected) {
        List<String> actual = actions.getUnpinnedRowsData().stream()
                .map(r -> r.get(HeaderColumnOption.EMAIL.label())).toList();
        assertTrue(new HashSet<>(actual).containsAll(List.of(expected)));
        return this;
    }

    public TableActions and() {
        return actions;
    }
}
