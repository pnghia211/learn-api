package test;

import component.main.factory.InputFactory;
import component.main.form.InputComp.RangeBound;
import data.DropdownOption;
import helpers.TestDataLoader;
import model.CardMaskData;
import model.FormTestData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.Base.BaseTest;

import java.util.List;

public class TestInput extends BaseTest {
    private InputFactory inputFactory() {
        return homePage().inputComp();
    };

    @BeforeClass
    public void setUp() {
        leftNavigatorComp().clickDataTableComp("input");
    }

    @Test
    public void input_uploadFile_showsFileUploaded() {
        FormTestData data = TestDataLoader.load("testdata/form-data.json", FormTestData.class);
        String filePath = TestDataLoader.resolveTestFile(data.uploadFilePath());

        leftNavigatorComp().clickDataTableComp("input");
        inputFactory().forInput("type")
                .uploadFile(filePath)
                .verify().hasFileUploaded();
    }

    @Test
    public void input_clearButton_clearsTypedText() {
        leftNavigatorComp().clickDataTableComp("input");
        inputFactory().forInput("with-clear-button")
                .verify().inputValueEquals("Click to clear").and()
                .type("text clear button")
                .verify().inputValueEquals("text clear button")
                .and().clickClearBtn()
                .verify().inputIsEmpty();
    }

    @Test
    public void input_passwordToggle_showsAndHidesPassword() {
        leftNavigatorComp().clickDataTableComp("input");
        inputFactory().forInput("with-password-toggle")
                .verify().inputIsEmpty()
                .and().type("password test")
                .verify().inputValueEquals("password test").inputIsHidden()
                .and().clickShowPasswordBtn()
                .verify().inputIsVisible();
    }

    @Test
    public void input_passwordStrengthIndicator_progressesThroughAllLevels() {
        leftNavigatorComp().clickDataTableComp("input");
        inputFactory().forInput("with-password-strength-indicator")
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
        leftNavigatorComp().clickDataTableComp("input");
        CardMaskData validCard = new CardMaskData("4242 4242 4242 4242", "12/25", "123");
        inputFactory().forInput("with-mask")
                .fillMaskInputFields(validCard)
                .verify().maskInputFieldsEqual(validCard);
    }

    @Test
    public void inputDate_singleDateInput_acceptsAndDisplaysDate() {
        leftNavigatorComp().clickDataTableComp("input-date");
        inputFactory().forInput("usage").fillDate("01-21-1995")
                .verify().dateSingleInput("01-21-1995");
    }

    @Test
    public void inputDate_dateRangeInput_acceptsAndDisplaysRange() {
        leftNavigatorComp().clickDataTableComp("input-date");
        inputFactory().forInput("range").fillDateRangeInput("01-21-1995", "12-12-2026")
                .verify().dateRangeInput("01-21-1995", "12-12-2026");
    }

    @Test
    public void inputTime_timeInput_fillSingleInputTime() {
        leftNavigatorComp().clickDataTableComp("input-time");
        inputFactory().forInput("usage")
                .fillTime("04:20", "PM")
                .verify().timeValue("04:20", "PM");
    }

    @Test
    public void inputTime_fillRangeInputTime() {
        leftNavigatorComp().clickDataTableComp("input-time");
        inputFactory().forInput("range")
                .fillTimeRangeInput(RangeBound.START, "08:30", "AM")
                .fillTimeRangeInput(RangeBound.END, "04:20", "PM")
                .verify()
                .timeRangeInput(RangeBound.START, "08:30", "AM")
                .timeRangeInput(RangeBound.END, "04:20", "PM");
    }

    @Test
    public void inputTags_addAndRemoveTags() {
        leftNavigatorComp().clickDataTableComp("input-tags");
        inputFactory().forInput("usage")
                .inputTags(List.of("abc", "test"))
                .verify().tagItems(List.of("Vue", "abc", "test"));
    }

    @Test
    public void inputMenu_usage_showsOptionsInOrder() {
        leftNavigatorComp().clickDataTableComp("input-menu");
        List<DropdownOption> opts = List.of(DropdownOption.BACKLOG, DropdownOption.TO_DO,
                DropdownOption.IN_PROGRESS, DropdownOption.DONE);
        inputFactory().forInput("usage").verify().selectedOptionsInOrder(opts);
    }

    @Test
    public void inputMenu_multiple_selectAndRemoveOptions() {
        leftNavigatorComp().clickDataTableComp("input-menu");
        List<DropdownOption> opts = List.of(DropdownOption.BACKLOG, DropdownOption.TO_DO,
                DropdownOption.IN_PROGRESS, DropdownOption.DONE);
        inputFactory().forInput("multiple").selectMultiDropdownOpt(opts)
                .removeOption(DropdownOption.BACKLOG, DropdownOption.TO_DO, DropdownOption.DONE)
                .verify().selectedTagsItem(DropdownOption.IN_PROGRESS);
    }

    @Test
    public void inputMenu_countryPicker_selectsVietnam() {
        leftNavigatorComp().clickDataTableComp("input-menu");
        inputFactory().forInput("as-a-country-picker")
                .verify().countryPickerDefault()
                .and().selectDropdownOpt(DropdownOption.COUNTRY_VIETNAM)
                .verify().selectedCountryInput("VN", "Vietnam");
    }

    @Test
    public void inputNumber_increasesAndDecreasesValue() {
        leftNavigatorComp().clickDataTableComp("input-number");
        inputFactory().forInput("usage")
                .increaseInputTo("10")
                .verify().inputValueEquals("10")
                .and().decreaseInputTo("-10")
                .verify().inputValueEquals("-10");
    }

    @Test
    public void inputNumber_increasesAndDecreasesValueOrientation() {
        leftNavigatorComp().clickDataTableComp("input-number");
        inputFactory().forInput("orientation")
                .increaseInputTo("50")
                .verify().inputValueEquals("50")
                .and().decreaseInputTo("-50")
                .verify().inputValueEquals("-50");
    }

    @Test
    public void textArea_UsageTypeInTextArea() {
        leftNavigatorComp().clickDataTableComp("textarea");
        inputFactory().forInput("usage")
                .typeInTextArea("typing in text area")
                .verify().textAreaEquals("typing in text area");
    }

    @Test
    public void pinInput_otpInput_acceptsAndVerifiesDigits() {
        leftNavigatorComp().clickDataTableComp("pin-input");
        inputFactory().forInput("otp").typeInputs("12345")
                .verify().pinInput("12345");
    }

    @Test
    public void pinInput_separatorInput_acceptsAndVerifiesDigits() {
        leftNavigatorComp().clickDataTableComp("pin-input");
        inputFactory().forInput("separator").typeInputs("123456")
                .verify().pinInput("123456");
    }
}
