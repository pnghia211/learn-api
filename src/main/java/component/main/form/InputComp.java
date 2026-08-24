package component.main.form;

import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InputComp extends BaseComp {
    private WebElement cachedRoot;
    private final String inputLabel;
    private By uploadFileInputSel = By.cssSelector("input[type='file']");
    private By textInputSel = By.cssSelector("input");
    private By clearBtnSel = By.cssSelector("button[aria-label='Clear input']");
    private By passwordInputSel = By.cssSelector("input[type='password']");
    private By showPasswordBtnSel = By.cssSelector("button[aria-label='Show password']");
    private By indicatorSel = By.cssSelector("[data-slot='indicator']");
    private By pwdStrengthRequirementSel = By.cssSelector("#password-strength");
    private By pwdRequirementList = By.cssSelector("ul li");
    private By creditCardSel = By.cssSelector("input[placeholder*='4242']");
    private By calendarSel = By.cssSelector("input[placeholder*='MM/YY']");
    private By cvcSel = By.cssSelector("input[placeholder*='CVC']");

    public InputComp(WebDriver driver, String inputLabel) {
        super(driver);
        this.inputLabel = inputLabel;
    }

    public WebElement inputByLabel() {
        cachedRoot = getOrRefreshCached(cachedRoot, () -> getRootComp(inputLabel));
        return cachedRoot;
    }

    public WebElement uploadFileInput() {
        return inputByLabel().findElement(uploadFileInputSel);
    }

    public WebElement textInput() {
        return inputByLabel().findElement(textInputSel);
    }

    public WebElement passwordInput() {
        return inputByLabel().findElement(passwordInputSel);
    }

    public WebElement clearBtn() {
        return inputByLabel().findElement(clearBtnSel);
    }

    public WebElement showPasswordBtn() {
        return inputByLabel().findElement(showPasswordBtnSel);
    }

    public WebElement indicator() {
        return inputByLabel().findElement(indicatorSel);
    }

    public WebElement pwdRequirement() {
        return inputByLabel().findElement(pwdStrengthRequirementSel);
    }

    public List<WebElement> pwdRequirementList() {
        return inputByLabel().findElements(pwdRequirementList);
    }

    public WebElement creditCardInput() {
        return inputByLabel().findElement(creditCardSel);
    }

    public WebElement calendarInput() {
        return inputByLabel().findElement(calendarSel);
    }

    public WebElement cvcInput() {
        return inputByLabel().findElement(cvcSel);
    }
}
