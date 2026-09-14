package test.Base;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import page.HomePage;

import static url.Url.mainPage;

public class BaseTest {

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    protected HomePage homePage() {
        return new HomePage(driver());
    }

    protected HeaderComp headerComp() {
        return homePage().componentsSection();
    }

    protected LeftNavigatorComp leftNavigatorComp() {
        return homePage().leftNavigatorComp();
    }

    @BeforeClass
    public void baseSetUp() {
        WebDriver driver = DriverFactory.getChromeDriver();
        driver.get(mainPage);

        headerComp().clickComponentsComp();
    }

    @AfterClass
    public void baseTearDown() {
        DriverFactory.quitDriver();
    }
}
