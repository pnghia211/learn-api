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

public class InputActions extends BaseActions<InputComp> {
    public InputActions(InputComp inputComp) {
        super(inputComp);
    }

    public WebElement getInputText() {
        return getComp().inputEle();
    }

    public WebElement getSelectText() {
        return getComp().selectEle();
    }

    public WebElement getCountryCodeInput() {
        return getComp().countryCodeInput();
    }

    public InputActions uploadFile(String filePath) {
        getComp().uploadFileInput().sendKeys(filePath);
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
        clearAndType(getComp().textArea(), value);
        return this;
    }

    public String getTextArea() {
        return getComp().textArea().getDomProperty("value");
    }

    public List<String> getTagItemsTxt() {
        return getComp().tagsItem().stream().map(WebElement::getText).toList();
    }

    public InputActions clickClearBtn() {
        getComp().clearBtn().click();
        return this;
    }

    public InputActions clickShowPasswordBtn() {
        getComp().showPasswordBtn().click();
        return this;
    }

    public String getIndicatorValue() {
        return getComp().indicator().getAttribute("data-value");
    }

    public String getPwdStrengthRequirementTxt() {
        return getComp().pwdRequirement().getText();
    }

    public InputActions fillMaskInputFields(CardMaskData data) {
        clearAndType(getComp().creditCardInput(), data.cardNumber());
        clearAndType(getComp().calendarInput(), data.expiry());
        clearAndType(getComp().cvcInput(), data.cvc());
        return this;
    }

    protected void clearAndType(WebElement element, String input) {
        element.clear();
        element.sendKeys(input);
    }

    protected void typeAndEnter(WebElement element, String input) {
        element.sendKeys(input);
        getComp().actions().sendKeys(Keys.ENTER).perform();
    }

    public void inputMenus(List<DropdownOption> options) {
        for (DropdownOption option : options) {
            typeAndEnter(getComp().inputEle(), option.label());
        }
        getComp().actions().sendKeys(Keys.ESCAPE).perform();
    }

    public void inputMenu(DropdownOption option) {
        typeAndEnter(getComp().inputEle(), option.label());
    }

    protected void typeInputSegment(WebElement element, String input) {
        for (char c : input.toCharArray()) {
            element.sendKeys(String.valueOf(c));
        }
    }

    public InputActions fillDate(String date) {
        String[] parts = DateHelper.validateAndSplitDate(date);

        typeInputSegment(getComp().singleInputMonth(), parts[0]);
        typeInputSegment(getComp().singleInputDay(), parts[1]);
        typeInputSegment(getComp().singleInputYear(), parts[2]);

        return this;
    }

    public InputActions fillTime(String timeInput, String period) {
        LocalTime time = LocalTime.parse(timeInput);
        String hour = String.valueOf(time.getHour());
        String minute = String.valueOf(time.getMinute());

        getComp().singleHourInput().sendKeys(hour);
        getComp().singleMinuteInput().sendKeys(minute);
        getComp().singleDayPeriodInput().sendKeys(period);

        return this;
    }

    public InputActions fillTimeRangeInput(RangeBound bound, String time, String period) {
        LocalTime t = LocalTime.parse(time);
        getComp().rangeInputHour(bound).sendKeys(String.valueOf(t.getHour()));
        getComp().rangeInputMinute(bound).sendKeys(String.valueOf(t.getMinute()));
        getComp().rangeInputPeriod(bound).sendKeys(period);
        return this;
    }

    private void fillDateWithBound(String date, RangeBound bound) {
        String[] parts = DateHelper.validateAndSplitDate(date);

        typeInputSegment(getComp().rangeInputMonth(bound), parts[0]);
        typeInputSegment(getComp().rangeInputDay(bound), parts[1]);
        typeInputSegment(getComp().rangeInputYear(bound), parts[2]);
    }

    public InputActions fillDateRangeInput(String startDate, String endDate) {
        fillDateWithBound(startDate, RangeBound.START);
        fillDateWithBound(endDate, RangeBound.END);
        return this;
    }

    public String getSingleDateInputTxt() {
        String month = getComp().singleInputMonth().getText();
        String day = getComp().singleInputDay().getText();
        String year = getComp().singleInputYear().getText();

        return String.format("%02d-%02d-%s",
                Integer.parseInt(month),
                Integer.parseInt(day),
                year);
    }

    public String getRangeDateInputTxt(RangeBound bound) {
        String month = getComp().rangeInputMonth(bound).getText();
        String day = getComp().rangeInputDay(bound).getText();
        String year = getComp().rangeInputYear(bound).getText();

        return String.format("%02d-%02d-%s",
                Integer.parseInt(month),
                Integer.parseInt(day),
                year);
    }

    public String getRangeTimeInputTxt(RangeBound bound) {
        int hour = Integer.parseInt(getComp().rangeInputHour(bound).getText());
        int minute = Integer.parseInt(getComp().rangeInputMinute(bound).getText());

        return String.format("%02d:%02d", hour, minute);
    }

    public String getRangePeriodInputTxt(RangeBound bound) {
        return getComp().rangeInputPeriod(bound).getText();
    }

    public InputActions clickPopupBtn() {
        getComp().popupBtn().click();
        WaitUtils.waitForVisibility(getComp().driver(), getComp().popupDropdown());
        return this;
    }

    public InputActions selectDropdownOpt(DropdownOption option) {
        clickPopupBtn();

        WebElement popupDropdown = getComp().popupDropdown();
        getComp().popoverOption(option).click();
        WaitUtils.waitForInvisibility(getComp().driver(), popupDropdown);

        return this;
    }

    public InputActions selectListBoxOption(DropdownOption option) {
        getComp().option(option).click();
        return this;
    }

    public InputActions selectCheckbox() {
        getComp().checkbox().click();
        return this;
    }

    public InputActions selectListBoxOptions(List<DropdownOption> options) {
        for (DropdownOption option : options) {
            selectListBoxOption(option);
        }

        return this;
    }

    public InputActions selectInputRating(int ratingValue) {
        getComp().ratingItem(ratingValue).click();
        return this;
    }

    public InputActions clickSwitchToggle() {
        getComp().switchToggle().click();
        return this;
    }

    public InputActions clickCheckboxGroup(DropdownOption option) {
        getComp().groupBtn(option).click();
        return this;
    }

    public InputActions selectMultiDropdownOpt(List<DropdownOption> options) {
        clickPopupBtn();

        for (DropdownOption option : options) {
            WebElement ele = getComp().popoverOption(option);
            if ("unchecked".equalsIgnoreCase(ele.getAttribute("data-state"))) {
                ele.click();

                new WebDriverWait(getComp().driver(), Duration.ofSeconds(5))
                        .until(d -> "checked".equalsIgnoreCase(
                                getComp().popoverOption(option).getAttribute("data-state")));

            }
        }
        getComp().actions().sendKeys(Keys.ESCAPE).perform();
        return this;
    }

    public InputActions setSliderTo(int target) {
        WebElement thumb = getComp().slider();
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
            String dropdownValue = getComp().inputEle().getAttribute("value");

            if (option.label().equalsIgnoreCase(dropdownValue)) {
                actualValues.add(dropdownValue);
                continue;
            }

            selectDropdownOpt(option);

            actualValues.add(getComp().inputEle().getAttribute("value"));
        }
        return actualValues;
    }

    public InputActions removeOption(DropdownOption... options) {
        getComp().actions().sendKeys(Keys.ESCAPE).perform();
        for (DropdownOption option : options) {
            getComp().deleteIconByItem(option.label()).click();
        }
        return this;
    }

    public List<String> getSelectedTagsItem() {
        return new ArrayList<>(getComp().tagsItem().stream().map(WebElement::getText).toList());
    }

    public InputActions typeInputs(String input) {
        List<WebElement> elements = getComp().pinInputs();
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
        while (!getComp().inputEle().getAttribute("value").equalsIgnoreCase(value)) {
            stepBtn.click();
        }
        return this;
    }

    public InputActions increaseInputTo(String value) {
        return stepInputTo(value, getComp().increaseBtn());
    }

    public InputActions decreaseInputTo(String value) {
        return stepInputTo(value, getComp().decreaseBtn());
    }

    public InputAssertions verify() {
        return new InputAssertions(getComp(), this);
    }
}
