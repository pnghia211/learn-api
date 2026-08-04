package assertions;

import actions.TableActions;
import actions.TableActions.HeaderColumnOption;
import component.main.TableComp;
import model.TableRecord;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TableAssertions {
    private final TableComp parent;
    private final TableActions actions;

    public TableAssertions(TableComp parent, TableActions actions) {
        this.parent = parent;
        this.actions = actions;
    }

    public TableAssertions cellIsDisplayed(String cell) {
        assertTrue("Element is not displayed!!!", actions.isCellDisplayed(cell));
        return this;
    }

    public TableAssertions headerColumnNotDisplayed(HeaderColumnOption option) {
        List<String> actual = new ArrayList<>(actions.getHeadersMap().keySet());
        assertFalse(actual.contains(option.headerLabel()));
        return this;
    }

    public TableAssertions cellsByColumnHeader(HeaderColumnOption option, List<String> expected) {
        List<String> actual = new ArrayList<>(actions.getCellsByColumn(option.headerLabel()));
        assertEquals(expected, actual);
        return this;
    }

    public TableAssertions columnCellsNotDisplayed(HeaderColumnOption column) {
        List<String> cells = actions.getCellsByColumn(column.headerLabel());
        assertTrue("Expected no cells for hidden column: " + column.headerLabel(), cells.isEmpty());
        return this;
    }

    public TableAssertions rowsDisplayed(List<TableRecord> expected) {
        List<TableRecord> actual = actions.rowsToRecords();
        assertEquals(expected, actual);
        return this;
    }

    public TableAssertions copyNotificationPopupDisplayed() {
        assertTrue(actions.getCopyNotificationPopup().isDisplayed());
        return this;
    }

    public TableActions and() {
        return actions;
    }
}
