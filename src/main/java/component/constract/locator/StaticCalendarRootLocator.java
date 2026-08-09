package component.constract.locator;

import component.constract.CalendarRootLocator;
import component.main.calendar.CalendarComp;
import org.openqa.selenium.WebElement;

public class StaticCalendarRootLocator implements CalendarRootLocator {
    private final CalendarComp comp;
    private final String calendarLabel;

    public StaticCalendarRootLocator(CalendarComp comp, String calendarLabel) {
        this.comp = comp;
        this.calendarLabel = calendarLabel;
    }

    @Override
    public WebElement locate() {
        return comp.calendarByLabel(calendarLabel);
    }

    @Override
    public String getCalendarLabel() {
        return calendarLabel;
    }
}
