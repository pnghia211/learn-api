package test;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.form.InputComp;
import component.main.table.TableFactory;
import data.*;
import driver.DriverFactory;
import model.TableRecord;
import org.openqa.selenium.WebDriver;
import page.HomePage;

import java.util.List;

import static helpers.TestDataLoader.loadExpectedTableData;
import static url.Url.mainPage;

public class TestInput {
    private final static WebDriver driver = DriverFactory.getChromeDriver();

    public static void main(String[] args) throws InterruptedException {
        driver.get(mainPage);

        HomePage homePage = new HomePage(driver);
        homePage.componentsSection().clickComponentsComp();
        homePage.leftNavigatorComp().clickDataTableComp("input");
        InputComp inputComp = homePage.inputComp();

        inputComp.forInput("type").uploadFile("C:\\Users\\ADMIN\\Desktop\\dummy-png-image.png");


        Thread.sleep(5000);
        driver.quit();
    }
}
