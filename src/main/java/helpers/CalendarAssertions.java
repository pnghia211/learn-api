package helpers;

import actions.CalendarActions;
import component.constract.CalendarRootLocator;
import component.main.CalendarComp;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class CalendarAssertions {
    private final CalendarComp parent;
    private final CalendarRootLocator rootLocator;
    private final CalendarActions actions;

    public CalendarAssertions(CalendarComp parent, CalendarRootLocator rootLocator, CalendarActions actions) {
        this.parent = parent;
        this.rootLocator = rootLocator;
        this.actions = actions;
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

    public CalendarAssertions dateIsSelected(String dateValue) {
        assertTrue(isDateSelected(dateValue));
        return this;
    }

    public CalendarAssertions dateIsNotSelected(String dateValue) {
        assertFalse(isDateSelected(dateValue));
        return this;
    }

    public CalendarAssertions dateIsDisabled(String dateValue) {
        String result = getDateCell(dateValue).getAttribute("aria-disabled");
        assertEquals("true", result);
        return this;
    }

    public CalendarAssertions datesAreSelected(List<String> dates) {
        dates.forEach(this::dateIsSelected);
        return this;
    }

    public CalendarAssertions heading(String heading) {
        String result = DateHelper.convertHeadingFormat(heading);
        String actual = getHeadingEle().getText();
        assertEquals(result, actual);
        return this;
    }

    public CalendarAssertions dateRangeSelected(String startDate, String endDate) {
        List<String> expected = DateHelper.buildDateRange(startDate, endDate);
        List<String> actual = parent.getSelectedDate(rootLocator)
                .stream()
                .map(el -> el.getAttribute("data-value"))
                .toList();

        assertEquals(new TreeSet<>(expected), new TreeSet<>(actual));
        return this;
    }

    public CalendarActions and() {
        return actions;
    }
}
