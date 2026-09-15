package actions;

import component.main.table.PaginationComp;
import model.TableRecord;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PaginationActions extends BaseActions<PaginationComp> {
    public PaginationActions(PaginationComp paginationComp) {
        super(paginationComp);
    }

    public WebElement getFirstPageBtn() {
        return getComp().firstPageBtn();
    }

    public WebElement getPreviousPageBtn() {
        return getComp().previousPageBtn();
    }

    public WebElement getNextPageBtn() {
        return getComp().nextPageBtn();
    }

    public WebElement getLastPageBtn() {
        return getComp().lastPageBtn();
    }

    public WebElement getCurrentPageBtn() {
        return getComp().currentPageBtn();
    }

    public List<WebElement> getListPageBtn() {
        return getComp().listPageBtn();
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
