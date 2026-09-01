package test;

import actions.CalendarActions.CalendarView;
import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.calendar.CalendarComp;
import component.main.factory.CalendarFactory;
import data.CalendarLabel;
import data.CalendarTestData;
import driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import page.HomePage;

import java.util.List;

import static url.Url.mainPage;

public class TestCalendar {
    private WebDriver driver;
    private CalendarFactory calendarComp;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.getChromeDriver();
        driver.get(mainPage);

        HomePage homePage = new HomePage(driver);
        HeaderComp headerComp = homePage.componentsSection();
        LeftNavigatorComp leftNavigatorComp = homePage.leftNavigatorComp();
        calendarComp = homePage.calenderComp();

        headerComp.clickComponentsComp();
        leftNavigatorComp.clickDataTableComp("calendar");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void usageCalendar_selectsDateAndUpdatesSelection() {
        calendarComp.forCalendar(CalendarLabel.USAGE)
                .verify().dateIsSelected(CalendarTestData.DEFAULT_DATE)
                .and().selectDate("2022-02-04")
                .verify().dateIsSelected("2022-02-04")
                .dateIsNotSelected(CalendarTestData.DEFAULT_DATE);
    }

    @Test
    public void disabledDates_cannotBeSelected() {
        calendarComp.forCalendar(CalendarLabel.DISABLED_DATES)
                .selectDate(CalendarTestData.DISABLED_DATE)
                .verify().dateIsDisabled(CalendarTestData.DISABLED_DATE);
    }

    @Test
    public void usageCalendar_navigatesMonthAndYear() {
        calendarComp.forCalendar(CalendarLabel.USAGE)
                .selectNextMonth()
                .verify().heading("2022-03")
                .and().selectNextYear()
                .verify().heading("2023-03");
    }

    @Test
    public void usageCalendar_switchesViewGrids() {
        calendarComp.forCalendar(CalendarLabel.USAGE)
                .selectViewGrid(CalendarView.YEAR)
                .selectViewGrid(CalendarView.DECADE)
                .selectViewGrid(CalendarView.MONTH);
    }

    @Test
    public void multipleDates_selectsAllDates() {
        List<String> dates = List.of("2022-02-04", "2022-02-16", "2022-02-23");
        calendarComp.forCalendar(CalendarLabel.MULTIPLES)
                .selectMultipleDates(dates)
                .verify().datesAreSelected(dates);
    }

    @Test
    public void dateRange_selectsStartAndEnd() {
        String startDate = "2022-02-10";
        String endDate = "2022-02-02";
        calendarComp.forCalendar(CalendarLabel.RANGE)
                .selectDateRange(startDate, endDate)
                .verify().dateRangeSelected(startDate, endDate);
    }

    @Test
    public void usageCalendar_selectDateWithNavigation() {
        calendarComp.forCalendar(CalendarLabel.USAGE)
                .selectDateWithNavigation("2016-01-01")
                .verify().heading("2016-01").dateIsSelected("2016-01-01");
    }

    @Test
    public void datePicker_selectDateWithNavigation() {
        calendarComp.forDatePicker(CalendarLabel.DATE_PICKER)
                .selectDateWithNavigation("2024-05-04")
                .verify().heading("2024-05")
                .and().verify().dateIsSelected("2024-05-04")
                .datePickerHeading("2024-05-04");
    }

    @Test
    public void dateRangePicker_selectsRangeAndShowsPresets() {
        String firstDateSelected = "2030-12-30";
        String secondDateSelected = "2020-12-28";
        List<String> expectedPresets = List.of(
                "Last 7 days",
                "Last 14 days",
                "Last 30 days",
                "Last 3 months",
                "Last 6 months",
                "Last year");

        calendarComp.forDatePicker(CalendarLabel.DATE_RANGE_PICKER)
                .selectDateWithNavigation(firstDateSelected)
                .selectDateWithNavigation(secondDateSelected)
                .verify().datePickerHeading(firstDateSelected + " - " + secondDateSelected)
                .presetRangesDisplayed(expectedPresets);
    }
}
