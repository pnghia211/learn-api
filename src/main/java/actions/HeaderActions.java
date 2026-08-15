package actions;

import component.main.table.HeaderComp;
import data.DropdownOption;
import data.HeaderColumnOption;
import data.SortingOption;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HeaderActions {
    private final HeaderComp headerComp;
    private final TableActions tableActions;

    public HeaderActions(HeaderComp headerComp, TableActions tableActions) {
        this.headerComp = headerComp;
        this.tableActions = tableActions;
    }

    public WebElement getHeaderDropdownButton() {
        return headerComp.headerDropdownButton();
    }

    public WebElement getHeaderCheckbox() {
        return headerComp.headerCheckbox();
    }

    public WebElement getDropdownBtn() {
        return headerComp.headerDropdownButton();
    }

    private HeaderActions selectDropdownButton() {
        WebElement button = getDropdownBtn();
        if (!"open".equalsIgnoreCase(button.getAttribute("data-state"))) {
            button.click();
        }
        return this;
    }

    private HeaderActions unselectDropdownButton() {
        WebElement button = getDropdownBtn();
        if ("open".equalsIgnoreCase(button.getAttribute("data-state"))) {
            headerComp.actions().sendKeys(Keys.ESCAPE).perform();
        }
        return this;
    }

    public enum DropdownOptionState {
        SELECTED, UNSELECTED
    }

    public HeaderActions selectDropdownOption(DropdownOption option) {
        setBtnDropdownOption(option, DropdownOptionState.SELECTED);
        return this;
    }

    public HeaderActions unselectDropdownOption(DropdownOption option) {
        setBtnDropdownOption(option, DropdownOptionState.UNSELECTED);
        return this;
    }

    private HeaderActions setBtnDropdownOption(DropdownOption option, DropdownOptionState desiredState) {
        selectDropdownButton();

        WebElement optionEle = headerComp.btnDropdownOptions(option);
        boolean isChecked = "checked".equalsIgnoreCase(optionEle.getAttribute("data-state"));
        boolean shouldBeChecked = desiredState == DropdownOptionState.SELECTED;

        if (isChecked != shouldBeChecked) {
            optionEle.click();
            new WebDriverWait(headerComp.driver(), Duration.ofSeconds(5))
                    .until(d -> shouldBeChecked == "checked".equalsIgnoreCase(
                            headerComp.btnDropdownOptions(option).getAttribute("data-state")));
        }

        unselectDropdownButton();
        return this;
    }

    public Map<String, Integer> getHeadersMap() {
        List<WebElement> headers = headerComp.headerColumns();
        Map<String, Integer> headersMap = new LinkedHashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            String text = headers.get(i).getText().trim();
            if (!text.isEmpty()) {
                headersMap.put(text, i);
            }
        }
        return headersMap;
    }

    public HeaderActions setAllSelectionHeaderToDefaultState() {
        WebElement ele = headerComp.headerCheckbox();
        new WebDriverWait(headerComp.driver(), Duration.ofSeconds(5))
                .until(d -> {
                    ele.click();
                    return "unchecked".equalsIgnoreCase(ele.getAttribute("data-state"));
                });
        return this;
    }

    public HeaderActions selectSortingHeader(HeaderColumnOption option) {
        WebElement ele = headerComp.sortingHeader(option);
        if (!"open".equalsIgnoreCase(ele.getAttribute("data-state"))) {
            ele.click();
        }
        return this;
    }

    public HeaderActions unselectSortingHeader(HeaderColumnOption option) {
        WebElement ele = headerComp.sortingHeader(option);
        if ("open".equalsIgnoreCase(ele.getAttribute("data-state"))) {
            headerComp.actions().sendKeys(Keys.ESCAPE).perform();
        }
        return this;
    }

    public HeaderActions setHeaderDropdownOption(HeaderColumnOption option, SortingOption sortingOption) {
        selectSortingHeader(option);

        WebElement optionEle = headerComp.headerDropdownOptions(sortingOption);
        boolean isChecked = "checked".equalsIgnoreCase(optionEle.getAttribute("data-state"));

        if (!isChecked) {
            optionEle.click();
            new WebDriverWait(headerComp.driver(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOf(optionEle));
        } else {
            unselectSortingHeader(option);
        }

        return this;
    }

    public TableActions and() {
        return tableActions;
    }
}
