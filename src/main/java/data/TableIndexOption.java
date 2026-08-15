package data;

public enum TableIndexOption {
    PRIMARY(1),
    SECONDARY(2);
    private final int label;

    TableIndexOption(int label) {
        this.label = label;
    }

    public int label() {
        return label;
    }
}
