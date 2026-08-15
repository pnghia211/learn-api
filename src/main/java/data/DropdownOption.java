package data;

public enum DropdownOption {
    ID("Id"),
    DATE("Date"),
    STATUS("Status"),
    EMAIL("Email"),
    AMOUNT("Amount"),
    ASC("Asc"),
    DESC("Desc");

    private final String label;

    DropdownOption(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
