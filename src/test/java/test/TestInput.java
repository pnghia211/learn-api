package test;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.factory.InputFactory;
import data.DropdownOption;
import driver.DriverFactory;
import model.CardMaskData;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import page.HomePage;

import static url.Url.mainPage;

public class TestInput {
    private WebDriver driver;
    private LeftNavigatorComp leftNavigatorComp;
    private InputFactory inputComp;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.getChromeDriver();
        driver.get(mainPage);

        HomePage homePage = new HomePage(driver);
        HeaderComp headerComp = homePage.componentsSection();
        leftNavigatorComp = homePage.leftNavigatorComp();
        inputComp = homePage.inputComp();

        headerComp.clickComponentsComp();
        leftNavigatorComp.clickDataTableComp("input");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void uploadFile_showsFileUploaded() {
        String filePath = "C:\\Users\\ADMIN\\Desktop\\dummy-png-image.png";
        inputComp.forInput("type")
                .uploadFile(filePath)
                .verify().hasFileUploaded();
    }

    @Test
    public void clearButton_clearsTypedText() {
        inputComp.forInput("with-clear-button")
                .verify().valueEquals("Click to clear").and()
                .type("text clear button")
                .verify().valueEquals("text clear button")
                .and().clickClearBtn()
                .verify().inputIsEmpty();
    }

    @Test
    public void passwordToggle_showsAndHidesPassword() {
        inputComp.forInput("with-password-toggle")
                .verify().inputIsEmpty()
                .and().type("password test")
                .verify().valueEquals("password test").inputIsHidden()
                .and().clickShowPasswordBtn()
                .verify().inputIsVisible();
    }

    @Test
    public void passwordStrengthIndicator_progressesThroughAllLevels() {
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
                .pwdRequirementMet("At least 1 number",
                        "At least 8 characters",
                        "At least 1 lowercase letter")
                .pwdRequirementNotMet("At least 1 uppercase letter")

                .and().type("Asdqwez1")
                .verify()
                .indicatorValue("4")
                .pwdStrengthRequirement("Strong password. Must contain:")
                .pwdRequirementMet("At least 1 number",
                        "At least 8 characters",
                        "At least 1 lowercase letter",
                        "At least 1 uppercase letter");
    }

    @Test
    public void maskInput_fillsAndVerifiesCardFields() {
        CardMaskData validCard = new CardMaskData("4242 4242 4242 4242", "12/25", "123");
        inputComp.forInput("with-mask")
                .fillMaskInputFields(validCard)
                .verify().maskInputFieldsEqual(validCard);
    }

    @Test
    public void singleDateInput_acceptsAndDisplaysDate() {
        leftNavigatorComp.clickDataTableComp("input-date");
        inputComp.forInput("usage").fillOneDateBound("01-21-1995")
                .verify().dateSingleInput("01-21-1995");
    }

    @Test
    public void dateRangeInput_acceptsAndDisplaysRange() {
        inputComp.forInput("range").fillDateRangeInput("01-21-1995", "12-12-2026")
                .verify().dateRangeInput("01-21-1995", "12-12-2026");
    }

    @Test
    public void defaultUsage_showsOptionsInOrder() {
        leftNavigatorComp.clickDataTableComp("input-menu");
        DropdownOption[] opts = {DropdownOption.BACKLOG, DropdownOption.TO_DO,
                DropdownOption.IN_PROGRESS, DropdownOption.DONE};
        inputComp.forInput("usage").verify().selectedOptionsInOrder(opts);
    }

    @Test
    public void multiSelect_selectAndRemoveOptions() {
        DropdownOption[] opts = {DropdownOption.BACKLOG, DropdownOption.TO_DO,
                DropdownOption.IN_PROGRESS, DropdownOption.DONE};
        inputComp.forInput("multiple").selectMultiDropdownOpt(opts)
                .removeOption(DropdownOption.BACKLOG, DropdownOption.TO_DO, DropdownOption.DONE)
                .verify().selectedOptions(DropdownOption.IN_PROGRESS);
    }

    @Test
    public void countryPicker_selectsVietnam() {
        inputComp.forInput("as-a-country-picker")
                .verify().countryPickerDefault()
                .and().selectDropdownOpt("Vietnam")
                .verify().selectedCountryInput("VN", "Vietnam");
    }

    @Test
    public void otpInput_acceptsAndVerifiesDigits() {
        leftNavigatorComp.clickDataTableComp("pin-input");
        inputComp.forInput("otp").typeInputs("12345")
                .verify().inputForm("12345");
    }

    @Test
    public void separatorInput_acceptsAndVerifiesDigits() {
        inputComp.forInput("separator").typeInputs("123456")
                .verify().inputForm("123456");
    }
}
