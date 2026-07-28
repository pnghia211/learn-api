package actions;

import component.constract.CalendarRootLocator;
import component.main.CalendarComp;
import helpers.CalendarAssertions;
import helpers.DateHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class CalendarActions {
    private final CalendarRootLocator rootLocator;
    private static final Pattern MONTH_PATTERN = Pattern.compile("^[A-Z][a-z]+ \\d{4}$"); // "February 2022"
    private static final Pattern YEAR_PATTERN = Pattern.compile("^\\d{4}$"); // "2022"
    private static final Pattern DECADE_PATTERN = Pattern.compile("^\\d{4}\\s-\\s\\d{4}$"); // "2020-2031"
    private String calendarId;
    private CalendarComp parent;

    public CalendarActions(CalendarComp parent, CalendarRootLocator rootLocator) {
        this.parent = parent;
        this.rootLocator = rootLocator;
    }

    public CalendarAssertions verify(){
        return new CalendarAssertions(parent, rootLocator, this);
    }

    private WebElement getDateCell(String dateValue) {
        return parent.getDateCell(rootLocator, dateValue);
    }

    private WebElement getHeadingEle() {
        return parent.getHeadingEle(rootLocator);
    }

    private boolean isDateSelected(String dateValue) {
        String result = getDateCell(dateValue).getAttribute("data-selected");
        return "true".equalsIgnoreCase(result);
    }

    public CalendarActions selectNextMonth() {
        parent.getNextMonthBtn(rootLocator).click();
        return this;
    }

    public CalendarActions selectNextYear() {
        parent.getNextYearBtn(rootLocator).click();
        return this;
    }

    public CalendarActions selectViewGrid(CalendarView view) {
        WebElement headingEle = parent.getHeadingEle(rootLocator);
        headingEle.click();

        String headingText = parent.getHeadingEle(rootLocator).getText();

        switch (view) {
            case MONTH:
                assertTrue(MONTH_PATTERN.matcher(headingText).matches());
                break;
            case YEAR:
                assertTrue(YEAR_PATTERN.matcher(headingText).matches());
                break;
            case DECADE:
                assertTrue(DECADE_PATTERN.matcher(headingText).matches());
                break;
        }

        return this;
    }

    public CalendarActions selectDate(String dateValue) {
        WebElement dateCell = getDateCell(dateValue);
        dateCell.click();
        return this;
    }

    public CalendarActions selectMultipleDates(List<String> dates) {
        dates.forEach(date -> {
            WebElement dateCell = getDateCell(date);
            if (!isDateSelected(date)) {
                dateCell.click();
            }
        });
        return this;
    }

    public CalendarActions selectDateRange(String startDate, String endDate) {
        selectDate(startDate);
        selectDate(endDate);
        return this;
    }

    public CalendarActions selectDateWithNavigation(String dateValue) {
        LocalDate target = LocalDate.parse(dateValue);

        navigateToYear(target);
        navigateToMonth(target);
        selectDate(dateValue);

        return this;
    }

    private void navigateToYear(LocalDate target) {
        int currentYear = getCurrentYear();
        int targetYear = target.getYear();
        boolean forward = targetYear > currentYear;
        WebElement navBtnEle = forward ? parent.getNextYearBtn(rootLocator) : parent.getPervYearBtn(rootLocator);

        clickUntil(
                navBtnEle,
                () -> getCurrentYear() == targetYear,
                "Failed to navigate to year: " + targetYear
        );
    }

    private void navigateToMonth(LocalDate targetMonth) {
        YearMonth currentYearMonth = getCurrentYearMonth();
        YearMonth targetYearMonth = YearMonth.from(targetMonth);
        boolean forward = targetYearMonth.isAfter(currentYearMonth);
        WebElement navBtnEle = forward ? parent.getNextMonthBtn(rootLocator) : parent.getPrevMonthBtn(rootLocator);

        clickUntil(
                navBtnEle,
                () -> getCurrentYearMonth().equals(targetYearMonth),
                "Failed to navigate to month: " + targetYearMonth);
    }

    private void clickUntil(WebElement buttonEle, Supplier<Boolean> isTargetReached, String failMsg) {
        int maxIterations = 200;
        while (!isTargetReached.get() && maxIterations-- > 0) {
            buttonEle.click();
        }
        if (!isTargetReached.get()) throw new IllegalStateException(failMsg);
    }

    private int getCurrentYear() {
        String headingText = getHeadingEle().getText();
        return Integer.parseInt(headingText.replaceAll("\\D", ""));
    }

    private YearMonth getCurrentYearMonth() {
        String headingText = getHeadingEle().getText();
        return YearMonth.parse(headingText, DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH));
    }

    public enum CalendarView {
        MONTH, YEAR, DECADE
    }
}
