package test;

import component.main.factory.FormFactory;
import helpers.TestDataLoader;
import model.FormTestData;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.Base.BaseTest;

public class TestForm extends BaseTest {

    private FormFactory formFactory;

    @BeforeClass
    public void setUp() {
        leftNavigatorComp.clickDataTableComp("form");
        formFactory = homePage.formComp();
    }

    @Test
    public void formInputEvents_fillsAndVerifiesAllFields() {
        FormTestData data = TestDataLoader.load("testdata/form-data.json", FormTestData.class);
        String filePath = TestDataLoader.resolveTestFile(data.uploadFilePath());

        formFactory.forForm("input-events")
                .fillTextInput(data.textInput())
                .verify().textInputEquals(data.textInput())

                .and().decreaseInputNumberTo(data.numberMin())
                .verify().numberInputEquals(data.numberMin())
                .and().increaseInputNumberTo(data.numberMax())
                .verify().numberInputEquals(data.numberMax())

                .and().fillPinInput(data.pin())
                .verify().pinInput(data.pin())

                .and().fillInputDate(data.date())
                .verify().dateInput(data.date())

                .and().fillInputTime(data.time(), data.period())
                .verify().timeInput(data.time(), data.period())

                .and().inputTags(data.tags())
                .verify().tagsInput(data.tags())

                .and().inputMenu(data.menuOption())
                .verify().inputMenu(data.menuOption())
                .and().inputMenus(data.menuOptions())
                .verify().inputMenus(data.menuOptions())

                .and().fillTextArea(data.textArea())
                .verify().textArea(data.textArea())

                .and().selectDropdownOption(data.selectOption())
                .verify().selectValue(data.selectOption())
                .and().selectDropdownOptions(data.selectOptions())
                .verify().selectMultipleValue(data.selectOptions())

                .and().selectMenuDropdownOption(data.selectMenuOption())
                .verify().selectMenuValue(data.selectMenuOption())
                .and().selectMenuDropdownOptions(data.selectMenuOptions())
                .verify().selectMenusValue(data.selectMenuOptions())

                .and().selectListBoxOption(data.listBoxOption())
                .verify().listBoxSelected(data.listBoxOption())
                .and().selectListBoxOptions(data.listBoxOptions())
                .verify().listBoxesSelected(data.listBoxOptions())

                .and().uploadFile(filePath)
                .verify().fileUploaded()

                .and().selectCheckbox().verify().checkboxSelected()
                .and().selectRating(data.ratingValue()).verify().ratingSelected(data.ratingValue())

                .and().clickSwitchToggle().verify().switchToggleSelected()

                .and().selectCheckboxGroup(data.checkboxGroupOption())
                .verify().checkboxGroupBtnSelected(data.checkboxGroupOption())
                .and().selectRadioGroup(data.radioGroupOption())
                .verify().radioBtnSelected(data.radioGroupOption())

                .and().setSliderTo(data.sliderValue()).verify().sliderValue(data.sliderValue())

                .and().clickSubmitBtn().verify().successfulToast();
    }
}
