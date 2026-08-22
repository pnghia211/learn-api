package assertions;

import actions.CalendarActions;
import component.constract.CalendarRootLocator;
import component.main.calendar.CalendarComp;
import helpers.DateHelper;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class InputAssertions {
    private final CalendarComp parent;
    private final CalendarRootLocator rootLocator;
    private final CalendarActions actions;

    public InputAssertions(CalendarComp parent, CalendarRootLocator rootLocator, CalendarActions actions) {
        this.parent = parent;
        this.rootLocator = rootLocator;
        this.actions = actions;
    }

    private boolean isDateSelected(String dateValue) {
        String result = actions.getDateCell(dateValue).getAttribute("data-selected");
        return "true".equalsIgnoreCase(result);
    }

    public InputAssertions dateIsSelected(String dateValue) {
        assertTrue(isDateSelected(dateValue));
        return this;
    }

    public InputAssertions dateIsNotSelected(String dateValue) {
        assertFalse(isDateSelected(dateValue));
        return this;
    }

    public InputAssertions dateIsDisabled(String dateValue) {
        String result = actions.getDateCell(dateValue).getAttribute("aria-disabled");
        assertEquals("true", result);
        return this;
    }

    public InputAssertions datesAreSelected(List<String> dates) {
        dates.forEach(this::dateIsSelected);
        return this;
    }

    public InputAssertions heading(String heading) {
        String result = DateHelper.convertHeadingFormat(heading);
        String actual = actions.getHeadingEle().getText();
        assertEquals(result, actual);
        return this;
    }

    public InputAssertions datePickerHeading(String date) {
        String result = DateHelper.convertHeadingFormat(date);
        String actual = actions.getDatePickerHeadingEle(rootLocator.getCalendarLabel()).getText();
        assertEquals(result, actual);
        return this;
    }

    public InputAssertions dateRangeSelected(String startDate, String endDate) {
        List<String> expected = DateHelper.buildDateRange(startDate, endDate);
        List<String> actual = parent.selectedDate(rootLocator)
                .stream()
                .map(el -> el.getAttribute("data-value"))
                .toList();

        assertEquals(new TreeSet<>(expected), new TreeSet<>(actual));
        return this;
    }

    public InputAssertions presetRangesDisplayed(List<String> expectedLabels) {
        List<String> actualLabels = parent.dateRangePresets(rootLocator)
                .stream()
                .map(WebElement::getText)
                .toList();

        assertEquals(expectedLabels, actualLabels);
        return this;
    }

    public CalendarActions and() {
        return actions;
    }
}
