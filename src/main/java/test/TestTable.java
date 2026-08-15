package test;

import actions.TableActions.*;
import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.table.TableFactory;
import data.DropdownOption;
import data.HeaderColumnOption;
import data.SortingOption;
import data.TableIndexOption;
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
        HomePage homePage = new HomePage(driver);
        HeaderComp headerComp = homePage.componentsSection();
        LeftNavigatorComp leftNavigatorComp = homePage.leftNavigatorComp();
        TableFactory tableFactory = homePage.tableComp();

        headerComp.clickComponentsComp();
        leftNavigatorComp.clickDataTableComp("table");

//        tableFactory.forTable("with-infinite-scroll")
//                .scrollTillCellDisplayed("ariamx")
//                .verify().cellDisplayed("ariamx");

        List<TableRecord> usageExpectedRows = loadExpectedTableData(relativePathUsageTable);
        List<TableRecord> visibilityColumnExpectedRows = loadExpectedTableData(relativePathColumnVisibility);
        List<String> expectedEmails = usageExpectedRows.stream().map(TableRecord::email).toList();
        TableRecord expectedRow = usageExpectedRows.stream().filter(r -> r.id().equalsIgnoreCase("4598")).toList().get(0);

        tableFactory.forTable("usage")
                .verify().cellDisplayed("mia.white@example.com")
                .and().headerActions().unselectDropdownOption(DropdownOption.EMAIL)
                .and().verify().cellsByColumnNotDisplayed(HeaderColumnOption.EMAIL)
                .and().headerActions().selectDropdownOption(DropdownOption.EMAIL).and()
                .verify().cellsByColumnDisplayed(HeaderColumnOption.EMAIL, expectedEmails);

        tableFactory.forTable("with-column-visibility")
                .headerActions().unselectDropdownOption(DropdownOption.AMOUNT)
                .and().verify().headerColumnNotDisplayed(HeaderColumnOption.AMOUNT)
                .cellsByColumnNotDisplayed(HeaderColumnOption.AMOUNT)
                .and().headerActions().selectDropdownOption(DropdownOption.AMOUNT);

        tableFactory.forTable("with-row-actions")
                .clickActionButton("#4597")
                .selectCopyPaymentIdOpt(ActionOption.COPY_PAYMENT)
                .verify().copyNotificationPopupDisplayed();

        tableFactory.forTable("usage").verify().rowsByTableDisplayed(usageExpectedRows);
        tableFactory.forTable("with-column-visibility").verify().rowsByTableDisplayed(visibilityColumnExpectedRows);
        tableFactory.forTable("usage").verify().rowByCelDisplayed(expectedRow, "#4598");

        List<String> checkedRowsSelection = List.of("paid", "william.brown@example.com");
        tableFactory.forTable("with-row-selection").headerActions()
                .setAllSelectionHeaderToDefaultState()
                .and().selectCheckboxesByCells(checkedRowsSelection)
                .verify().checkboxesAreSelected(checkedRowsSelection)
                .checkedRowsFooter();

        List<String> checkedRowsUsage = List.of("evelyn.green@example.com", "mia.white@example.com", "noah.clark@example.com");
        tableFactory.forTable("usage").selectCheckboxesByCells(checkedRowsUsage).verify().checkedRowsFooter();

        tableFactory.forTable("with-column-footer").verify().footerTotalAmount();

        List<String> names = List.of("Laptop", "Phone", "Tablet", "T-Shirt", "Jeans");
        tableFactory.forTable("with-column-span").verify().groupsAreContiguous(HeaderColumnOption.CATEGORY)
                .columnValuesEqual(HeaderColumnOption.NAME, names);

        tableFactory.forTable("with-column-sorting", TableIndexOption.SECONDARY)
                .headerActions().and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.ID, SortingOption.ASC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.ID, SortingOption.DESC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.EMAIL, SortingOption.DESC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.AMOUNT, SortingOption.ASC).and()
                .verify().cellsByColumnIsSorted(HeaderColumnOption.STATUS, SortingOption.DESC);

        Thread.sleep(5000);
        driver.quit();
    }
}
