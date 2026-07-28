package actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface BaseActions {
      WebDriver getDriver() ;
      WebDriverWait getWait();
      Actions getActions();
}
