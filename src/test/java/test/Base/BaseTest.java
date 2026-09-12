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
    protected WebDriver driver;
    protected HomePage homePage;
    protected HeaderComp headerComp;
    protected LeftNavigatorComp leftNavigatorComp;

    @BeforeClass
    public void baseSetUp() {
        driver = DriverFactory.getChromeDriver();
        driver.get(mainPage);

        homePage = new HomePage(driver);
        headerComp = homePage.componentsSection();
        leftNavigatorComp = homePage.leftNavigatorComp();

        headerComp.clickComponentsComp();
    }

    @AfterClass
    public void baseTearDown() {
        DriverFactory.quitDriver();
    }
}
