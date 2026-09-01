package component.main.factory;

import actions.CalendarActions;
import component.constract.locator.DatePickerRootLocator;
import component.constract.locator.StaticCalendarRootLocator;
import component.main.calendar.CalendarComp;
import data.CalendarLabel;
import org.openqa.selenium.WebDriver;
import page.BasePage;

public class CalendarFactory extends BasePage {
    public CalendarFactory(WebDriver driver) {
        super(driver);
    }

    public CalendarActions forCalendar(CalendarLabel calendarLabel) {
        CalendarComp calendarComp = new CalendarComp(driver);
        return new CalendarActions(calendarComp, new StaticCalendarRootLocator(calendarComp, calendarLabel.label()));
    }

    public CalendarActions forDatePicker(CalendarLabel calendarLabel) {
        CalendarComp calendarComp = new CalendarComp(driver);
        return new CalendarActions(calendarComp, new DatePickerRootLocator(calendarComp, calendarLabel.label()));
    }
}
