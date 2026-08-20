package test;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.table.TableFactory;
import data.*;
import driver.DriverFactory;
import model.TableRecord;
import org.openqa.selenium.WebDriver;
import page.HomePage;

import java.util.List;

import static helpers.TestDataLoader.loadExpectedTableData;
import static url.Url.mainPage;

public class TestTable {
    private final static WebDriver driver = DriverFactory.getChromeDriver();

    public static void main(String[] args) throws InterruptedException {
        driver.get(mainPage);

        String relativePathUsageTable = "testdata/table-usage.json";
        String relativePathColumnVisibility = "testdata/table-column-visibility.json";
        String relativePathPagination = "testdata/table-pagination.json";
        HomePage homePage = new HomePage(driver);
        HeaderComp headerComp = homePage.componentsSection();
        LeftNavigatorComp leftNavigatorComp = homePage.leftNavigatorComp();
        TableFactory tableFactory = homePage.tableComp();

        headerComp.clickComponentsComp();
        leftNavigatorComp.clickDataTableComp("table");

        tableFactory.forTable(TableLabel.WITH_INFINITE_SCROLL)
                .scrollTillCellDisplayed("ariamx")
                .verify().cellDisplayed("ariamx");

        List<TableRecord> usageExpectedRows = loadExpectedTableData(relativePathUsageTable);
        List<TableRecord> visibilityColumnExpectedRows = loadExpectedTableData(relativePathColumnVisibility);
        List<String> expectedEmails = usageExpectedRows.stream().map(TableRecord::email).toList();
        TableRecord expectedRow = usageExpectedRows.stream().filter(r -> r.id().equalsIgnoreCase("4598")).toList().get(0);

        tableFactory.forTable(TableLabel.USAGE)
                .verify().cellDisplayed("mia.white@example.com")
                .and().headerActions().unselectDropdownOption(DropdownOption.EMAIL)
                .and().verify().cellsByColumnNotDisplayed(HeaderColumnOption.EMAIL)
                .and().headerActions().selectDropdownOption(DropdownOption.EMAIL).and()
                .verify().cellsByColumnDisplayed(HeaderColumnOption.EMAIL, expectedEmails);

        tableFactory.forTable(TableLabel.WITH_COLUMN_VISIBILITY)
                .headerActions().unselectDropdownOption(DropdownOption.AMOUNT)
                .and().verify().headerColumnNotDisplayed(HeaderColumnOption.AMOUNT)
                .cellsByColumnNotDisplayed(HeaderColumnOption.AMOUNT)
                .and().headerActions().selectDropdownOption(DropdownOption.AMOUNT);

        tableFactory.forTable(TableLabel.WITH_ROW_ACTIONS)
                .clickActionButton("#4597")
                .selectCopyPaymentIdOpt(RowActionOption.COPY_PAYMENT)
                .verify().copyNotificationPopupDisplayed();

        tableFactory.forTable(TableLabel.USAGE).verify().rowsByTableDisplayed(usageExpectedRows);
        tableFactory.forTable(TableLabel.WITH_COLUMN_VISIBILITY).verify().rowsByTableDisplayed(visibilityColumnExpectedRows);
        tableFactory.forTable(TableLabel.USAGE).verify().rowByCelDisplayed(expectedRow, "#4598");

        List<String> checkedRowsSelection = List.of("paid", "william.brown@example.com");
        tableFactory.forTable(TableLabel.WITH_ROW_SELECTION).headerActions()
                .setAllSelectionHeaderToDefaultState()
                .and().selectCheckboxesByCells(checkedRowsSelection)
                .verify().checkboxesAreSelected(checkedRowsSelection)
                .checkedRowsFooter();

        List<String> checkedRowsUsage = List.of("evelyn.green@example.com", "mia.white@example.com", "noah.clark@example.com");
        tableFactory.forTable(TableLabel.USAGE).selectCheckboxesByCells(checkedRowsUsage).verify().checkedRowsFooter();

        tableFactory.forTable(TableLabel.WITH_COLUMN_FOOTER).verify().footerTotalAmount();

        List<String> names = List.of("Laptop", "Phone", "Tablet", "T-Shirt", "Jeans");
        tableFactory.forTable(TableLabel.WITH_COLUMN_SPAN).verify().groupsAreContiguous(HeaderColumnOption.CATEGORY)
                .columnValuesEqual(HeaderColumnOption.NAME, names);

        tableFactory.forTable(TableLabel.WITH_COLUMN_SORTING, TableIndexOption.SECONDARY)
                .headerActions().and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.ID, SortingOption.ASC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.ID, SortingOption.DESC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.EMAIL, SortingOption.DESC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.AMOUNT, SortingOption.ASC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.STATUS, SortingOption.DESC);

        List<TableRecord> paginationList = loadExpectedTableData(relativePathPagination);

        tableFactory.forTable(TableLabel.WITH_PAGINATION).verify().paginationDefaultState()
                .rowsDisplayedEachPage(paginationList)
                .rowContentEachPage(paginationList);

        List<String> rowToPin = List.of("emma.davis@example.com", "benjamin.jackson@example.com", "ava.thomas@example.com");
        tableFactory.forTable(TableLabel.WITH_ROW_PINNING).verify()
                .pinnedRowsOrder(List.of("mia.white@example.com", "emma.davis@example.com"))
                .and().unpinAllRows()
                .pinRowsByCells(rowToPin)
                .verify().pinnedRowsOrder(rowToPin).unpinnedRowsByCells("mia.white@example.com");

        tableFactory.forTable(TableLabel.WITH_TREE_DATA)
                .expandUntilCellDisplayed("4595")
                .verify().cellDisplayed("4595");

        tableFactory.forTable(TableLabel.WITH_GROUPED_ROWS)
                .expandUntilCellDisplayed("emma.davis@example.com")
                .verify().cellDisplayed("emma.davis@example.com");

        Thread.sleep(5000);
        driver.quit();
    }
}
