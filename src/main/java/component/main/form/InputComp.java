package component.main.form;

import component.main.BaseComp;
import data.DropdownOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class InputComp extends BaseComp {
    private WebElement cachedRoot;
    private final String inputLabel;
    private static final String dropdownOpt = ".//*[@role='option'][.//span[contains(.,'%s')]]";
    private static final By popupSel = By.cssSelector("[id^='reka'][role='listbox']");
    private static final By uploadFileInputSel = By.cssSelector("input[type='file']");
    private static final By inputEleSel = By.cssSelector("input");
    private static final By selectEleSel = By.cssSelector("[data-slot='value']");
    private static final By countryCodeSel = By.cssSelector("span[data-slot='leading'] span");
    private static final By pinInputSel = By.cssSelector("input[inputmode='text']");
    private static final By clearBtnSel = By.cssSelector("button[aria-label='Clear input']");
    private static final By showPasswordBtnSel = By.cssSelector("button[aria-label='Show password']");
    private static final By showPopupBtnSel = By.cssSelector("span[data-slot='trailingIcon']");
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
    private static final By incrementBtnSel = By.cssSelector("[data-slot='increment'] button");
    private static final By decrementBtnSel = By.cssSelector("[data-slot='decrement'] button");
    private static final By textAreaSel = By.cssSelector("textarea");
    private static final By tagsItemSel = By.cssSelector("div[aria-labelledby*='reka-tags-input-item-text']");
    private static final By checkboxSel = By.cssSelector("button[role='checkbox']");
    private static final By switchSel = By.cssSelector("button[role='switch']");
    private static final By sliderSel = By.cssSelector("span[role='slider']");
    private String inputRatingStr = "button[role='radio'][value='%d']";
    private String groupBtnStr = ".//label[contains(., '%s')]/ancestor::*[@data-slot='item']//button";
    private String rangeInputMonthStr = "[data-segment='month'][data-reka-date-range-field-segment-type='%s']";
    private String rangeInputDayStr = "[data-segment='day'][data-reka-date-range-field-segment-type='%s']";
    private String rangeInputYearStr = "[data-segment='year'][data-reka-date-range-field-segment-type='%s']";
    private String rangeInputHourStr = "[data-segment='hour'][data-reka-time-range-field-segment-type='%s']";
    private String rangeInputMinuteStr = "[data-segment='minute'][data-reka-time-range-field-segment-type='%s']";
    private String rangeInputPeriodStr = "[data-segment='dayPeriod'][data-reka-time-range-field-segment-type='%s']";
    private String tagItemDeleteIconXStr = ".//span[contains(.,'%s')]/following-sibling::button[@data-slot='tagsItemDelete']";

    public InputComp(WebDriver driver, String inputLabel) {
        super(driver);
        this.inputLabel = inputLabel;
    }

    public WebElement inputByLabel() {
        cachedRoot = getOrRefreshCached(cachedRoot, this::resolveRoot);
        return cachedRoot;
    }

    protected WebElement resolveRoot() {
        return getRootComp(inputLabel);
    }

    public WebElement uploadFileInput() {
        return inputByLabel().findElement(uploadFileInputSel);
    }

    public WebElement inputEle() {
        return inputByLabel().findElement(inputEleSel);
    }

    public WebElement selectEle() {
        return inputByLabel().findElement(selectEleSel);
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
        return wait.pollingEvery(Duration.ofMillis(200))
                .until(ExpectedConditions.visibilityOfElementLocated(popupSel));
    }

    public WebElement popoverOption(DropdownOption option) {
        return popupDropdown().findElement(By.xpath(String.format(dropdownOpt, option.label())));
    }

    public WebElement option(DropdownOption option) {
        return inputByLabel().findElement(By.xpath(String.format(dropdownOpt, option.label())));
    }

    public WebElement checkbox() {
        return inputByLabel().findElement(checkboxSel);
    }

    public WebElement ratingItem(int ratingValue) {
        return inputByLabel().findElement(By.cssSelector(String.format(inputRatingStr, ratingValue)));
    }

    public WebElement switchToggle() {
        return inputByLabel().findElement(switchSel);
    }

    public WebElement groupBtn(DropdownOption option) {
        return inputByLabel().findElement(By.xpath(String.format(groupBtnStr, option.label())));
    }

    public WebElement slider() {
        return inputByLabel().findElement(sliderSel);
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
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputMonthStr, rangeBound.value())));
    }

    public WebElement rangeInputDay(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputDayStr, rangeBound.value())));
    }

    public WebElement rangeInputYear(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputYearStr, rangeBound.value())));
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
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputHourStr, rangeBound.value())));
    }

    public WebElement rangeInputMinute(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputMinuteStr, rangeBound.value())));
    }

    public WebElement rangeInputPeriod(RangeBound rangeBound) {
        return inputByLabel().findElement(By.cssSelector(String.format(rangeInputPeriodStr, rangeBound.value())));
    }

    public List<WebElement> tagsItem() {
        return inputByLabel().findElements(tagsItemSel);
    }

    public WebElement deleteIconByItem(String tagItem) {
        return inputByLabel().findElement(By.xpath(String.format(tagItemDeleteIconXStr, tagItem)));
    }

    public WebElement increaseBtn() {
        return inputByLabel().findElement(incrementBtnSel);
    }

    public WebElement decreaseBtn() {
        return inputByLabel().findElement(decrementBtnSel);
    }

    public WebElement textArea() {
        return inputByLabel().findElement(textAreaSel);
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
