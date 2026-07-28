package helpers;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DateHelper {
    public static String convertDate(String dateValue){
        LocalDate date = LocalDate.parse(dateValue);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
        return date.format(formatter);
    }

    public static String convertMonth(String monthValue) {
        YearMonth yearMonth = YearMonth.parse(monthValue);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
        return yearMonth.format(formatter);
    }

    public static String convertFullDate(String heading) {
        LocalDate date = LocalDate.parse(heading, DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
        return date.toString(); // yyyy-MM-dd
    }

    public static List<String> buildDateRange(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        LocalDate rangeStart = start.isBefore(end) ? start : end;
        LocalDate rangeEnd = start.isBefore(end) ? end : start;

        return rangeStart.datesUntil(rangeEnd.plusDays(1))
                .map(LocalDate::toString)
                .toList();
    }

    public static String convertHeadingFormat(String heading) {
        // Pattern: MMM d, YYYY (full date, e.g. "Jan 10, 2022")
        if (heading.matches("[A-Z][a-z]{2} \\d{1,2}, \\d{4}")) {
            return DateHelper.convertFullDate(heading);
        }

        // Pattern: YYYY-MM (year-month)
        if (heading.matches("\\d{4}-\\d{2}")) {
            return DateHelper.convertMonth(heading);
        }

        // Pattern: YYYY-YYYY (year range / decade)
        if (heading.matches("\\d{4}-\\d{4}")) {
            return heading;
        }

        // Pattern: YYYY (year only)
        if (heading.matches("\\d{4}")) {
            return heading;
        }

        throw new IllegalArgumentException("Invalid heading format: " + heading);
    }
}
