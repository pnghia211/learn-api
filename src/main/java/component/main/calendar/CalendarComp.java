package component.main.calendar;

import component.constract.CalendarRootLocator;
import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitForCalendarReady;

import java.time.Duration;
import java.util.List;

public class CalendarComp extends BaseComp {
    private WebElement cachedRoot;
    private By calendarSel = By.cssSelector("[data-slot='root']");
    private By datePickerBtnSel = By.cssSelector("button:has(> [class*='calendar'])");
    private String dateValueCss = "[data-value='%s']";
    private By datePickerCalenderSel = By.cssSelector("[id^='reka'][dir='ltr']");
    private By headingSel = By.cssSelector("[data-slot='header'] [data-slot='label']");
    private By nextMonthSel = By.cssSelector("[aria-label='Next month']");
    private By previousMonthSel = By.cssSelector("[aria-label='Previous month']");
    private By nextYearSel = By.cssSelector("[aria-label='Next year']");
    private By previousYearSel = By.cssSelector("[aria-label='Previous year']");
    private By selectedDateSel = By.cssSelector("[data-selected='true']");
    private By dateRangePresets = By.xpath("//div[@data-slot='root']/preceding-sibling::div/button");

    public CalendarComp(WebDriver driver) {
        super(driver);
    }

    public Actions action() {
        return this.actions;
    }

    public WebElement getCachedRoot(String tableLabel) {
        cachedRoot = getOrRefreshCached(cachedRoot, () -> getRootComp(tableLabel));
        return cachedRoot;
    }

    public WebElement calendarByLabel(String calendarLabel) {
        return getCachedRoot(calendarLabel).findElement(calendarSel);
    }

    public WebElement datePickByLabel(String calendarLabel) {
        return getCachedRoot(calendarLabel).findElement(datePickerBtnSel);
    }

    public WebElement waitForOpenDatePickerCalendarReady() {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(StaleElementReferenceException.class)
                .until(new WaitForCalendarReady(datePickerCalenderSel));
    }

    public List<WebElement> dateRangePresets(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElements(dateRangePresets);
    }

    public WebElement dateCell(CalendarRootLocator rootLocator, String dateValue) {
        return rootLocator.locate()
                .findElement(By.cssSelector(String.format(dateValueCss, dateValue)));
    }

    public WebElement headingEle(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(headingSel);
    }

    public WebElement nextMonthBtn(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(nextMonthSel);
    }

    public WebElement prevMonthBtn(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(previousMonthSel);
    }

    public WebElement nextYearBtn(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(nextYearSel);
    }

    public WebElement pervYearBtn(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElement(previousYearSel);
    }

    public List<WebElement> selectedDate(CalendarRootLocator rootLocator) {
        return rootLocator.locate().findElements(selectedDateSel);
    }
}
