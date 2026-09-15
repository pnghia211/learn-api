package actions;

import component.main.table.ToolbarComp;
import data.DropdownOption;
import data.HeaderColumnOption;
import data.SortingOption;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ToolbarActions extends BaseActions<ToolbarComp> {
    private final TableActions tableActions;

    public ToolbarActions(ToolbarComp toolbarComp, TableActions tableActions) {
        super(toolbarComp);
        this.tableActions = tableActions;
    }

    public WebElement getHeaderCheckbox() {
        return getComp().headerCheckbox();
    }

    public WebElement getDropdownBtn() {
        return getComp().headerDropdownButton();
    }

    private ToolbarActions selectDropdownButton() {
        WebElement button = getDropdownBtn();
        if (!"open".equalsIgnoreCase(button.getAttribute("data-state"))) {
            button.click();
        }
        return this;
    }

    private ToolbarActions unselectDropdownButton() {
        WebElement button = getDropdownBtn();
        if ("open".equalsIgnoreCase(button.getAttribute("data-state"))) {
            getComp().actions().sendKeys(Keys.ESCAPE).perform();
        }
        return this;
    }

    public enum DropdownOptionState {
        SELECTED, UNSELECTED
    }

    public ToolbarActions selectDropdownOption(DropdownOption option) {
        setBtnDropdownOption(option, DropdownOptionState.SELECTED);
        return this;
    }

    public ToolbarActions unselectDropdownOption(DropdownOption option) {
        setBtnDropdownOption(option, DropdownOptionState.UNSELECTED);
        return this;
    }

    private ToolbarActions setBtnDropdownOption(DropdownOption option, DropdownOptionState desiredState) {
        selectDropdownButton();

        WebElement optionEle = getComp().btnDropdownOptions(option);
        boolean isChecked = "checked".equalsIgnoreCase(optionEle.getAttribute("data-state"));
        boolean shouldBeChecked = desiredState == DropdownOptionState.SELECTED;

        if (isChecked != shouldBeChecked) {
            optionEle.click();
            new WebDriverWait(getComp().driver(), Duration.ofSeconds(5))
                    .until(d -> shouldBeChecked == "checked".equalsIgnoreCase(
                            getComp().btnDropdownOptions(option).getAttribute("data-state")));
        }

        unselectDropdownButton();
        return this;
    }

    public Map<String, Integer> getHeadersMap() {
        List<WebElement> headers = getComp().headerColumns();
        Map<String, Integer> headersMap = new LinkedHashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            String text = headers.get(i).getText().trim();
            if (!text.isEmpty()) {
                headersMap.put(text, i);
            }
        }
        return headersMap;
    }

    public ToolbarActions setAllSelectionHeaderToDefaultState() {
        WebElement ele = getComp().headerCheckbox();
        new WebDriverWait(getComp().driver(), Duration.ofSeconds(5))
                .until(d -> {
                    ele.click();
                    return "unchecked".equalsIgnoreCase(ele.getAttribute("data-state"));
                });
        return this;
    }

    public ToolbarActions selectSortingHeader(HeaderColumnOption option) {
        WebElement ele = getComp().sortingHeader(option);
        if (!"open".equalsIgnoreCase(ele.getAttribute("data-state"))) {
            ele.click();
        }
        return this;
    }

    public ToolbarActions unselectSortingHeader(HeaderColumnOption option) {
        WebElement ele = getComp().sortingHeader(option);
        if ("open".equalsIgnoreCase(ele.getAttribute("data-state"))) {
            getComp().actions().sendKeys(Keys.ESCAPE).perform();
        }
        return this;
    }

    public ToolbarActions setHeaderDropdownOption(HeaderColumnOption option, SortingOption sortingOption) {
        selectSortingHeader(option);

        WebElement optionEle = getComp().headerDropdownOptions(sortingOption);
        boolean isChecked = "checked".equalsIgnoreCase(optionEle.getAttribute("data-state"));

        if (!isChecked) {
            optionEle.click();
            WaitUtils.waitForInvisibility(getComp().driver(), optionEle);
        } else {
            unselectSortingHeader(option);
        }

        return this;
    }

    public TableActions and() {
        return tableActions;
    }
}
