package assertions;

import actions.InputActions;
import component.main.form.InputComp;
import data.DropdownOption;
import model.CardMaskData;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import component.main.form.InputComp.*;

public class InputAssertions {
    private final InputComp inputComp;
    private final InputActions actions;

    public InputAssertions(InputComp parent, InputActions actions) {
        this.inputComp = parent;
        this.actions = actions;
    }

    public InputAssertions hasFileUploaded() {
        Long fileCount = (Long) ((JavascriptExecutor) inputComp.driver()).executeScript(
                "return arguments[0].files.length;", inputComp.uploadFileInput());

        assertTrue(fileCount != null && fileCount > 0);
        return this;
    }

    public InputAssertions valueEquals(String expected) {
        String actual = actions.typeInput().getDomProperty("value");
        assertEquals(expected, actual);
        return this;
    }

    public InputAssertions inputIsEmpty() {
        assertTrue(actions.typeInput().getDomProperty("value").isEmpty());
        return this;
    }

    public InputAssertions inputIsHidden() {
        assertTrue(actions.typeInput().getAttribute("type").equalsIgnoreCase("password"));
        return this;
    }

    public InputAssertions inputIsVisible() {
        assertTrue(actions.typeInput().getAttribute("type").equalsIgnoreCase("text"));
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

    public InputAssertions selectedOptionsInOrder(DropdownOption... expected) {
        List<String> actual = actions.selectDropdownOptionsInOrder(expected);

        List<String> expectedLabels = Arrays.stream(expected)
                .map(DropdownOption::label)
                .toList();

        assertEquals(actual, expectedLabels);

        return this;
    }

    public InputAssertions selectedOptions(DropdownOption... expected) {
        List<String> actual = actions.getSelectedOptions();

        List<String> expectedLabels = Arrays.stream(expected)
                .map(DropdownOption::label)
                .toList();

        assertEquals(actual, expectedLabels);

        return this;
    }

    public InputActions and() {
        return actions;
    }
}
