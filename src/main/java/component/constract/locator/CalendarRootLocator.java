package component.constract.locator;

import component.constract.RootLocator;
import component.main.calendar.CalendarComp;
import org.openqa.selenium.WebElement;

public class CalendarRootLocator implements RootLocator {
    private final CalendarComp comp;
    private final String calendarLabel;

    public CalendarRootLocator(CalendarComp comp, String calendarLabel) {
        this.comp = comp;
        this.calendarLabel = calendarLabel;
    }

    @Override
    public WebElement locate() {
        return comp.calendarByLabel(calendarLabel);
    }

    @Override
    public String getRootLabel() {
        return calendarLabel;
    }
}
