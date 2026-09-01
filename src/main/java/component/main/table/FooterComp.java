package component.main.table;

import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FooterComp extends BaseComp {
    TableComp tableComp;
    private String cellFooterByColumnIndexXpath = ".//tfoot//tr/th[%s]";
    private By cellFooterSummaryXpath = By.cssSelector("[data-slot='root'] + div");

    public FooterComp(TableComp tableComp) {
        super(tableComp.driver());
        this.tableComp = tableComp;
    }

    public WebElement getFooterCellByIndex(int headerIndex) {
        return tableComp.tableByLabel().findElement(By.xpath(String.format(cellFooterByColumnIndexXpath, headerIndex)));
    }

    public WebElement getFooterSummary() {
        return tableComp.tableByLabel().findElement(cellFooterSummaryXpath);
    }
}
