package data;

public enum RowActionOption {
    COPY_PAYMENT("Copy payment ID");
    private final String label;

    RowActionOption(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
