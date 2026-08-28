package test;

import component.main.factory.InputFactory;
import data.DropdownOption;
import driver.DriverFactory;
import model.CardMaskData;
import org.openqa.selenium.WebDriver;
import page.HomePage;

import static url.Url.mainPage;

public class TestInput {
    private final static WebDriver driver = DriverFactory.getChromeDriver();

    public static void main(String[] args) throws InterruptedException {
        driver.get(mainPage);

        HomePage homePage = new HomePage(driver);
        homePage.componentsSection().clickComponentsComp();
        homePage.leftNavigatorComp().clickDataTableComp("input");
        InputFactory inputComp = homePage.inputComp();
        String filePath = "C:\\Users\\ADMIN\\Desktop\\dummy-png-image.png";

        inputComp.forInput("type")
                .uploadFile(filePath)
                .verify().hasFileUploaded();

        inputComp.forInput("with-clear-button")
                .verify()
                .valueEquals("Click to clear").and()
                .type("text clear button")
                .verify().valueEquals("text clear button")
                .and().clickClearBtn()
                .verify().inputIsEmpty();

        inputComp.forInput("with-password-toggle")
                .verify().inputIsEmpty()
                .and().type("password test")
                .verify().valueEquals("password test").inputIsHidden()
                .and().clickShowPasswordBtn()
                .verify().inputIsVisible();

        inputComp.forInput("with-password-strength-indicator")
                .verify()
                .inputIsEmpty()
                .indicatorValue("0")
                .pwdStrengthRequirement("Enter a password. Must contain:")
                .pwdRequirementNotMet("At least 8 characters",
                        "At least 1 number",
                        "At least 1 lowercase letter",
                        "At least 1 uppercase letter")

                .and().type("a")
                .verify()
                .indicatorValue("1")
                .pwdStrengthRequirement("Weak password. Must contain:")
                .pwdRequirementMet("At least 1 lowercase letter")
                .pwdRequirementNotMet("At least 1 number")

                .and().type("1")
                .verify()
                .indicatorValue("1")
                .pwdStrengthRequirement("Weak password. Must contain:")
                .pwdRequirementMet("At least 1 number")
                .pwdRequirementNotMet("At least 8 characters")

                .and().type("12345678")
                .verify()
                .indicatorValue("2")
                .pwdStrengthRequirement("Weak password. Must contain:")
                .pwdRequirementMet("At least 1 number", "At least 8 characters")
                .pwdRequirementNotMet("At least 1 lowercase letter")

                .and().type("asdqwez1")
                .verify()
                .indicatorValue("3")
                .pwdStrengthRequirement("Medium password. Must contain:")
                .pwdRequirementMet("At least 1 number"
                        , "At least 8 characters"
                        , "At least 1 lowercase letter")
                .pwdRequirementNotMet("At least 1 uppercase letter")

                .and().type("Asdqwez1")
                .verify()
                .indicatorValue("4")
                .pwdStrengthRequirement("Strong password. Must contain:")
                .pwdRequirementMet("At least 1 number"
                        , "At least 8 characters"
                        , "At least 1 lowercase letter"
                        , "At least 1 uppercase letter");

        CardMaskData validCard = new CardMaskData("4242 4242 4242 4242", "12/25", "123");

        inputComp.forInput("with-mask")
                .fillMaskInputFields(validCard)
                .verify().maskInputFieldsEqual(validCard);

        // Verify input date
        homePage.leftNavigatorComp().clickDataTableComp("input-date");

        inputComp.forInput("usage").fillOneDateBound("01-21-1995")
                .verify().dateSingleInput("01-21-1995");

        inputComp.forInput("range").fillDateRangeInput("01-21-1995", "12-12-2026")
                .verify().dateRangeInput("01-21-1995", "12-12-2026");

        // Verify input menu
        homePage.leftNavigatorComp().clickDataTableComp("input-menu");

        DropdownOption[] opts = {DropdownOption.BACKLOG, DropdownOption.TO_DO, DropdownOption.IN_PROGRESS, DropdownOption.DONE};
        inputComp.forInput("usage").verify().selectedOptionsInOrder(opts);

        inputComp.forInput("multiple").selectMultiDropdownOpt(opts)
                .removeOption(DropdownOption.BACKLOG, DropdownOption.TO_DO, DropdownOption.DONE)
                .verify().selectedOptions(DropdownOption.IN_PROGRESS);

        inputComp.forInput("as-a-country-picker")
                .verify().countryPickerDefault()
                .and().selectDropdownOpt("Vietnam")
                .verify().selectedCountryInput("VN", "Vietnam");

        // Verify pin input
        homePage.leftNavigatorComp().clickDataTableComp("pin-input");

        inputComp.forInput("otp").typeInputs("12345")
                .verify().inputForm("12345");

        inputComp.forInput("separator").typeInputs("123456")
                .verify().inputForm("123456");

        Thread.sleep(5000);
        driver.quit();
    }
}
