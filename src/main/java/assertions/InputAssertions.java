package assertions;

import actions.InputActions;
import component.main.form.InputComp;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.*;

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

    public InputAssertions pwdRequirementMet(String expected) {
        WebElement li = actions.getRequirementItemByTxt(expected);
        assertTrue(li.getAttribute("class").contains("text-success"));
        return this;
    }

    public InputAssertions pwdRequirementNotMet(String expected) {
        WebElement li = actions.getRequirementItemByTxt(expected);
        assertTrue(li.getAttribute("class").contains("text-muted"));
        return this;
    }

    public InputActions and() {
        return actions;
    }
}
