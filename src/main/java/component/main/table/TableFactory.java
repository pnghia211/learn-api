package component.main.table;

import actions.TableActions;
import component.main.BaseComp;
import data.TableIndexOption;
import data.TableLabel;
import org.openqa.selenium.WebDriver;

public class TableFactory extends BaseComp {
    public TableFactory(WebDriver driver) {
        super(driver);
    }

    public TableActions forTable(TableLabel tableLabel) {
        return forTable(tableLabel, TableIndexOption.PRIMARY);
    }

    public TableActions forTable(TableLabel tableLabel, TableIndexOption option) {
        return new TableActions(driver, tableLabel.label(), option.label());
    }
}
