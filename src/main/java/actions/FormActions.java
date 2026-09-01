package actions;

import component.main.form.FormComp;
import org.openqa.selenium.WebElement;

public class FormActions {
    FormComp formComp;
    InputActions inputActions;

    public FormActions(FormComp formComp) {
        this.formComp = formComp;
    }

    public InputActions inputActions() {
        if (inputActions == null) inputActions = new InputActions(formComp.inputComp());
        return inputActions;
    }

    public FormActions fillInput(String text) {
        formComp.input().sendKeys(text);
        return this;
    }

    public FormActions fillInputNumber(String number) {
        WebElement inputNumber = formComp.inputNumber();
        WebElement incButton = formComp.inputNumberIncrementBtn();

        while (!inputNumber.getAttribute("value").equalsIgnoreCase(number)) {
            incButton.click();
        }
        return this;
    }

    public void fillInputDate(String date) {
        inputActions().fillDate(date);
    }
}
