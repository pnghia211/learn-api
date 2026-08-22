package page;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.calendar.CalendarComp;
import component.main.form.InputComp;
import component.main.table.TableFactory;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HeaderComp componentsSection(){
        return new HeaderComp(driver);
    }

    public LeftNavigatorComp leftNavigatorComp(){
        return new LeftNavigatorComp(driver);
    }

    public TableFactory tableComp(){
        return new TableFactory(driver);
    }

    public InputComp inputComp(){
        return new InputComp(driver);
    }

    public CalendarComp calenderComp(){
        return new CalendarComp(driver);
    }
}
