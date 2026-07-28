package test;

import actions.CalendarActions.CalendarView;
import component.main.CalendarComp;
import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.TableComp;
import data.CalendarTestData;
import driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import page.HomePage;

import java.util.List;

import static url.Url.mainPage;

public class TestCalendar {
    private final static WebDriver driver = DriverFactory.getChromeDriver();

    public static void main(String[] args) throws InterruptedException {
        driver.get(mainPage);

        HomePage homePage = new HomePage(driver);
        HeaderComp headerComp = homePage.componentsSection();
        LeftNavigatorComp leftNavigatorComp = homePage.leftNavigatorComp();
        TableComp tableComp = homePage.tableComp();
        CalendarComp calendarComp = homePage.calenderComp();

        headerComp.clickComponentsComp();
//        leftNavigatorComp.clickDataTableComp("table");
//        tableComp.getTableBasedOnHeader("with-infinite-scroll");
//        tableComp.scrollTillCelDisplayed("with-infinite-scroll", "Brandon");

        leftNavigatorComp.clickDataTableComp("calendar");
        calendarComp.forCalendar("usage")
                .verify().dateIsSelected(CalendarTestData.DEFAULT_DATE)
                .and().selectDate("2022-02-04")
                .verify().dateIsSelected("2022-02-04")
                .dateIsNotSelected(CalendarTestData.DEFAULT_DATE);

        calendarComp.forCalendar("with-disabled-dates")
                .selectDate(CalendarTestData.DISABLED_DATE)
                .verify().dateIsDisabled(CalendarTestData.DISABLED_DATE);

        calendarComp.forCalendar("usage")
                .selectNextMonth()
                .verify().heading("2022-03")
                .and().selectNextYear()
                .verify().heading("2023-03");

        calendarComp.forCalendar("usage")
                .selectViewGrid(CalendarView.YEAR)
                .selectViewGrid(CalendarView.DECADE)
                .selectViewGrid(CalendarView.MONTH);

        List<String> dates = List.of("2022-02-04", "2022-02-16", "2022-02-23");
        calendarComp.forCalendar("multiple")
                .selectMultipleDates(dates)
                .verify().datesAreSelected(dates);

        String startDate = "2022-02-10";
        String endDate = "2022-02-02";
        calendarComp.forCalendar("range")
                .selectDateRange(startDate, endDate)
                .verify().dateRangeSelected(startDate, endDate);

        calendarComp.forCalendar("usage")
                .selectDateWithNavigation("2016-01-01");

        calendarComp.forDatePicker("as-a-date-picker")
                .selectDateWithNavigation("2024-05-04");


        Thread.sleep(5000);
        driver.quit();
    }
}
