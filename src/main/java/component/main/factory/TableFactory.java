package component.main.factory;

import actions.TableActions;
import component.main.BaseComp;
import component.main.table.TableComp;
import data.ComponentIndexOption;
import data.TableLabel;
import org.openqa.selenium.WebDriver;

public class TableFactory extends BaseComp {
    public TableFactory(WebDriver driver) {
        super(driver);
    }

    public TableActions forTable(TableLabel tableLabel) {
        return forTable(tableLabel, ComponentIndexOption.PRIMARY);
    }

    public TableActions forTable(TableLabel tableLabel, ComponentIndexOption option) {
        return new TableActions(new TableComp(driver, tableLabel.label(), option.label()));
    }
}
