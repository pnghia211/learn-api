package component.locator;

import component.constract.locator.CalendarRootLocator;
import component.main.CalendarComp;
import org.openqa.selenium.WebElement;

public class DatePickerRootLocator implements CalendarRootLocator {
    private final CalendarComp comp;
    public final String calendarLabel;

    public DatePickerRootLocator(CalendarComp comp, String calendarLabel) {
        this.comp = comp;
        this.calendarLabel = calendarLabel;
    }

    @Override
    public WebElement locate() {
        WebElement datePickerEle = comp.getDatePickerBasedOnId(calendarLabel);
        comp.getAction().moveToElement(datePickerEle).perform();
        if (!"true".equals(datePickerEle.getAttribute("aria-expanded"))) {
            datePickerEle.click();
        }
        return comp.waitForOpenDatePickerCalendarReady();
    }

    @Override
    public String getCalendarLabel() {
        return calendarLabel;
    }
}
