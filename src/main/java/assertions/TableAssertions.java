package assertions;

import actions.TableActions;
import data.HeaderColumnOption;
import data.SortingOption;
import helpers.TableRecordNormalizer;
import model.TableRecord;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TableAssertions {
    private final TableActions actions;

    public TableAssertions(TableActions actions) {
        this.actions = actions;
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
        assertTrue("Expected no cells for hidden column: " + column.label(), cells.isEmpty());
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
        assertTrue("true", actions.isCellDisplayed(cell));
        return this;
    }

    public TableAssertions copyNotificationPopupDisplayed() {
        assertTrue(actions.getCopyNotificationPopup().isDisplayed());
        return this;
    }

    public TableAssertions checkboxIsSelected(String cell) {
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
            checkboxIsSelected(cell);
        }
        assertFalse("unchecked".equalsIgnoreCase(actions.headerActions().getHeaderCheckbox().getAttribute("data-state")));

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
        assertEquals("Mismatch for column: " + option.label(), expected, actual);
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

    public TableActions and() {
        return actions;
    }
}
