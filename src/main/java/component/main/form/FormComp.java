package component.main.form;

import component.main.BaseComp;
import data.FormFieldLabel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FormComp extends BaseComp {
    private WebElement cachedRoot;
    private final String formLabel;
    private String wrapper = "//label[.='%s']/ancestor::*[@data-slot='wrapper']/following-sibling::div";
    private By inputSel = By.cssSelector("input");
    private By inputIncrementSel = By.cssSelector("[data-slot='increment']");
    private By inputDecrementSel = By.cssSelector("[data-slot='decrement']");
    private By textAreaSel = By.cssSelector("textarea");
    private By inputMonthSel = By.cssSelector("[data-segment='month']");
    private By inputDaySel = By.cssSelector("[data-segment='day']");
    private By inputYearSel = By.cssSelector("[data-segment='year']");
    private By hourInputSel = By.cssSelector("[data-reka-time-field-segment='hour']");
    private By minuteInputSel = By.cssSelector("[data-reka-time-field-segment='minute']");
    private By addTagsInputSel = By.cssSelector("input[placeholder*='Add a tag']");
    private String dropdownOpt = ".//*[contains(@id,'reka-combobox-item')][.//span[contains(.,'%s')]]";
    private By popupSel = By.cssSelector("[id*='reka-combobox-content']");

    public FormComp(WebDriver driver, String formLabel) {
        super(driver);
        this.formLabel = formLabel;
    }

    public InputComp inputComp() {
        return new InputComp(driver, formLabel);
    }

    public WebElement formByLabel() {
        cachedRoot = getOrRefreshCached(cachedRoot, () -> getRootComp(formLabel));
        return cachedRoot;
    }

    public WebElement baseComp(FormFieldLabel formField) {
        return formByLabel().findElement(By.xpath(String.format(wrapper, formField.label())));
    }

    public WebElement input() {
        return baseComp(FormFieldLabel.INPUT).findElement(inputSel);
    }

    public WebElement inputNumber() {
        return baseComp(FormFieldLabel.INPUT_NUMBER).findElement(inputSel);
    }

    public WebElement inputNumberIncrementBtn() {
        return baseComp(FormFieldLabel.INPUT_NUMBER).findElement(inputIncrementSel);
    }

    public WebElement inputNumberDecrementBtn() {
        return baseComp(FormFieldLabel.INPUT_NUMBER).findElement(inputDecrementSel);
    }

    public List<WebElement> pinInputs() {
        return baseComp(FormFieldLabel.PIN_INPUT).findElements(inputSel);
    }

    public WebElement inputMonth() {
        return inputComp().singleInputMonth();
    }

    public WebElement inputDay() {
        return inputComp().singleInputDay();
    }

    public WebElement inputYear() {
        return inputComp().singleInputYear();
    }
}
