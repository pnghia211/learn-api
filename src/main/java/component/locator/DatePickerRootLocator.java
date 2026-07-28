package component.locator;

import component.constract.CalendarRootLocator;
import component.main.CalendarComp;
import org.openqa.selenium.WebElement;

public class DatePickerRootLocator implements CalendarRootLocator {
    private final CalendarComp comp;
    private final String calendarLabel;

    public DatePickerRootLocator(CalendarComp comp, String calendarLabel) {
        this.comp = comp;
        this.calendarLabel = calendarLabel;
    }

    @Override
    public WebElement locate() {
        WebElement trigger = comp.getDatePickerTriggerBasedOnId(calendarLabel);
        if (!"true".equals(trigger.getAttribute("aria-expanded"))) {
            trigger.click();
        }
        return comp.getOpenDatePickerCalendar();
    }
}
