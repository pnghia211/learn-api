package component.main.table;

import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FooterComp extends BaseComp {
    private final String tableLabel;
    private String cellFooterByColumnIndexXpath = ".//tfoot//tr/th[%s]";
    private By cellFooterSummaryXpath = By.xpath("./following-sibling::*");

    public FooterComp(WebDriver driver, String tableLabel) {
        super(driver);
        this.tableLabel = tableLabel;
    }

    public WebElement getFooterCellByIndex(int headerIndex) {
        return tableByLabel(tableLabel).findElement(By.xpath(String.format(cellFooterByColumnIndexXpath, headerIndex)));
    }

    public WebElement getFooterSummary() {
        return tableByLabel(tableLabel).findElement(cellFooterSummaryXpath);
    }
}
