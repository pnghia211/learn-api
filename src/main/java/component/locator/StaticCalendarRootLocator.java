package component.locator;

import component.constract.locator.CalendarRootLocator;
import component.main.CalendarComp;
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
        return comp.getCalenderBasedOnId(calendarLabel);
    }

    @Override
    public String getCalendarLabel() {
        return calendarLabel;
    }
}
