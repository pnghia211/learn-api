package component.main.factory;

import actions.InputActions;
import component.main.BaseComp;
import component.main.form.InputComp;
import org.openqa.selenium.WebDriver;

public class InputFactory extends BaseComp {
    public InputFactory(WebDriver driver) {
        super(driver);
    }

    public InputActions forInput(String inputLabel) {
        return new InputActions(new InputComp(driver, inputLabel));
    }
}
