package component.main.table;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class RowDropdownComp{
    TableComp tableComp;
    private By actionButtonSel = By.cssSelector("td button");
    private By actionDropdownMenuSel = By.cssSelector("[id^='reka-dropdown-menu'][dir='ltr']");
    private String menuItemCssXpath = "button[data-slot='item'][role='menuitem']";
    private By copyNotificationPopupSel = By.cssSelector("[aria-label^='Notifications'] [data-slot=base]");

    public RowDropdownComp(TableComp tableComp) {
        this.tableComp = tableComp;
    }

    public WebElement actionBtnByCellText(String cell) {
        return tableComp.rowsByCellText(cell).get(0).findElement(actionButtonSel);
    }

    public WebElement rowDropdownMenu() {
        return tableComp.driver().findElement(actionDropdownMenuSel);
    }

    public WebElement menuItemByLabel(String label) {
        return rowDropdownMenu().findElements(By.cssSelector(menuItemCssXpath)).stream()
                .filter(btn -> btn.getText().trim().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Menu item not found: " + label));
    }

    public WebElement copyNotificationPopup() {
        return tableComp.driver().findElement(copyNotificationPopupSel);
    }
}
