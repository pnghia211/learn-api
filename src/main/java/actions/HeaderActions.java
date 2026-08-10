package actions;

import component.main.table.HeaderComp;
import data.HeaderColumnOption;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HeaderActions {
    private final HeaderComp comp;
    private final TableActions tableActions;
    private final String tableLabel;

    public HeaderActions(HeaderComp comp, String tableLabel, TableActions tableActions) {
        this.comp = comp;
        this.tableActions = tableActions;
        this.tableLabel = tableLabel;
    }

    public WebElement getHeaderDropdownButton() {
        return comp.headerDropdownButton(tableLabel);
    }

    public WebElement getHeaderCheckbox() {
        return comp.headerCheckbox(tableLabel);
    }

    public WebElement getDropdownBtn() {
        return comp.headerDropdownButton(tableLabel);
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
            comp.actions().sendKeys(Keys.ESCAPE).perform();
        }
        return this;
    }

    public enum DropdownOptionState {
        SELECTED, UNSELECTED
    }

    public HeaderActions selectDropdownOption(HeaderColumnOption option) {
        setDropdownOption(option, DropdownOptionState.SELECTED);
        return this;
    }

    public HeaderActions unselectDropdownOption(HeaderColumnOption option) {
        setDropdownOption(option, DropdownOptionState.UNSELECTED);
        return this;
    }

    private HeaderActions setDropdownOption(HeaderColumnOption option, DropdownOptionState desiredState) {
        selectDropdownButton();

        WebElement optionEle = comp.headerDropdownOptions(tableLabel, option.dropdownLabel());
        boolean isChecked = "checked".equalsIgnoreCase(optionEle.getAttribute("data-state"));
        boolean shouldBeChecked = desiredState == DropdownOptionState.SELECTED;

        if (isChecked != shouldBeChecked) {
            optionEle.click();
            new WebDriverWait(comp.driver(), Duration.ofSeconds(5))
                    .until(d -> shouldBeChecked == "checked".equalsIgnoreCase(
                            comp.headerDropdownOptions(tableLabel, option.dropdownLabel())
                                    .getAttribute("data-state")));
        }

        unselectDropdownButton();
        return this;
    }

    public Map<String, Integer> getHeadersMap() {
        List<WebElement> headers = comp.headerColumns(tableLabel);
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
        WebElement ele = comp.headerCheckbox(tableLabel);
        new WebDriverWait(comp.driver(), Duration.ofSeconds(5))
                .until(d -> {
                    ele.click();
                    return "unchecked".equalsIgnoreCase(ele.getAttribute("data-state"));
                });
        return this;
    }

    public TableActions and() {
        return tableActions;
    }
}
