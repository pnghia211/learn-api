package actions;

import assertions.InputAssertions;
import component.main.form.InputComp;
import model.CardMaskData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InputActions {
    private final InputComp inputComp;

    public InputActions(InputComp inputComp) {
        this.inputComp = inputComp;
    }

    public WebElement typeInput() {
        return inputComp.textInput();
    }

    public InputActions uploadFile(String filePath) {
        inputComp.uploadFileInput().sendKeys(filePath);
        return this;
    }

    public InputActions type(String input) {
        clearAndType(typeInput(), input);
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

    public InputAssertions verify() {
        return new InputAssertions(inputComp, this);
    }
}
