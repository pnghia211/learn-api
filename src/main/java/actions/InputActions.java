package actions;

import assertions.InputAssertions;
import component.main.form.InputComp;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InputActions {
    private final WebDriver driver;
    private final InputComp inputComp;
    public final String inputLabel;

    public InputActions(WebDriver driver, String inputLabel) {
        this.driver = driver;
        this.inputComp = new InputComp(driver, inputLabel);
        this.inputLabel = inputLabel;
    }

    public WebElement typeInput() {
        return inputComp.textInput();
    }

    public WebElement passwordInput() {
        return inputComp.passwordInput();
    }

    public InputActions uploadFile(String filePath) {
        inputComp.uploadFileInput().sendKeys(filePath);
        return this;
    }

    public InputActions type(String input) {
        WebElement typeInput = typeInput();
        typeInput.clear();
        typeInput.sendKeys(input);
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

    public WebElement getRequirementItemByTxt(String expected) {
        List<WebElement> items = inputComp.pwdRequirementList();
        return items.stream().filter(i -> i.getText().contains(expected))
                .findFirst().orElseThrow();
    }

    public InputAssertions verify() {
        return new InputAssertions(inputComp, this);
    }
}
