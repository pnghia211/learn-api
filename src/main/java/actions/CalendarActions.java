package actions;

import assertions.CalendarAssertions;
import component.constract.CalendarRootLocator;
import component.main.calendar.CalendarComp;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static helpers.DateHelper.parseMonthYear;
import static org.testng.AssertJUnit.assertTrue;

public class CalendarActions{
    private final CalendarRootLocator rootLocator;
    private static final Pattern MONTH_PATTERN = Pattern.compile("^[A-Z][a-z]+ \\d{4}$"); // "February 2022"
    private static final Pattern YEAR_PATTERN = Pattern.compile("^\\d{4}$"); // "2022"
    private static final Pattern DECADE_PATTERN = Pattern.compile("^\\d{4}\\s-\\s\\d{4}$"); // "2020-2031"
    private CalendarComp parent;

    public CalendarActions(CalendarComp parent, CalendarRootLocator rootLocator) {
        this.parent = parent;
        this.rootLocator = rootLocator;
    }

    public CalendarAssertions verify(){
        return new CalendarAssertions(rootLocator, this);
    }

    public WebElement getDateCell(String dateValue) {
        return parent.dateCell(rootLocator, dateValue);
    }

    public WebElement getHeadingEle() {
        return parent.headingEle(rootLocator);
    }

    public WebElement getDatePickerHeadingEle(String datePickerHeader) {
        return parent.datePickByLabel(datePickerHeader);
    }

    private boolean isDateSelected(String dateValue) {
        String result = getDateCell(dateValue).getAttribute("data-selected");
        return "true".equalsIgnoreCase(result);
    }

    public List<WebElement> getSelectedDates(CalendarRootLocator rootLocator) {
        return parent.selectedDate(rootLocator);
    }

    public List<WebElement> getDateRangePreset(CalendarRootLocator rootLocator){
        return parent.dateRangePresets(rootLocator);
    }

    public CalendarActions selectNextMonth() {
        parent.nextMonthBtn(rootLocator).click();
        return this;
    }

    public CalendarActions selectNextYear() {
        parent.nextYearBtn(rootLocator).click();
        return this;
    }

    public CalendarActions selectViewGrid(CalendarView view) {
        WebElement headingEle = parent.headingEle(rootLocator);
        headingEle.click();

        String headingText = parent.headingEle(rootLocator).getText();

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
        int targetYear = target.getYear();
        List<YearMonth> current = getCurrentYearMonths();

        if (current.stream().anyMatch(ym -> ym.getYear() == targetYear)) return;

        boolean forward = targetYear > current.get(current.size() - 1).getYear();
        WebElement navBtnEle = forward ? parent.nextYearBtn(rootLocator) : parent.pervYearBtn(rootLocator);

        clickUntil(
                navBtnEle,
                () -> getCurrentYearMonths().stream().anyMatch(ym -> ym.getYear() == targetYear),
                "Failed to navigate to year: " + targetYear
        );
    }

    private void navigateToMonth(LocalDate targetMonth) {
        YearMonth targetYearMonth = YearMonth.from(targetMonth);
        List<YearMonth> current = getCurrentYearMonths();
        if (current.contains(targetYearMonth)) return;

        boolean forward = targetYearMonth.isAfter(current.get(current.size() - 1));
        WebElement navBtnEle = forward ? parent.nextMonthBtn(rootLocator) : parent.prevMonthBtn(rootLocator);

        clickUntil(
                navBtnEle,
                () -> getCurrentYearMonths().contains(targetYearMonth),
                "Failed to navigate to month: " + targetYearMonth);
    }

    private void clickUntil(WebElement buttonEle, Supplier<Boolean> isTargetReached, String failMsg) {
        int maxIterations = 20;
        while (!isTargetReached.get()) {
            if (maxIterations-- <= 0) throw new IllegalStateException(failMsg);
            buttonEle.click();
        }
    }

    private List<YearMonth> getCurrentYearMonths() {
        String headingText = getHeadingEle().getText();
        String[] parts = headingText.split("\\s*-\\s*");

        if (parts.length == 1) {
            return List.of(parseMonthYear(parts[0], null));
        }

        // dual calendar: right side always carries an explicit year
        YearMonth right = parseMonthYear(parts[1], null);
        YearMonth left = parseMonthYear(parts[0], right.getYear());

        // left had no year of its own and wrapped backward (e.g. "Dec - Jan 2024" -> Dec 2023)
        if (!parts[0].matches(".*\\d{4}$") && left.isAfter(right)) {
            left = left.minusYears(1);
        }

        return List.of(left, right);
    }

    public enum CalendarView {
        MONTH, YEAR, DECADE
    }
}
