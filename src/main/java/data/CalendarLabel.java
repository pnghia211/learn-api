package data;

public enum CalendarLabel {
    USAGE("usage"),
    DISABLED_DATES("with-disabled-dates"),
    MULTIPLES("multiple"),
    RANGE("range"),
    DATE_PICKER("as-a-date-picker"),
    DATE_RANGE_PICKER("as-a-date-range-picker");

    private final String label;

    CalendarLabel(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

}
