package actions;

import assertions.InputAssertions;
import component.main.form.InputComp;
import component.main.form.InputComp.RangeBound;
import data.DropdownOption;
import helpers.DateHelper;
import model.CardMaskData;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InputActions {
    private final InputComp inputComp;

    public InputActions(InputComp inputComp) {
        this.inputComp = inputComp;
    }

    public WebElement getInputText() {
        return inputComp.inputEle();
    }

    public WebElement getSelectText() {
        return inputComp.selectEle();
    }

    public WebElement getCountryCodeInput() {
        return inputComp.countryCodeInput();
    }

    public InputActions uploadFile(String filePath) {
        inputComp.uploadFileInput().sendKeys(filePath);
        return this;
    }

    public InputActions type(String input) {
        clearAndType(getInputText(), input);
        return this;
    }

    public InputActions inputTags(List<String> inputs) {
        for (String input : inputs) {
            typeAndEnter(getInputText(), input);
        }
        return this;
    }

    public InputActions typeInTextArea(String value) {
        clearAndType(inputComp.textArea(), value);
        return this;
    }

    public String getTextArea() {
        return inputComp.textArea().getDomProperty("value");
    }

    public List<String> getTagItemsTxt() {
        List<String> result = new ArrayList<>();
        for (WebElement element : inputComp.tagsItem()) {
            String txt = element.getText();

            result.add(txt);
        }

        return result;
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

    protected void typeAndEnter(WebElement element, String input) {
        element.sendKeys(input);
        inputComp.actions().sendKeys(Keys.ENTER).perform();
    }

    public void inputMenus(List<DropdownOption> options) {
        for (DropdownOption option : options) {
            typeAndEnter(inputComp.inputEle(), option.label());
        }
        inputComp.actions().sendKeys(Keys.ESCAPE).perform();
    }

    public void inputMenu(DropdownOption option) {
        typeAndEnter(inputComp.inputEle(), option.label());
    }

    protected void typeInputSegment(WebElement element, String input) {
        for (char c : input.toCharArray()) {
            element.sendKeys(String.valueOf(c));
        }
    }

    public InputActions fillDate(String date) {
        String[] parts = DateHelper.validateAndSplitDate(date);

        typeInputSegment(inputComp.singleInputMonth(), parts[0]);
        typeInputSegment(inputComp.singleInputDay(), parts[1]);
        typeInputSegment(inputComp.singleInputYear(), parts[2]);

        return this;
    }

    public InputActions fillTime(String timeInput, String period) {
        LocalTime time = LocalTime.parse(timeInput);
        String hour = String.valueOf(time.getHour());
        String minute = String.valueOf(time.getMinute());

        inputComp.singleHourInput().sendKeys(hour);
        inputComp.singleMinuteInput().sendKeys(minute);
        inputComp.singleDayPeriodInput().sendKeys(period);

        return this;
    }

    public InputActions fillTimeRangeInput(RangeBound bound, String time, String period) {
        LocalTime t = LocalTime.parse(time);
        inputComp.rangeInputHour(bound).sendKeys(String.valueOf(t.getHour()));
        inputComp.rangeInputMinute(bound).sendKeys(String.valueOf(t.getMinute()));
        inputComp.rangeInputPeriod(bound).sendKeys(period);
        return this;
    }

    private void fillDateWithBound(String date, RangeBound bound) {
        String[] parts = DateHelper.validateAndSplitDate(date);

        typeInputSegment(inputComp.rangeInputMonth(bound), parts[0]);
        typeInputSegment(inputComp.rangeInputDay(bound), parts[1]);
        typeInputSegment(inputComp.rangeInputYear(bound), parts[2]);
    }

    public InputActions fillDateRangeInput(String startDate, String endDate) {
        fillDateWithBound(startDate, RangeBound.START);
        fillDateWithBound(endDate, RangeBound.END);
        return this;
    }

    public String getSingleDateInputTxt() {
        String month = inputComp.singleInputMonth().getText();
        String day = inputComp.singleInputDay().getText();
        String year = inputComp.singleInputYear().getText();

        return String.format("%02d-%02d-%s",
                Integer.parseInt(month),
                Integer.parseInt(day),
                year);
    }

    public String getRangeDateInputTxt(RangeBound bound) {
        String month = inputComp.rangeInputMonth(bound).getText();
        String day = inputComp.rangeInputDay(bound).getText();
        String year = inputComp.rangeInputYear(bound).getText();

        return String.format("%02d-%02d-%s",
                Integer.parseInt(month),
                Integer.parseInt(day),
                year);
    }

    public String getRangeTimeInputTxt(RangeBound bound) {
        int hour = Integer.parseInt(inputComp.rangeInputHour(bound).getText());
        int minute = Integer.parseInt(inputComp.rangeInputMinute(bound).getText());

        return String.format("%02d:%02d", hour, minute);
    }

    public String getRangePeriodInputTxt(RangeBound bound) {
        return inputComp.rangeInputPeriod(bound).getText();
    }

    public InputActions clickPopupBtn() {
        inputComp.popupBtn().click();
        WaitUtils.waitForVisibility(inputComp.driver(), inputComp.popupDropdown());
        return this;
    }

    public InputActions selectDropdownOpt(DropdownOption option) {
        clickPopupBtn();

        WebElement popupDropdown = inputComp.popupDropdown();
        inputComp.popoverOption(option).click();
        WaitUtils.waitForInvisibility(inputComp.driver(), popupDropdown);

        return this;
    }

    public InputActions selectListBoxOption(DropdownOption option) {
        inputComp.option(option).click();
        return this;
    }

    public InputActions selectCheckbox() {
        inputComp.checkbox().click();
        return this;
    }

    public InputActions selectListBoxOptions(List<DropdownOption> options) {
        for (DropdownOption option : options) {
            selectListBoxOption(option);
        }

        return this;
    }

    public InputActions selectInputRating(int ratingValue) {
        inputComp.ratingItem(ratingValue).click();
        return this;
    }

    public InputActions clickSwitchToogle() {
        inputComp.switchToggle().click();
        return this;
    }

    public InputActions clickCheckboxGroup(DropdownOption option) {
        inputComp.groupBtn(option).click();
        return this;
    }

    public InputActions selectMultiDropdownOpt(List<DropdownOption> options) {
        clickPopupBtn();

        for (DropdownOption option : options) {
            WebElement ele = inputComp.popoverOption(option);
            if ("unchecked".equalsIgnoreCase(ele.getAttribute("data-state"))) {
                ele.click();

                new WebDriverWait(inputComp.driver(), Duration.ofSeconds(5))
                        .until(d -> "checked".equalsIgnoreCase(
                                inputComp.popoverOption(option).getAttribute("data-state")));

            }
        }
        inputComp.actions().sendKeys(Keys.ESCAPE).perform();
        return this;
    }

    public InputActions setSliderTo(int target) {
        WebElement thumb = inputComp.slider();
        thumb.click();
        thumb.sendKeys(Keys.HOME);
        int current = 0;
        while (current < target) {
            thumb.sendKeys(Keys.ARROW_RIGHT);
            current++;
        }
        return this;
    }

    public List<String> selectDropdownOptionsInOrder(List<DropdownOption> options) {
        List<String> actualValues = new ArrayList<>();

        for (DropdownOption option : options) {
            String dropdownValue = inputComp.inputEle().getAttribute("value");

            if (option.label().equalsIgnoreCase(dropdownValue)) {
                actualValues.add(dropdownValue);
                continue;
            }

            selectDropdownOpt(option);

            actualValues.add(inputComp.inputEle().getAttribute("value"));
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

    public List<String> getSelectedTagsItem() {
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

    private InputActions stepInputTo(String value, WebElement stepBtn) {
        while (!inputComp.inputEle().getAttribute("value").equalsIgnoreCase(value)) {
            stepBtn.click();
        }
        return this;
    }

    public InputActions increaseInputTo(String value) {
        return stepInputTo(value, inputComp.increaseBtn());
    }

    public InputActions decreaseInputTo(String value) {
        return stepInputTo(value, inputComp.decreaseBtn());
    }

    public InputAssertions verify() {
        return new InputAssertions(inputComp, this);
    }
}
