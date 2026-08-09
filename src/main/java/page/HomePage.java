package page;

import component.main.HeaderComp;
import component.main.LeftNavigatorComp;
import component.main.calendar.CalendarComp;
import component.main.table.TableComp;
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

    public TableComp tableComp(){
        return new TableComp(driver);
    }

    public CalendarComp calenderComp(){
        return new CalendarComp(driver);
    }
}
