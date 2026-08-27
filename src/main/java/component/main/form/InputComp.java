package component.main.form;

import component.main.BaseComp;
import data.DropdownOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InputComp extends BaseComp {
    private WebElement cachedRoot;
    private final String inputLabel;
    private String dropdownOpt = ".//*[contains(@id,'reka-combobox-item')][.//span[contains(.,'%s')]]";
    private By popupSel = By.cssSelector("[id*='reka-combobox-content'][data-state='open']");
    private By uploadFileInputSel = By.cssSelector("input[type='file']");
    private By textInputSel = By.cssSelector("input");
    private By clearBtnSel = By.cssSelector("button[aria-label='Clear input']");
    private By showPasswordBtnSel = By.cssSelector("button[aria-label='Show password']");
    private By showPopupBtnSel = By.cssSelector("button[aria-label='Show popup'] > span");
    private By indicatorSel = By.cssSelector("[data-slot='indicator']");
    private By pwdStrengthRequirementSel = By.cssSelector("#password-strength");
    private By pwdRequirementList = By.cssSelector("ul li");
    private By creditCardSel = By.cssSelector("input[placeholder*='4242']");
    private By calendarSel = By.cssSelector("input[placeholder*='MM/YY']");
    private By cvcSel = By.cssSelector("input[placeholder*='CVC']");
    private By singleInputMonthSel = By.cssSelector("[data-segment='month']");
    private By singleInputDaySel = By.cssSelector("[data-segment='day']");
    private By singleInputYearSel = By.cssSelector("[data-segment='year']");
    private String rangeInputMonthSel = "[data-segment='month'][data-reka-date-range-field-segment-type='%s']";
    private String rangeInputDaySel = "[data-segment='day'][data-reka-date-range-field-segment-type='%s']";
    private String rangeInputYearSel = "[data-segment='year'][data-reka-date-range-field-segment-type='%s']";
    private By tagsItem = By.cssSelector("[data-slot='tagsItem']");
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
