package test;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.factory.FormFactory;
import component.main.factory.InputFactory;
import component.main.form.FormComp;
import data.DropdownOption;
import driver.DriverFactory;
import model.CardMaskData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Sleeper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import page.HomePage;

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
                .fillInput("dsadsadsadsa").fillInputNumber("10")
                .fillInputDate("01-21-1995");

        Thread.sleep(5000);
        driver.quit();
    }
}
