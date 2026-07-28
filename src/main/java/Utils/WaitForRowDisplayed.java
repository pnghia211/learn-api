package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class WaitForRowDisplayed implements ExpectedCondition<Boolean> {
    private final By rowLocator;
    private final int requiredStableChecks;
    private int lastCount = -1;
    private int stableStreak = 0;

    public WaitForRowDisplayed(By rowLocator, int requiredStableChecks) {
        this.rowLocator = rowLocator;
        this.requiredStableChecks = requiredStableChecks;
    }

    @Override
    public Boolean apply(WebDriver driver) {
        int current = driver.findElements(rowLocator).size();

        if (current == lastCount) {
            stableStreak++;
        } else {
            stableStreak = 0;
            lastCount = current;
        }

        return stableStreak >= requiredStableChecks;
    }

    @Override
    public String toString() {
        return "row count for locator [" + rowLocator + "] to remain stable for "
                + requiredStableChecks + " consecutive polls (last seen count: " + lastCount + ")";
    }
}
