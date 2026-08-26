package data;

public enum DropdownOption {
    ID("Id"),
    DATE("Date"),
    STATUS("Status"),
    EMAIL("Email"),
    AMOUNT("Amount"),
    BACKLOG("Backlog"),
    TO_DO("Todo"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String label;

    DropdownOption(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
