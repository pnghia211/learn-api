package component.main.table;

import actions.TableActions;
import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TableComp extends BaseComp {
//    private By rowSel = By.cssSelector("tbody tr");
//    private By rowSelectionSel = By.cssSelector("tr td [aria-label='Select row']");
//    private By checkedRowsSel = By.cssSelector("tr td [aria-label='Select row'][data-state='checked']");
//    private String cellsByColumnIndexXpath = ".//tbody/tr/td[%s]";

    public TableComp(WebDriver driver) {
        super(driver);
    }

    public TableActions forTable(String tableLabel) {
        return forTable(tableLabel, 1);
    }

    public TableActions forTable(String tableLabel, int tableIndex) {
        return new TableActions(driver, tableLabel, tableIndex);
    }
}
