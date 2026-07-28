package component.main;

import actions.CalendarActions;
import component.constract.CalendarRootLocator;
import component.locator.DatePickerRootLocator;
import component.locator.StaticCalendarRootLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CalendarComp extends BaseComp {
    private String calendarSel = ".//*[@data-slot='root']";
    private String datePickerSel = ".//button[./*[contains(@class,'calendar')]]";
    private String dateSel = "[data-value='%s']";
    private By datePickerCalenderSel = By.cssSelector("[id^='reka'][dir='ltr']");
    private By headingSel = By.cssSelector("[data-slot='heading']");
    private By nextMonthSel = By.cssSelector("[aria-label='Next month']");
    private By previousMonthSel = By.cssSelector("[aria-label='Previous month']");
    private By nextYearSel = By.cssSelector("[aria-label='Next year']");
    private By previousYearSel = By.cssSelector("[aria-label='Previous year']");
    private By selectedDateSel = By.cssSelector("[data-selected='true']");

    public CalendarComp(WebDriver driver) {
        super(driver);
    }

    public WebElement getCalenderBasedOnId(String calendarLabel) {
        return getComponentBasedOnHeader(calendarLabel, calendarSel);
    }

    public WebElement getDatePickerTriggerBasedOnId(String calendarLabel) {
        return getComponentBasedOnHeader(calendarLabel, datePickerSel);
    }

    public WebElement getOpenDatePickerCalendar() {
        return driver.findElement(datePickerCalenderSel);
    }

    public WebElement getDateCell(CalendarRootLocator rootLocator, String dateValue) {
        return rootLocator.locate()
                .findElement(By.cssSelector(String.format(dateSel, dateValue)));
    }

    public WebElement getHeadingEle(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(headingSel);
    }

    public WebElement getNextMonthBtn(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(nextMonthSel);
    }

    public WebElement getPrevMonthBtn(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(previousMonthSel);
    }

    public WebElement getNextYearBtn(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(nextYearSel);
    }

    public WebElement getPervYearBtn(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(previousYearSel);
    }

    public List<WebElement> getSelectedDate(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElements(selectedDateSel);
    }

    public CalendarActions forCalendar(String calendarLabel) {
        return new CalendarActions(this, new StaticCalendarRootLocator(this, calendarLabel));
    }

    public CalendarActions forDatePicker(String calendarLabel) {
        return new CalendarActions(this, new DatePickerRootLocator(this, calendarLabel));
    }
}
