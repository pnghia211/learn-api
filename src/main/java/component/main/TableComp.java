package component.main;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TableComp extends BaseComp {
    private String cellValueStr = ".//td[text()='%s']/..";
    private String tableSel = ".//*[contains(@class,'overflow-auto')]";
    private By rowSel = By.cssSelector("tbody tr");

    public TableComp(WebDriver driver) {
        super(driver);
    }

    public WebElement getTableBasedOnHeader(String header) {
        WebElement tableEle = getComponentBasedOnHeader(header, tableSel);
        actions.moveToElement(tableEle).perform();
        return tableEle;
    }

    public boolean scrollTillCelDisplayed(String tableHeader, String cell) {
        WebElement tableEle = getComponentBasedOnHeader(tableHeader, tableSel);
        int attempts = 0;
        int maxAttempts = 20;

        while (attempts < maxAttempts) {
            List<WebElement> matches = tableEle.findElements(By.xpath(String.format(cellValueStr, cell)));
            if (!matches.isEmpty() && matches.get(0).isDisplayed()) {
                actions.scrollToElement(matches.get(0)).perform();
                System.out.println("Element is display!!!");
                return true;
            }

            List<WebElement> rows = tableEle.findElements(rowSel);
            if (rows.isEmpty()) return false;

            WebElement lastRow = rows.get(rows.size() - 1);
            int beforeCount = rows.size();

            actions.scrollToElement(lastRow).perform();

            try {
                wait.until(d -> tableEle.findElements(rowSel).size() > rows.size());
            } catch (TimeoutException e) {
                System.out.println("Cannot find element in time frame");
                return false;
            }

            int afterCount = tableEle.findElements(rowSel).size();
            if (afterCount == beforeCount) {
                return false;
            }

            attempts++;
        }
        return false;
    }
}
