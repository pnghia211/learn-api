package data;

public enum SortingOption {
    ASC("Asc"),
    DESC("Desc");
    private final String label;

    SortingOption(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
