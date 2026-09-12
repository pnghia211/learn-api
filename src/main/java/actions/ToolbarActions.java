package actions;

import component.main.table.ToolbarComp;
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

public class ToolbarActions {
    private final ToolbarComp toolbarComp;
    private final TableActions tableActions;

    public ToolbarActions(ToolbarComp toolbarComp, TableActions tableActions) {
        this.toolbarComp = toolbarComp;
        this.tableActions = tableActions;
    }

    public WebElement getHeaderCheckbox() {
        return toolbarComp.headerCheckbox();
    }

    public WebElement getDropdownBtn() {
        return toolbarComp.headerDropdownButton();
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
            toolbarComp.actions().sendKeys(Keys.ESCAPE).perform();
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

        WebElement optionEle = toolbarComp.btnDropdownOptions(option);
        boolean isChecked = "checked".equalsIgnoreCase(optionEle.getAttribute("data-state"));
        boolean shouldBeChecked = desiredState == DropdownOptionState.SELECTED;

        if (isChecked != shouldBeChecked) {
            optionEle.click();
            new WebDriverWait(toolbarComp.driver(), Duration.ofSeconds(5))
                    .until(d -> shouldBeChecked == "checked".equalsIgnoreCase(
                            toolbarComp.btnDropdownOptions(option).getAttribute("data-state")));
        }

        unselectDropdownButton();
        return this;
    }

    public Map<String, Integer> getHeadersMap() {
        List<WebElement> headers = toolbarComp.headerColumns();
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
        WebElement ele = toolbarComp.headerCheckbox();
        new WebDriverWait(toolbarComp.driver(), Duration.ofSeconds(5))
                .until(d -> {
                    ele.click();
                    return "unchecked".equalsIgnoreCase(ele.getAttribute("data-state"));
                });
        return this;
    }

    public ToolbarActions selectSortingHeader(HeaderColumnOption option) {
        WebElement ele = toolbarComp.sortingHeader(option);
        if (!"open".equalsIgnoreCase(ele.getAttribute("data-state"))) {
            ele.click();
        }
        return this;
    }

    public ToolbarActions unselectSortingHeader(HeaderColumnOption option) {
        WebElement ele = toolbarComp.sortingHeader(option);
        if ("open".equalsIgnoreCase(ele.getAttribute("data-state"))) {
            toolbarComp.actions().sendKeys(Keys.ESCAPE).perform();
        }
        return this;
    }

    public ToolbarActions setHeaderDropdownOption(HeaderColumnOption option, SortingOption sortingOption) {
        selectSortingHeader(option);

        WebElement optionEle = toolbarComp.headerDropdownOptions(sortingOption);
        boolean isChecked = "checked".equalsIgnoreCase(optionEle.getAttribute("data-state"));

        if (!isChecked) {
            optionEle.click();
            new WebDriverWait(toolbarComp.driver(), Duration.ofSeconds(5))
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
