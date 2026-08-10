package data;

public enum HeaderColumnOption {
    ID("Id", "#"),
    DATE("Date", "Date"),
    STATUS("Status", "Status"),
    EMAIL("Email", "Email"),
    AMOUNT("Amount", "Amount");

    private final String dropdownLabel;
    private final String headerLabel;

    HeaderColumnOption(String dropdownLabel, String headerLabel) {
        this.dropdownLabel = dropdownLabel;
        this.headerLabel = headerLabel;
    }

    public String dropdownLabel() {
        return dropdownLabel;
    }

    public String headerLabel() {
        return headerLabel;
    }
}
