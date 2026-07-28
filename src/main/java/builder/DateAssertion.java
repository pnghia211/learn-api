package builder;

import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateAssertion {
    private WebElement dateCell;
    private String expectedLabel;

    public DateAssertion(WebElement dateCell, String expectedLabel) {
        this.dateCell = dateCell;
        this.expectedLabel = expectedLabel;
    }

    public DateAssertion(WebElement dateCell) {
        this.dateCell = dateCell;
    }

    public DateAssertion assertLabelCorrect() {
        assertEquals(expectedLabel, dateCell.getAttribute("aria-label"));
        return this;
    }

    public DateAssertion assertSelected() {
        assertEquals("true", dateCell.getAttribute("data-selected"));
        return this;
    }

    public DateAssertion assertNotSelected() {
        assertNull(dateCell.getAttribute("data-selected"));
        return this;
    }
}
