package component.main.form;

import actions.InputActions;
import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InputComp extends BaseComp {
    private By inputSel = By.cssSelector("input[type='file']");

    public InputComp(WebDriver driver) {
        super(driver);
    }

    public WebElement fileInput(String label) {
        return getComponentBasedOnHeader(label, inputSel);
    }

    public InputActions forInput(String inputLabel) {
        return new InputActions(this, inputLabel);
    }
}
