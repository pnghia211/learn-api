package data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarTestData {
    public static final String AVAILABLE_DATE = LocalDate.now().plusDays(5)
            .format(DateTimeFormatter.ISO_DATE);

    public static final String DISABLED_DATE = "2022-01-10";

    public static final String DEFAULT_DATE = LocalDate.of(2022, 2, 3)
            .format(DateTimeFormatter.ISO_DATE);
}
