package component.main.form;

import component.main.BaseComp;
import data.DropdownOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InputComp extends BaseComp {
    private WebElement cachedRoot;
    private final String inputLabel;
    private static final String dropdownOpt = ".//*[contains(@id,'reka-combobox-item')][.//span[contains(.,'%s')]]";
    private static final By popupSel = By.cssSelector("[id*='reka-combobox-content']");
    private static final By uploadFileInputSel = By.cssSelector("input[type='file']");
    private static final By textInputSel = By.cssSelector("input");
    private static final By countryCodeSel = By.cssSelector("span[data-slot='leading'] span");
    private static final By pinInputSel = By.cssSelector("input[inputmode='text']");
    private static final By clearBtnSel = By.cssSelector("button[aria-label='Clear input']");
    private static final By showPasswordBtnSel = By.cssSelector("button[aria-label='Show password']");
    private static final By showPopupBtnSel = By.cssSelector("button[aria-label='Show popup']");
    private static final By indicatorSel = By.cssSelector("[data-slot='indicator']");
    private static final By pwdStrengthRequirementSel = By.cssSelector("#password-strength");
    private static final By pwdRequirementList = By.cssSelector("ul li");
    private static final By creditCardSel = By.cssSelector("input[placeholder*='4242']");
    private static final By calendarSel = By.cssSelector("input[placeholder*='MM/YY']");
    private static final By cvcSel = By.cssSelector("input[placeholder*='CVC']");
    private static final By singleInputMonthSel = By.cssSelector("[data-segment='month']");
    private static final By singleInputDaySel = By.cssSelector("[data-segment='day']");
    private static final By singleInputYearSel = By.cssSelector("[data-segment='year']");
    private static final By hourInputSel = By.cssSelector("[data-segment='hour']");
    private static final By minuteInputSel = By.cssSelector("[data-segment='minute']");
    private static final By dayPeriodInputSel = By.cssSelector("[data-segment='dayPeriod']");
    private static final By addTagsInputSel = By.cssSelector("input[placeholder*='Add a tag']");
    private String rangeInputMonthSel = "[data-segment='month'][data-reka-date-range-field-segment-type='%s']";
    private String rangeInputDaySel = "[data-segment='day'][data-reka-date-range-field-segment-type='%s']";
    private String rangeInputYearSel = "[data-segment='year'][data-reka-date-range-field-segment-type='%s']";
    private String rangeInputHourSel = "[data-segment='hour'][data-reka-time-range-field-segment-type='%s']";
    private String rangeInputMinuteSel = "[data-segment='minute'][data-reka-time-range-field-segment-type='%s']";
    private String rangeInputPeriodSel = "[data-segment='dayPeriod'][data-reka-time-range-field-segment-type='%s']";
    private static final By tagsItem = By.cssSelector("div[aria-labelledby*='reka-tags-input-item-text']");
    private String tagItemDeleteIconXpath = ".//span[contains(.,'%s')]/following-sibling::button[@data-slot='tagsItemDelete']";

    public InputComp(WebDriver driver, String inputLabel) {
        super(driver);
        this.inputLabel = inputLabel;
    }

    public WebElement inputByLabel() {
        cachedRoot = getOrRefreshCached(cachedRoot, () -> getRootComp(inputLabel));
        return cachedRoot;
    }

    public WebElement uploadFileInput() {
        return inputByLabel().findElement(uploadFileInputSel);
    }

    public WebElement textInput() {
        return inputByLabel().findElement(textInputSel);
    }

    public WebElement countryCodeInput() {
        return inputByLabel().findElement(countryCodeSel);
    }

    public List<WebElement> pinInputs() {
        return inputByLabel().findElements(pinInputSel);
    }

    public WebElement clearBtn() {
        return inputByLabel().findElement(clearBtnSel);
    }

    public WebElement showPasswordBtn() {
        return inputByLabel().findElement(showPasswordBtnSel);
    }

    public WebElement popupBtn() {
        return inputByLabel().findElement(showPopupBtnSel);
    }

    public WebElement popupDropdown() {
        return driver.findElement(popupSel);
    }

    public WebElement dropdownOption(DropdownOption option) {
        return popupDropdown().findElement(By.xpath(String.format(dropdownOpt, option.label())));
    }

    public WebElement dropdownOption(String option) {
        return popupDropdown().findElement(By.xpath(String.format(dropdownOpt, option)));
    }

    public WebElement indicator() {
        return inputByLabel().findElement(indicatorSel);
    }

    public WebElement pwdRequirement() {
        return inputByLabel().findElement(pwdStrengthRequirementSel);
    }

    public List<WebElement> pwdRequirementList() {
        return inputByLabel().findElements(pwdRequirementList);
    }

    public WebElement creditCardInput() {
        return inputByLabel().findElement(creditCardSel);
    }

    public WebElement calendarInput() {
        return inputByLabel().findElement(calendarSel);
    }

    public WebElement singleInputMonth() {
        return inputByLabel().findElement(singleInputMonthSel);
    }

    public WebElement singleInputDay() {
        return inputByLabel().findElement(singleInputDaySel);
    }

    public WebElement singleInputYear() {
        return inputByLabel().findElement(singleInputYearSel);
    }

    public WebElement rangeInputMonth(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputMonthSel, rangeBound.value())));
    }

    public WebElement rangeInputDay(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputDaySel, rangeBound.value())));
    }

    public WebElement rangeInputYear(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputYearSel, rangeBound.value())));
    }

    public WebElement singleHourInput() {
        return inputByLabel().findElement(hourInputSel);
    }

    public WebElement singleMinuteInput() {
        return inputByLabel().findElement(minuteInputSel);
    }

    public WebElement singleDayPeriodInput() {
        return inputByLabel().findElement(dayPeriodInputSel);
    }

    public WebElement rangeInputHour(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputHourSel, rangeBound.value())));
    }

    public WebElement rangeInputMinute(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputMinuteSel, rangeBound.value())));
    }

    public WebElement rangeInputPeriod(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputPeriodSel, rangeBound.value())));
    }

    public List<WebElement> tagsItem() {
        return inputByLabel().findElements(tagsItem);
    }

    public WebElement deleteIconByItem(String tagItem) {
        return inputByLabel().findElement(By.xpath(String.format(tagItemDeleteIconXpath, tagItem)));
    }

    public enum RangeBound {
        START, END;

        public String value() {
            return name().toLowerCase();
        }
    }

    public WebElement cvcInput() {
        return inputByLabel().findElement(cvcSel);
    }
}
