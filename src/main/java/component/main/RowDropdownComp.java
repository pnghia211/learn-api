package component.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class RowDropdownComp extends TableComp {
    String tableSel;
    private By rowActionButton = By.cssSelector("td button");
    private By rowActionDropdownMenu = By.cssSelector("[id^='reka-dropdown-menu'][dir='ltr']");
    private String menuItemCss = "button[data-slot='item'][role='menuitem']";
    private By copyNotificationPopup = By.cssSelector("[aria-label^='Notifications'] [data-slot=base]");

    public RowDropdownComp(WebDriver driver, String tableSel) {
        super(driver);
        this.tableSel = tableSel;
    }

    public WebElement actionBtnByCellText(String tableLabel, String cell){
        return rowByCellText(tableLabel, cell).get(0).findElement(rowActionButton);
    }

    public WebElement rowDropdownMenu() {
        return driver.findElement(rowActionDropdownMenu);
    }

    public WebElement menuItemByLabel(String label) {
        return rowDropdownMenu().findElements(By.cssSelector(menuItemCss)).stream()
                .filter(btn -> btn.getText().trim().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Menu item not found: " + label));
    }

    public WebElement copyNotificationPopup() {
        return driver.findElement(copyNotificationPopup);
    }
}
