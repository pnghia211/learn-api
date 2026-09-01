package component.main.factory;

import actions.FormActions;
import actions.InputActions;
import component.main.BaseComp;
import component.main.form.FormComp;
import component.main.form.InputComp;
import org.openqa.selenium.WebDriver;

public class FormFactory extends BaseComp {
    public FormFactory(WebDriver driver) {
        super(driver);
    }

    public FormActions forForm(String formLabel) {
        return new FormActions(new FormComp(driver, formLabel));
    }
}
