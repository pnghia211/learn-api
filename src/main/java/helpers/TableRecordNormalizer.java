package helpers;

import model.TableRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TableRecordNormalizer {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d, HH:mm", Locale.ENGLISH);

    public static TableRecord normalizeExpected(TableRecord raw) {
        String canonicalDate = convertIsoToDisplayFormat(raw.date());
        return new TableRecord(raw.id(), canonicalDate, raw.status(), raw.email(), raw.amount());
    }

    public static TableRecord normalizeActual(TableRecord rendered) {
        String rawId = stripIdPrefix(rendered.id());
        String rawStatus = rendered.status().toLowerCase(Locale.ENGLISH);
        String rawAmount = stripCurrencyFormatting(rendered.amount());

        return new TableRecord(rawId, rendered.date(), rawStatus, rendered.email(), rawAmount);
    }

    private static String convertIsoToDisplayFormat(String isoDate) {
        LocalDateTime dateTime = LocalDateTime.parse(isoDate, ISO_FORMATTER);
        return dateTime.format(DISPLAY_FORMATTER);
    }

    private static String stripIdPrefix(String displayedId) {
        return displayedId.replaceFirst("^#", "");
    }

    private static String stripCurrencyFormatting(String displayedAmount) {
        // "€594.00" -> "594"
        // Strips currency symbol, thousands separators, and decimal portion,
        // since the raw JSON amount is a whole-number int (e.g. 594).
        String digitsOnly = displayedAmount.replaceAll("[^0-9.]", "");
        double value = Double.parseDouble(digitsOnly);
        return String.valueOf((int) value);
    }
}