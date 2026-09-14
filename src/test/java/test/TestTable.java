package test;

import component.main.factory.TableFactory;
import data.*;
import model.TableRecord;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.Base.BaseTest;

import java.util.List;

import static helpers.TestDataLoader.loadList;

public class TestTable extends BaseTest {
    private TableFactory tableFactory() {
        return homePage().tableComp();
    }

    private static final String USAGE_TABLE_PATH = "testdata/table-usage.json";
    private static final String COLUMN_VISIBILITY_PATH = "testdata/table-column-visibility.json";
    private static final String PAGINATION_PATH = "testdata/table-pagination.json";

    @BeforeClass
    public void setUp() {
        leftNavigatorComp().clickDataTableComp("table");
    }

    @Test
    public void infiniteScroll_scrollsUntilCellDisplayed() {
        tableFactory().forTable(TableLabel.WITH_INFINITE_SCROLL)
                .scrollTillCellDisplayed("ariamx")
                .verify().cellDisplayed("ariamx");
    }

    @Test
    public void usageTable_toggleEmailColumnVisibility() {
        List<TableRecord> usageExpectedRows = loadList(USAGE_TABLE_PATH, TableRecord.class);
        List<String> expectedEmails = usageExpectedRows.stream().map(TableRecord::email).toList();

        tableFactory().forTable(TableLabel.USAGE)
                .verify().cellDisplayed("mia.white@example.com")
                .and().toolbarActions().unselectDropdownOption(DropdownOption.EMAIL)
                .and().verify().cellsByColumnNotDisplayed(HeaderColumnOption.EMAIL)
                .and().toolbarActions().selectDropdownOption(DropdownOption.EMAIL).and()
                .verify().cellsByColumnDisplayed(HeaderColumnOption.EMAIL, expectedEmails);
    }

    @Test
    public void columnVisibilityTable_toggleAmountColumn() {
        tableFactory().forTable(TableLabel.WITH_COLUMN_VISIBILITY)
                .toolbarActions().unselectDropdownOption(DropdownOption.AMOUNT)
                .and().verify().headerColumnNotDisplayed(HeaderColumnOption.AMOUNT)
                .cellsByColumnNotDisplayed(HeaderColumnOption.AMOUNT)
                .and().toolbarActions().selectDropdownOption(DropdownOption.AMOUNT);
    }

    @Test
    public void rowActions_copyPaymentIdShowsNotification() {
        tableFactory().forTable(TableLabel.WITH_ROW_ACTIONS)
                .clickActionButton("#4597")
                .selectCopyPaymentIdOpt(RowActionOption.COPY_PAYMENT)
                .verify().copyNotificationPopupDisplayed();
    }

    @Test
    public void usageTable_rowsMatchExpectedData() {
        List<TableRecord> usageExpectedRows = loadList(USAGE_TABLE_PATH, TableRecord.class);
        tableFactory().forTable(TableLabel.USAGE).verify().rowsByTableDisplayed(usageExpectedRows);
    }

    @Test
    public void columnVisibilityTable_rowsMatchExpectedData() {
        List<TableRecord> visibilityColumnExpectedRows = loadList(COLUMN_VISIBILITY_PATH, TableRecord.class);
        tableFactory().forTable(TableLabel.WITH_COLUMN_VISIBILITY).verify().rowsByTableDisplayed(visibilityColumnExpectedRows);
    }

    @Test
    public void usageTable_specificRowMatchesExpectedCell() {
        List<TableRecord> usageExpectedRows = loadList(USAGE_TABLE_PATH, TableRecord.class);
        TableRecord expectedRow = usageExpectedRows.stream()
                .filter(r -> r.id().equalsIgnoreCase("4598")).toList().get(0);
        tableFactory().forTable(TableLabel.USAGE).verify().rowByCelDisplayed(expectedRow, "#4598");
    }

    @Test
    public void rowSelectionTable_checkboxesSelectAndFooterUpdates() {
        List<String> checkedRowsSelection = List.of("paid", "william.brown@example.com");
        tableFactory().forTable(TableLabel.WITH_ROW_SELECTION).toolbarActions()
                .setAllSelectionHeaderToDefaultState()
                .and().selectCheckboxesByCells(checkedRowsSelection)
                .verify().checkboxesAreSelected(checkedRowsSelection)
                .checkedRowsFooter();
    }

    @Test
    public void usageTable_checkboxesSelectAndFooterUpdates() {
        List<String> checkedRowsUsage = List.of("evelyn.green@example.com", "mia.white@example.com", "noah.clark@example.com");
        tableFactory().forTable(TableLabel.USAGE).selectCheckboxesByCells(checkedRowsUsage).verify().checkedRowsFooter();
    }

    @Test
    public void columnFooterTable_totalAmountDisplayed() {
        tableFactory().forTable(TableLabel.WITH_COLUMN_FOOTER).verify().footerTotalAmount();
    }

    @Test
    public void columnSpanTable_groupsContiguousAndNamesMatch() {
        List<String> names = List.of("Laptop", "Phone", "Tablet", "T-Shirt", "Jeans");
        tableFactory().forTable(TableLabel.WITH_COLUMN_SPAN).verify().groupsAreContiguous(HeaderColumnOption.CATEGORY)
                .columnValuesEqual(HeaderColumnOption.NAME, names);
    }

    @Test
    public void columnSortingTable_sortsEachColumnCorrectly() {
        tableFactory().forTable(TableLabel.WITH_COLUMN_SORTING, ComponentIndexOption.SECONDARY)
                .toolbarActions().and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.ID, SortingOption.ASC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.ID, SortingOption.DESC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.EMAIL, SortingOption.DESC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.AMOUNT, SortingOption.ASC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.STATUS, SortingOption.DESC);
    }

    @Test
    public void paginationTable_defaultStateAndRowContentPerPage() {
        List<TableRecord> paginationList = loadList(PAGINATION_PATH, TableRecord.class);
        tableFactory().forTable(TableLabel.WITH_PAGINATION).verify().paginationDefaultState()
                .rowsDisplayedEachPage(paginationList)
                .rowContentEachPage(paginationList);
    }

    @Test
    public void rowPinningTable_pinAndUnpinRows() {
        List<String> rowToPin = List.of("emma.davis@example.com", "benjamin.jackson@example.com", "ava.thomas@example.com");
        tableFactory().forTable(TableLabel.WITH_ROW_PINNING).verify()
                .pinnedRowsOrder(List.of("mia.white@example.com", "emma.davis@example.com"))
                .and().unpinAllRows()
                .pinRowsByCells(rowToPin)
                .verify().pinnedRowsOrder(rowToPin).unpinnedRowsByCells("mia.white@example.com");
    }

    @Test
    public void treeDataTable_expandsUntilCellDisplayed() {
        tableFactory().forTable(TableLabel.WITH_TREE_DATA)
                .expandUntilCellDisplayed("4595")
                .verify().cellDisplayed("4595");
    }

    @Test
    public void groupedRowsTable_expandsUntilCellDisplayed() {
        tableFactory().forTable(TableLabel.WITH_GROUPED_ROWS)
                .expandUntilCellDisplayed("emma.davis@example.com")
                .verify().cellDisplayed("emma.davis@example.com");
    }

    @Test
    public void filterTable_expandsUntilCellDisplayed() {
        tableFactory().forTable(TableLabel.WITH_GLOBAL_FILTER)
                .filterWithValue("11").verify().expectedFilterRows("11");
    }
}