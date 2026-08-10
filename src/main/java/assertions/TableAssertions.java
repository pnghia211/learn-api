package assertions;

import actions.TableActions;
import data.HeaderColumnOption;
import helpers.TableRecordNormalizer;
import model.TableRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TableAssertions {
    private final TableActions actions;

    public TableAssertions(TableActions actions) {
        this.actions = actions;
    }

    public TableAssertions headerColumnNotDisplayed(HeaderColumnOption option) {
        List<String> actual = new ArrayList<>(actions.headerActions().getHeadersMap().keySet());
        assertFalse(actual.contains(option.headerLabel()));
        return this;
    }

    public TableAssertions cellsByColumnDisplayed(HeaderColumnOption option, List<String> expected) {
        List<String> actual = new ArrayList<>(actions.getCellsByColumn(option.headerLabel()));
        assertEquals(expected, actual);
        return this;
    }

    public TableAssertions cellsByColumnNotDisplayed(HeaderColumnOption column) {
        List<String> cells = actions.getCellsByColumn(column.headerLabel());
        assertTrue("Expected no cells for hidden column: " + column.headerLabel(), cells.isEmpty());
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


    public TableActions and() {
        return actions;
    }
}
