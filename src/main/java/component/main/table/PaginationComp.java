package component.main.table;

import component.main.BaseComp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PaginationComp extends BaseComp {
    String tableLabel;
    private WebElement cachedRoot;
    private By paginationBtnSel = By.cssSelector("button[data-slot='item']");
    private By firstPageBtnSel = By.cssSelector("button[data-slot='first']");
    private By prevPageBtnSel = By.cssSelector("button[data-slot='prev']");
    private By nextPageBtnSel = By.cssSelector("button[data-slot='next']");
    private By lastPageBtnSel = By.cssSelector("button[data-slot='last']");
    private By currentPageSel = By.cssSelector("button[data-slot='item'][aria-current='page']");

    public PaginationComp(WebDriver driver, String tableLabel) {
        super(driver);
        this.tableLabel = tableLabel;
    }

    private WebElement paginationNav() {
        cachedRoot = getOrRefreshCached(cachedRoot, () -> getRootComp(tableLabel));
        return cachedRoot;
    }

    public WebElement firstPageBtn() {
        return paginationNav().findElement(firstPageBtnSel);
    }

    public WebElement previousPageBtn() {
        return paginationNav().findElement(prevPageBtnSel);
    }

    public WebElement nextPageBtn() {
        return paginationNav().findElement(nextPageBtnSel);
    }

    public WebElement lastPageBtn() {
        return paginationNav().findElement(lastPageBtnSel);
    }

    public WebElement currentPageBtn() {
        return paginationNav().findElement(currentPageSel);
    }

    public List<WebElement> listPageBtn() {
        return paginationNav().findElements(paginationBtnSel);
    }
}
