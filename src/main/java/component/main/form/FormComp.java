package component.main.form;

import component.main.BaseComp;
import data.FormFieldLabel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FormComp extends BaseComp {
    private WebElement cachedRoot;
    private final String formLabel;
    private String wrapper = "//*[self::label or self::legend][.='%s']/ancestor::*[@data-slot='root'][./div[@data-slot='wrapper']]";
    private By submit = By.cssSelector("button[type='submit']");
    private By toastTitleSel = By.cssSelector("ol [data-slot='title']");

    public FormComp(WebDriver driver, String formLabel) {
        super(driver);
        this.formLabel = formLabel;
    }

    public WebElement formByLabel() {
        cachedRoot = getOrRefreshCached(cachedRoot, () -> getRootComp(formLabel));
        return cachedRoot;
    }

    public WebElement fieldWrapper(FormFieldLabel formField) {
        return formByLabel().findElement(By.xpath(String.format(wrapper, formField.label())));
    }

    public InputComp fieldInputComp(FormFieldLabel fieldLabel) {
        return new FormFieldInputComp(driver, this, fieldLabel);
    }

    public WebElement submit() {
        return formByLabel().findElement(submit);
    }

    public WebElement toastTitle() {
        return driver.findElement(toastTitleSel);
    }
}
