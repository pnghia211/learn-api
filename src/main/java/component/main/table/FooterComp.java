package component.main.table;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FooterComp extends TableComp {
    private String cellFooterByColumnIndexXpath = ".//tfoot//tr/th[%s]";
    private By cellFooterSummaryXpath = By.xpath("./following-sibling::*");

    public FooterComp(WebDriver driver, String tableLabel, int tableIndex) {
        super(driver, tableLabel, tableIndex);
    }

    public WebElement getFooterCellByIndex(int headerIndex) {
        return tableByLabel(tableLabel, tableIndex).findElement(By.xpath(String.format(cellFooterByColumnIndexXpath, headerIndex)));
    }

    public WebElement getFooterSummary() {
        return tableByLabel(tableLabel, tableIndex).findElement(cellFooterSummaryXpath);
    }
}
