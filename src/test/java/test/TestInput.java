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

import component.main.form.InputComp.*;

import java.util.List;

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
    public void input_uploadFile_showsFileUploaded() {
        String filePath = "C:\\Users\\ADMIN\\Desktop\\dummy-png-image.png";
        inputComp.forInput("type")
                .uploadFile(filePath)
                .verify().hasFileUploaded();
    }

    @Test
    public void input_clearButton_clearsTypedText() {
        inputComp.forInput("with-clear-button")
                .verify().inputValueEquals("Click to clear").and()
                .type("text clear button")
                .verify().inputValueEquals("text clear button")
                .and().clickClearBtn()
                .verify().inputIsEmpty();
    }

    @Test
    public void input_passwordToggle_showsAndHidesPassword() {
        inputComp.forInput("with-password-toggle")
                .verify().inputIsEmpty()
                .and().type("password test")
                .verify().inputValueEquals("password test").inputIsHidden()
                .and().clickShowPasswordBtn()
                .verify().inputIsVisible();
    }

    @Test
    public void input_passwordStrengthIndicator_progressesThroughAllLevels() {
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
    public void input_maskInput_fillsAndVerifiesCardFields() {
        CardMaskData validCard = new CardMaskData("4242 4242 4242 4242", "12/25", "123");
        inputComp.forInput("with-mask")
                .fillMaskInputFields(validCard)
                .verify().maskInputFieldsEqual(validCard);
    }

    @Test
    public void inputDate_singleDateInput_acceptsAndDisplaysDate() {
        leftNavigatorComp.clickDataTableComp("input-date");
        inputComp.forInput("usage").fillDate("01-21-1995")
                .verify().dateSingleInput("01-21-1995");
    }

    @Test
    public void inputDate_dateRangeInput_acceptsAndDisplaysRange() {
        inputComp.forInput("range").fillDateRangeInput("01-21-1995", "12-12-2026")
                .verify().dateRangeInput("01-21-1995", "12-12-2026");
    }

    @Test
    public void inputTime_timeInput_fillSingleInputTime() {
        leftNavigatorComp.clickDataTableComp("input-time");
        inputComp.forInput("usage")
                .fillTime("04:20", "PM")
                .verify().timeValue("04:20", "PM");
    }

    @Test
    public void inputTime_fillRangeInputTime() {
        inputComp.forInput("range")
                .fillTimeRangeInput(RangeBound.START, "08:30", "AM")
                .fillTimeRangeInput(RangeBound.END, "04:20", "PM")
                .verify()
                .timeRangeInput(RangeBound.START, "08:30", "AM")
                .timeRangeInput(RangeBound.END, "04:20", "PM");
    }

    @Test
    public void inputTags_addAndRemoveTags() {
        leftNavigatorComp.clickDataTableComp("input-tags");
        inputComp.forInput("usage")
                .inputTags("abc", "test")
                .verify().tagItems(List.of("Vue", "abc", "test"));
    }

    @Test
    public void inputMenu_usage_showsOptionsInOrder() {
        leftNavigatorComp.clickDataTableComp("input-menu");
        List<DropdownOption> opts = List.of(DropdownOption.BACKLOG, DropdownOption.TO_DO,
                DropdownOption.IN_PROGRESS, DropdownOption.DONE);
        inputComp.forInput("usage").verify().selectedOptionsInOrder(opts);
    }

    @Test
    public void inputMenu_multiple_selectAndRemoveOptions() {
        leftNavigatorComp.clickDataTableComp("input-menu");
        List<DropdownOption> opts = List.of(DropdownOption.BACKLOG, DropdownOption.TO_DO,
                DropdownOption.IN_PROGRESS, DropdownOption.DONE);
        inputComp.forInput("multiple").selectMultiDropdownOpt(opts)
                .removeOption(DropdownOption.BACKLOG, DropdownOption.TO_DO, DropdownOption.DONE)
                .verify().selectedTagsItem(DropdownOption.IN_PROGRESS);
    }

    @Test
    public void inputMenu_countryPicker_selectsVietnam() {
        inputComp.forInput("as-a-country-picker")
                .verify().countryPickerDefault()
                .and().selectDropdownOpt(DropdownOption.COUNTRY_VIETNAM)
                .verify().selectedCountryInput("VN", "Vietnam");
    }

    @Test
    public void inputNumber_increasesAndDecreasesValue() {
        leftNavigatorComp.clickDataTableComp("input-number");
        inputComp.forInput("usage")
                .increaseInputTo("10")
                .verify().inputValueEquals("10")
                .and().decreaseInputTo("-10")
                .verify().inputValueEquals("-10");
    }

    @Test
    public void inputNumber_increasesAndDecreasesValueOrientation() {
        inputComp.forInput("orientation")
                .increaseInputTo("50")
                .verify().inputValueEquals("50")
                .and().decreaseInputTo("-50")
                .verify().inputValueEquals("-50");
    }

    @Test
    public void textArea_UsageTypeInTextArea() {
        leftNavigatorComp.clickDataTableComp("textarea");
        inputComp.forInput("usage")
                .typeInTextArea("typing in text area")
                .verify().textAreaEquals("typing in text area");
    }

    @Test
    public void pinInput_otpInput_acceptsAndVerifiesDigits() {
        leftNavigatorComp.clickDataTableComp("pin-input");
        inputComp.forInput("otp").typeInputs("12345")
                .verify().pinInput("12345");
    }

    @Test
    public void pinInput_separatorInput_acceptsAndVerifiesDigits() {
        inputComp.forInput("separator").typeInputs("123456")
                .verify().pinInput("123456");
    }
}
