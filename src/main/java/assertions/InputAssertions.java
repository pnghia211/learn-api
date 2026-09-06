package assertions;

import actions.InputActions;
import component.main.form.InputComp;
import component.main.form.InputComp.RangeBound;
import data.DropdownOption;
import model.CardMaskData;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class InputAssertions {
    private final InputComp inputComp;
    private final InputActions actions;

    public
    InputAssertions(InputComp parent, InputActions actions) {
        this.inputComp = parent;
        this.actions = actions;
    }

    public InputAssertions hasFileUploaded() {
        Long fileCount = (Long) ((JavascriptExecutor) inputComp.driver()).executeScript(
                "return arguments[0].files.length;", inputComp.uploadFileInput());

        assertTrue(fileCount != null && fileCount > 0);
        return this;
    }

    public InputAssertions inputValueEquals(String expected) {
        String actual = actions.getInputText().getDomProperty("value");
        assertEquals(actual, expected);
        return this;
    }

    public InputAssertions selectValueEquals(String expected) {
        String actual = actions.getSelectText().getText();
        assertEquals(actual, expected);
        return this;
    }

    public InputAssertions textAreaEquals(String expected) {
        String actual = actions.getTextArea();
        assertEquals(actual, expected);
        return this;
    }

    public InputAssertions tagItems(List<String> expected) {
        List<String> actual = actions.getTagItemsTxt();

        assertEquals(actual, expected);
        return this;
    }

    public InputAssertions inputIsEmpty() {
        assertTrue(actions.getInputText().getDomProperty("value").isEmpty());
        return this;
    }

    public InputAssertions inputIsHidden() {
        assertTrue(actions.getInputText().getAttribute("type").equalsIgnoreCase("password"));
        return this;
    }

    public InputAssertions inputIsVisible() {
        assertTrue(actions.getInputText().getAttribute("type").equalsIgnoreCase("text"));
        return this;
    }

    public InputAssertions indicatorValue(String expected) {
        assertEquals(expected, actions.getIndicatorValue());
        return this;
    }

    public InputAssertions pwdStrengthRequirement(String expected) {
        assertEquals(expected, actions.getPwdStrengthRequirementTxt());
        return this;
    }

    public InputAssertions pwdRequirementMet(String... expected) {
        return assertRequirementClass(expected, "text-success");
    }

    public InputAssertions pwdRequirementNotMet(String... expected) {
        return assertRequirementClass(expected, "text-muted");
    }

    private InputAssertions assertRequirementClass(String[] expected, String expectedClass) {
        List<WebElement> items = inputComp.pwdRequirementList();

        for (String s : expected) {
            WebElement li = items.stream()
                    .filter(e -> e.getText().contains(s))
                    .findFirst()
                    .orElseThrow();

            assertTrue(li.getAttribute("class").contains(expectedClass));
        }
        return this;
    }

    public InputAssertions maskInputFieldsEqual(CardMaskData expected) {
        assertEquals(expected.cardNumber(), inputComp.creditCardInput().getDomProperty("value"));
        assertEquals(expected.expiry(), inputComp.calendarInput().getDomProperty("value"));
        assertEquals(expected.cvc(), inputComp.cvcInput().getDomProperty("value"));
        return this;
    }

    public InputAssertions dateSingleInput(String expected) {
        String actual = actions.getSingleDateInputTxt();
        assertEquals(expected, actual);
        return this;
    }

    public InputAssertions dateRangeInput(String expectedStart, String expectedEnd) {
        String actualStart = actions.getRangeDateInputTxt(RangeBound.START);
        String actualEnd = actions.getRangeDateInputTxt(RangeBound.END);
        assertEquals(expectedStart, actualStart);
        assertEquals(expectedEnd, actualEnd);
        return this;
    }

    public InputAssertions timeRangeInput(RangeBound bound, String expectedTime, String expectedPeriod) {
        assertEquals(expectedTime, actions.getRangeTimeInputTxt(bound));
        assertEquals(expectedPeriod, actions.getRangePeriodInputTxt(bound));
        return this;
    }

    public InputAssertions selectedOptionsInOrder(List<DropdownOption> expected) {
        List<String> actual = actions.selectDropdownOptionsInOrder(expected);

        List<String> expectedLabels = expected.stream()
                .map(DropdownOption::label)
                .toList();

        assertEquals(actual, expectedLabels);

        return this;
    }

    public InputAssertions selectedCountryInput(String code, String country) {
        String actualCountry = actions.getInputText().getAttribute("value");
        String actualCountryCode = actions.getCountryCodeInput().getText();

        assertEquals(countryCodeToEmoji(code), actualCountryCode);
        assertEquals(country, actualCountry);

        return this;
    }

    private String countryCodeToEmoji(String countryCode) {
        String code = countryCode.toUpperCase();
        int firstChar = Character.codePointAt(code, 0) - 0x41 + 0x1F1E6;
        int secondChar = Character.codePointAt(code, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
    }

    public InputAssertions selectedTagsItem(DropdownOption... expected) {
        List<String> actual = actions.getSelectedTagsItem();

        List<String> expectedLabels = Arrays.stream(expected)
                .map(DropdownOption::label)
                .toList();

        assertEquals(actual, expectedLabels);

        return this;
    }

    public InputAssertions pinInput(String expected) {
        StringBuilder actual = new StringBuilder();
        for (WebElement element : inputComp.pinInputs()) {
            actual.append(element.getAttribute("value"));
        }

        assertEquals(expected, actual.toString());
        return this;
    }

    public InputAssertions timeValue(String expected, String expectedPeriod) {
        LocalTime time = LocalTime.parse(expected);
        String hourExpected = String.valueOf(time.getHour());
        String minuteExpected = String.valueOf(time.getMinute());

        assertEquals(inputComp.singleHourInput().getText(), hourExpected);
        assertEquals(inputComp.singleMinuteInput().getText(), minuteExpected);
        assertEquals(inputComp.singleDayPeriodInput().getText(), expectedPeriod);
        return this;
    }

    public InputAssertions countryPickerDefault() {
        assertTrue("Select country".equalsIgnoreCase(inputComp.inputEle().getAttribute("placeholder")));
        return this;
    }

    public InputActions and() {
        return actions;
    }
}
