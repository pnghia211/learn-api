package helpers;

import actions.TableActions.HeaderColumnOption;
import model.TableRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * Normalizes and verifies TableRecord (raw JSON / expected) data against
 * Map<String, String> (scraped UI / actual) data.
 * <p>
 * Design:
 * - "Expected" is always built as a complete map, covering every known ColumnOption.
 * - "Actual" reflects whatever the UI currently renders — it may contain
 * fewer keys if some columns are hidden.
 * - verify() drives comparison off actual's keys, not expected's, so it
 * automatically adapts to however many columns are visible, for any
 * table using this same ColumnOption set. No per-table or
 * per-column-count special-casing is required.
 */
public class TableRecordNormalizer {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d, HH:mm", Locale.ENGLISH);

    private static final Map<HeaderColumnOption, Function<TableRecord, String>> FIELD_EXTRACTORS = Map.of(
            HeaderColumnOption.ID, r -> "#" + r.id(),
            HeaderColumnOption.DATE, r -> convertIsoToDisplayFormat(r.date()),
            HeaderColumnOption.STATUS, r -> capitalize(r.status()),
            HeaderColumnOption.EMAIL, TableRecord::email,
            HeaderColumnOption.AMOUNT, r ->  formatAsEuro(r.amount())
    );

    /**
     * Builds a fully-normalized expected map covering every ColumnOption,
     * with each value formatted exactly as the UI would render it.
     */
    public static Map<String, String> toDisplayMap(TableRecord record) {
        Map<String, String> map = new LinkedHashMap<>();
        for (HeaderColumnOption column : HeaderColumnOption.values()) {
            Function<TableRecord, String> extractor = FIELD_EXTRACTORS.get(column);
            if (extractor != null) {
                map.put(column.headerLabel(), extractor.apply(record));
            }
        }
        return map;
    }

    /**
     * Verifies actual (whatever columns the UI currently shows) against
     * expectedRecord (always-complete, loaded from JSON). Only keys present
     * in actual are checked — this is what makes it dynamic across tables
     * with different numbers of visible columns.
     * <p>
     * Throws an assertion error naming the specific column that mismatched,
     * or a clear message if actual contains a column with no known mapping.
     */
    public static void verify(TableRecord expectedRecord, Map<String, String> actual) {
        Map<String, String> expected = toDisplayMap(expectedRecord);

        for (Map.Entry<String, String> entry : actual.entrySet()) {
            String column = entry.getKey();
            String actualValue = entry.getValue();

            if (!expected.containsKey(column)) {
                throw new IllegalStateException(
                        "Actual data contains column '" + column + "' with no known expected mapping. " +
                                "Add a ColumnOption + FIELD_EXTRACTORS entry for it.");
            }

            String expectedValue = expected.get(column);
            assertEquals("Mismatch for column: " + column, expectedValue, actualValue);
        }
    }

    public static void verify(List<TableRecord> expectedRows, List<Map<String, String>> actualRows) {
        if (expectedRows.size() != actualRows.size()) {
            throw new IllegalStateException(
                    "Row count mismatch — expected " + expectedRows.size() + " rows but actual had " + actualRows.size());
        }

        for (int i = 0; i < expectedRows.size(); i++) {
            try {
                verify(expectedRows.get(i), actualRows.get(i));
            } catch (AssertionError e) {
                throw new AssertionError("Row " + i + " failed: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Same as verify(), but returns true/false instead of throwing, for callers
     * that want to collect/report multiple row failures rather than stopping
     * at the first mismatch.
     */
    public static boolean matches(TableRecord expectedRecord, Map<String, String> actual) {
        Map<String, String> expected = toDisplayMap(expectedRecord);

        for (Map.Entry<String, String> entry : actual.entrySet()) {
            String column = entry.getKey();
            String actualValue = entry.getValue();
            String expectedValue = expected.get(column);

            if (expectedValue == null || !expectedValue.equals(actualValue)) {
                return false;
            }
        }
        return true;
    }

    private static String convertIsoToDisplayFormat(String isoDate) {
        LocalDateTime dateTime = LocalDateTime.parse(isoDate, ISO_FORMATTER);
        return dateTime.format(DISPLAY_FORMATTER);
    }

    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase(Locale.ENGLISH) + text.substring(1);
    }

    private static String formatAsEuro(String rawAmount) {
        double value = Double.parseDouble(rawAmount);
        return String.format(Locale.ENGLISH, "\u20AC%.2f", value);
    }
}