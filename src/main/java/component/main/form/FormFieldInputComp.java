package component.main.form;

import data.FormFieldLabel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FormFieldInputComp extends InputComp {
    private final FormComp formComp;
    private final FormFieldLabel fieldLabel;

    public FormFieldInputComp(WebDriver driver, FormComp formComp, FormFieldLabel fieldLabel) {
        super(driver, null);
        this.formComp = formComp;
        this.fieldLabel = fieldLabel;
    }

    @Override
    protected WebElement resolveRoot() {
        return formComp.fieldWrapper(fieldLabel);
    }
}
