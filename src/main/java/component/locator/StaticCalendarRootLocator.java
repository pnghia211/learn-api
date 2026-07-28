package component.locator;

import component.constract.CalendarRootLocator;
import component.main.CalendarComp;
import org.openqa.selenium.WebElement;

public class StaticCalendarRootLocator implements CalendarRootLocator {
    private final CalendarComp comp;
    private final String calendarId;

    public StaticCalendarRootLocator(CalendarComp comp, String calendarId) {
        this.comp = comp;
        this.calendarId = calendarId;
    }

    @Override
    public WebElement locate() {
        return comp.getCalenderBasedOnId(calendarId);
    }
}
