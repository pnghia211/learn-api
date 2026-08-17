package actions;

import component.main.table.PaginationComp;
import model.TableRecord;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PaginationActions {
    PaginationComp paginationComp;
    TableActions tableActions;

    public PaginationActions(PaginationComp paginationComp, TableActions tableActions) {
        this.paginationComp = paginationComp;
        this.tableActions = tableActions;
    }

    public WebElement getFirstPageBtn() {
        return paginationComp.firstPageBtn();
    }

    public WebElement getPreviousPageBtn() {
        return paginationComp.previousPageBtn();
    }

    public WebElement getNextPageBtn() {
        return paginationComp.nextPageBtn();
    }

    public WebElement getLastPageBtn() {
        return paginationComp.lastPageBtn();
    }

    public WebElement getCurrentPageBtn() {
        return paginationComp.currentPageBtn();
    }

    public List<WebElement> getListPageBtn() {
        return paginationComp.listPageBtn();
    }

    public void backToFirstPage() {
        WebElement firstPageBtn = getFirstPageBtn();
        if (firstPageBtn.isEnabled()) {
            firstPageBtn.click();
        }
    }

    public List<TableRecord> sliceForPage(List<TableRecord> allData, int pageIndex, int numberOfPages) {
        int baseSize = allData.size() / numberOfPages;
        int remainder = allData.size() % numberOfPages;
        int start = pageIndex * baseSize + Math.min(pageIndex, remainder);
        int end = start + baseSize + (pageIndex < remainder ? 1 : 0);
        return allData.subList(start, end);
    }
}
