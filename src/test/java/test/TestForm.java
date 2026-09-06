package test;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.factory.FormFactory;
import data.DropdownOption;
import driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import page.HomePage;

import java.util.List;

import static url.Url.mainPage;

public class TestForm {


    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = DriverFactory.getChromeDriver();
        driver.get(mainPage);

        LeftNavigatorComp leftNavigatorComp;

        HomePage homePage = new HomePage(driver);
        HeaderComp headerComp = homePage.componentsSection();
        leftNavigatorComp = homePage.leftNavigatorComp();

        headerComp.clickComponentsComp();
        leftNavigatorComp.clickDataTableComp("form");

        FormFactory formFactory = homePage.formComp();

        formFactory.forForm("input-events")
                .fillTextInput("automation test")
                .verify().textInputEquals("automation test")

                .and().decreaseInputNumberTo("-10")
                .verify().numberInputEquals("-10")
                .and().increaseInputNumberTo("10")
                .verify().numberInputEquals("10")

                .and().fillPinInput("12345")
                .verify().pinInput("12345")

                .and().fillInputDate("12-02-2022")
                .verify().dateInput("12-02-2022")

                .and().fillInputTime("09:30", "PM")
                .verify().timeInput("09:30", "PM")

                .and().inputTags("vue", "test", "auto")
                .verify().tagsInput(List.of("vue", "test", "auto"))

                .and().inputMenu(DropdownOption.OPTION_2)
                .verify().inputMenu(DropdownOption.OPTION_2)
                .and().inputMenus(List.of(DropdownOption.OPTION_1, DropdownOption.OPTION_2))
                .verify().inputMenus(List.of(DropdownOption.OPTION_1, DropdownOption.OPTION_2))

                .and().fillTextArea("we gonna be alright")
                .verify().textArea("we gonna be alright")

                .and().selectDropdownOption(DropdownOption.OPTION_2)
                .verify().selectValue(DropdownOption.OPTION_2)
                .and().selectDropdownOptions(List.of(DropdownOption.OPTION_2, DropdownOption.OPTION_3))
                .verify().selectMultipleValue(List.of(DropdownOption.OPTION_2, DropdownOption.OPTION_3))

                .and().selectMenuDropdownOption(DropdownOption.OPTION_2)
                .verify().selectMenuValue(DropdownOption.OPTION_2)
                .and().selectMenuDropdownOptions(List.of(DropdownOption.OPTION_1, DropdownOption.OPTION_2, DropdownOption.OPTION_3))
                .verify().selectMenusValue(List.of(DropdownOption.OPTION_1, DropdownOption.OPTION_2, DropdownOption.OPTION_3))

        ;


        Thread.sleep(5000);
        driver.quit();
    }
}
