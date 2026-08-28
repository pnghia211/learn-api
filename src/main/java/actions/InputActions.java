package actions;

import assertions.InputAssertions;
import component.main.form.InputComp;
import data.DropdownOption;
import model.CardMaskData;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;


import component.main.form.InputComp.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InputActions {
    private final InputComp inputComp;

    public InputActions(InputComp inputComp) {
        this.inputComp = inputComp;
    }

    public WebElement getTypeInput() {
        return inputComp.textInput();
    }

    public WebElement getCountryCodeInput() {
        return inputComp.countryCodeInput();
    }

    public InputActions uploadFile(String filePath) {
        inputComp.uploadFileInput().sendKeys(filePath);
        return this;
    }

    public InputActions type(String input) {
        clearAndType(getTypeInput(), input);
        return this;
    }

    public InputActions clickClearBtn() {
        inputComp.clearBtn().click();
        return this;
    }

    public InputActions clickShowPasswordBtn() {
        inputComp.showPasswordBtn().click();
        return this;
    }

    public String getIndicatorValue() {
        return inputComp.indicator().getAttribute("data-value");
    }

    public String getPwdStrengthRequirementTxt() {
        return inputComp.pwdRequirement().getText();
    }

    public InputActions fillMaskInputFields(CardMaskData data) {
        clearAndType(inputComp.creditCardInput(), data.cardNumber());
        clearAndType(inputComp.calendarInput(), data.expiry());
        clearAndType(inputComp.cvcInput(), data.cvc());
        return this;
    }

    protected void clearAndType(WebElement element, String input) {
        element.clear();
        element.sendKeys(input);
    }

    protected void typeForDateInputSegment(WebElement element, String input) {
        for (char c : input.toCharArray()) {
            element.sendKeys(String.valueOf(c));
        }
    }

    private String[] validateAndSplitDate(String date) {
        String[] parts = date.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Expected mm-dd-yyyy, got: " + date);
        }

        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month out of range (1-12): " + month);
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day out of range (1-31): " + day);
        }

        return parts;
    }

    public InputActions fillOneDateBound(String date) {
        String[] parts = validateAndSplitDate(date);

        typeForDateInputSegment(inputComp.singleInputMonth(), parts[0]);
        typeForDateInputSegment(inputComp.singleInputDay(), parts[1]);
        typeForDateInputSegment(inputComp.singleInputYear(), parts[2]);

        return this;
    }

    private void fillOneDateBound(String date, RangeBound bound) {
        String[] parts = validateAndSplitDate(date);

        typeForDateInputSegment(inputComp.rangeInputMonth(bound), parts[0]);
        typeForDateInputSegment(inputComp.rangeInputDay(bound), parts[1]);
        typeForDateInputSegment(inputComp.rangeInputYear(bound), parts[2]);
    }

    public InputActions fillDateRangeInput(String startDate, String endDate) {
        fillOneDateBound(startDate, RangeBound.START);
        fillOneDateBound(endDate, RangeBound.END);
        return this;
    }

    public String getSingleDateInputTxt() {
        String month = inputComp.singleInputMonth().getAttribute("aria-valuenow");
        String day = inputComp.singleInputDay().getAttribute("aria-valuenow");
        String year = inputComp.singleInputYear().getAttribute("aria-valuenow");

        return String.format("%02d-%02d-%s",
                Integer.parseInt(month),
                Integer.parseInt(day),
                year);
    }

    public String getRangeDateInputTxt(RangeBound bound) {
        String month = inputComp.rangeInputMonth(bound).getAttribute("aria-valuenow");
        String day = inputComp.rangeInputDay(bound).getAttribute("aria-valuenow");
        String year = inputComp.rangeInputYear(bound).getAttribute("aria-valuenow");

        return String.format("%02d-%02d-%s",
                Integer.parseInt(month),
                Integer.parseInt(day),
                year);
    }

    public InputActions clickPopupBtn() {
        inputComp.popupBtn().click();
        WaitUtils.waitForVisibility(inputComp.driver(), inputComp.popupDropdown());
        return this;
    }

    private void selectDropdownOpt(DropdownOption option) {
        WebElement ele = inputComp.dropdownOption(option);
        if ("unchecked".equalsIgnoreCase(ele.getAttribute("data-state"))) {
            WebElement popupDropdown = inputComp.popupDropdown();
            ele.click();

            WaitUtils.waitForInvisibility(inputComp.driver(), popupDropdown);
        }
    }

    public InputActions selectDropdownOpt(String option) {
        clickPopupBtn();
        WebElement ele = inputComp.dropdownOption(option);

        if ("unchecked".equalsIgnoreCase(ele.getAttribute("data-state"))) {
            WebElement popupDropdown = inputComp.popupDropdown();
            ele.click();

            WaitUtils.waitForInvisibility(inputComp.driver(), popupDropdown);
        }

        return this;
    }

    public InputActions selectMultiDropdownOpt(DropdownOption... options) {
        clickPopupBtn();

        for (DropdownOption option : options) {
            WebElement ele = inputComp.dropdownOption(option);
            if ("unchecked".equalsIgnoreCase(ele.getAttribute("data-state"))) {
                inputComp.dropdownOption(option).click();

                new WebDriverWait(inputComp.driver(), Duration.ofSeconds(5))
                        .until(d -> "checked".equalsIgnoreCase(
                                inputComp.dropdownOption(option).getAttribute("data-state")));
            }
        }

        return this;
    }

    public List<String> selectDropdownOptionsInOrder(DropdownOption... options) {
        List<String> actualValues = new ArrayList<>();

        for (DropdownOption option : options) {
            String dropdownValue = inputComp.textInput().getAttribute("value");

            if (option.label().equalsIgnoreCase(dropdownValue)) {
                actualValues.add(dropdownValue);
                continue;
            }

            clickPopupBtn();
            selectDropdownOpt(option);

            actualValues.add(inputComp.textInput().getAttribute("value"));
        }
        return actualValues;
    }

    public InputActions removeOption(DropdownOption... options) {
        inputComp.actions().sendKeys(Keys.ESCAPE).perform();
        for (DropdownOption option : options) {
            inputComp.deleteIconByItem(option.label()).click();
        }
        return this;
    }

    public List<String> getSelectedOptions() {
        List<String> result = new ArrayList<>();
        for (WebElement element : inputComp.tagsItem()) {
            String value = element.getText();
            result.add(value);
        }

        return result;
    }

    public InputActions typeInputs(String input) {
        List<WebElement> elements = inputComp.pinInputs();
        if (input.length() != elements.size()) {
            throw new IllegalArgumentException(
                    "Expected " + elements.size() + " characters but got " + input.length());
        }
        for (int i = 0; i < elements.size(); i++) {
            clearAndType(elements.get(i), String.valueOf(input.charAt(i)));
        }

        return this;
    }

//    public InputActions

    public InputAssertions verify() {
        return new InputAssertions(inputComp, this);
    }
}
