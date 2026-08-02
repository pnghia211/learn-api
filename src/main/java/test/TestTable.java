package test;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.TableComp;
import driver.DriverFactory;
import helpers.DateHelper;
import helpers.TableRecordNormalizer;
import helpers.TestDataLoader;
import model.TableRecord;
import org.openqa.selenium.WebDriver;
import page.HomePage;

import static url.Url.mainPage;

import actions.TableActions.*;

import java.util.List;

public class TestTable {
    private final static WebDriver driver = DriverFactory.getChromeDriver();

    public static void main(String[] args) throws InterruptedException {
        driver.get(mainPage);

        String relativePathUsage = "testdata/table-usage.json";
        HomePage homePage = new HomePage(driver);
        HeaderComp headerComp = homePage.componentsSection();
        LeftNavigatorComp leftNavigatorComp = homePage.leftNavigatorComp();
        TableComp tableComp = homePage.tableComp();

        headerComp.clickComponentsComp();
        leftNavigatorComp.clickDataTableComp("table");

        tableComp.forTable("with-infinite-scroll")
                .scrollTillCelDisplayed("ariamx")
                .verify().cellIsDisplayed("ariamx");

        List<TableRecord> expected = TestDataLoader.loadExpectedTableData(relativePathUsage).stream()
                .map(TableRecordNormalizer::normalizeExpected)
                .toList();
        List<String> expectedEmails = expected.stream().map(TableRecord::email).toList();

        tableComp.forTable("usage")
                .verify().rowsDisplayed(expected).and()
                .unselectDropdownOption(ColumnOption.AMOUNT)
                .verify().cellsByColumnHeader(ColumnOption.EMAIL, expectedEmails);

        tableComp.forTable("with-column-visibility")
                .unselectDropdownOption(ColumnOption.AMOUNT)
                .verify().headerColumnNotDisplayed(ColumnOption.AMOUNT)
                .columnCellsNotDisplayed(ColumnOption.AMOUNT);

        Thread.sleep(5000);
        driver.quit();
    }
}
