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
    DONE("Done"),
    COUNTRY_VIETNAM("Vietnam"),
    OPTION_1("Option 1"),
    OPTION_2("Option 2"),
    OPTION_3("Option 3");

    private final String label;

    DropdownOption(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
