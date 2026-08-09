package test;

import actions.TableActions.ActionOption;
import actions.TableActions.HeaderColumnOption;
import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.table.TableComp;
import driver.DriverFactory;
import helpers.TestDataLoader;
import model.TableRecord;
import org.openqa.selenium.WebDriver;
import page.HomePage;

import java.util.List;

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
        TableComp tableComp = homePage.tableComp();

        headerComp.clickComponentsComp();
        leftNavigatorComp.clickDataTableComp("table");

        tableComp.forTable("with-infinite-scroll")
                .scrollTillCelDisplayed("ariamx")
                .verify().cellDisplayed("ariamx");

        List<TableRecord> usageExpectedRows = TestDataLoader.loadExpectedTableData(relativePathUsageTable);
        List<TableRecord> visibilityColumnExpectedRows = TestDataLoader.loadExpectedTableData(relativePathColumnVisibility);
        List<String> expectedEmails = usageExpectedRows.stream().map(TableRecord::email).toList();
        TableRecord expectedRow = usageExpectedRows.stream().filter(r -> r.id().equalsIgnoreCase("4598")).toList().get(0);

        tableComp.forTable("usage")
                .verify().cellDisplayed("mia.white@example.com").and()
                .unselectDropdownOption(HeaderColumnOption.EMAIL)
                .verify().cellsByColumnNotDisplayed(HeaderColumnOption.EMAIL)
                .and().selectDropdownOption(HeaderColumnOption.EMAIL)
                .verify().cellsByColumnDisplayed(HeaderColumnOption.EMAIL, expectedEmails);

        tableComp.forTable("with-column-visibility")
                .unselectDropdownOption(HeaderColumnOption.AMOUNT)
                .verify().headerColumnNotDisplayed(HeaderColumnOption.AMOUNT)
                .cellsByColumnNotDisplayed(HeaderColumnOption.AMOUNT)
                .and().selectDropdownOption(HeaderColumnOption.AMOUNT);

        tableComp.forTable("with-row-actions")
                .clickActionButton("#4597")
                .selectCopyPaymentIdOpt(ActionOption.COPY_PAYMENT)
                .verify().copyNotificationPopupDisplayed();

        tableComp.forTable("usage").verify().rowsByTableDisplayed(usageExpectedRows);
        tableComp.forTable("with-column-visibility").verify().rowsByTableDisplayed(visibilityColumnExpectedRows);
        tableComp.forTable("usage").verify().rowByCelDisplayed(expectedRow, "#4598");

        List<String> checkedRowsSelection = List.of("paid", "william.brown@example.com");
        tableComp.forTable("with-row-selection")
                .setAllSelectionHeaderToDefaultState()
                .selectCheckboxesByCells(checkedRowsSelection)
                .verify().checkboxesAreSelected(checkedRowsSelection)
                .checkedRowsFooter();

        List<String> checkedRowsUsage = List.of("evelyn.green@example.com", "mia.white@example.com", "noah.clark@example.com");
        tableComp.forTable("usage").selectCheckboxesByCells(checkedRowsUsage).verify().checkedRowsFooter();

        tableComp.forTable("with-column-footer").verify().totalAmount();

        Thread.sleep(5000);
        driver.quit();
    }
}
