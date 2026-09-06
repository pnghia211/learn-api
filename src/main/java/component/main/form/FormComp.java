package component.main.form;

import component.main.BaseComp;
import data.FormFieldLabel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FormComp extends BaseComp {
    private WebElement cachedRoot;
    private final String formLabel;
    private String wrapper = "//label[.='%s']/ancestor::*[@data-slot='wrapper']/following-sibling::div";
    private By selectSel = By.cssSelector("span[data-slot='value']");

    public FormComp(WebDriver driver, String formLabel) {
        super(driver);
        this.formLabel = formLabel;
    }

    public WebElement formByLabel() {
        cachedRoot = getOrRefreshCached(cachedRoot, () -> getRootComp(formLabel));
        return cachedRoot;
    }

    public WebElement baseComp(FormFieldLabel formField) {
        return formByLabel().findElement(By.xpath(String.format(wrapper, formField.label())));
    }

    public InputComp fieldInputComp(FormFieldLabel fieldLabel) {
        return new FormFieldInputComp(driver, this, fieldLabel);
    }

    public WebElement selectMultipleEle() {
        return baseComp(FormFieldLabel.SELECT_MULTIPLE).findElement(selectSel);
    }
}
