package component.main.calendar;

import component.constract.RootLocator;
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
    private static final By calendarSel = By.cssSelector("[data-slot='root']");
    private static final By datePickerBtnSel = By.cssSelector("button:has(> [class*='calendar'])");
    private static final String dateValueCss = "[data-value='%s']";
    private static final By datePickerCalendarSel = By.cssSelector("[id^='reka'][dir='ltr']");
    private static final By headingSel = By.cssSelector("[data-slot='header'] [data-slot='label']");
    private static final By nextMonthSel = By.cssSelector("[aria-label='Next month']");
    private static final By previousMonthSel = By.cssSelector("[aria-label='Previous month']");
    private static final By nextYearSel = By.cssSelector("[aria-label='Next year']");
    private static final By previousYearSel = By.cssSelector("[aria-label='Previous year']");
    private static final By selectedDateSel = By.cssSelector("[data-selected='true']");
    private static final By dateRangePresets = By.xpath("//div[@data-slot='root']/preceding-sibling::div/button");

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
                .until(new WaitForCalendarReady(datePickerCalendarSel));
    }

    public List<WebElement> dateRangePresets(RootLocator rootLocator) {
        return rootLocator.locate().findElements(dateRangePresets);
    }

    public WebElement dateCell(RootLocator rootLocator, String dateValue) {
        return rootLocator.locate()
                .findElement(By.cssSelector(String.format(dateValueCss, dateValue)));
    }

    public WebElement headingEle(RootLocator rootLocator) {
        return rootLocator.locate().findElement(headingSel);
    }

    public WebElement nextMonthBtn(RootLocator rootLocator) {
        return rootLocator.locate().findElement(nextMonthSel);
    }

    public WebElement prevMonthBtn(RootLocator rootLocator) {
        return rootLocator.locate().findElement(previousMonthSel);
    }

    public WebElement nextYearBtn(RootLocator rootLocator) {
        return rootLocator.locate().findElement(nextYearSel);
    }

    public WebElement pervYearBtn(RootLocator rootLocator) {
        return rootLocator.locate().findElement(previousYearSel);
    }

    public List<WebElement> selectedDate(RootLocator rootLocator) {
        return rootLocator.locate().findElements(selectedDateSel);
    }
}
