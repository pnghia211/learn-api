package data;

public enum ComponentIndexOption {
    PRIMARY(1),
    SECONDARY(2);
    private final int label;

    ComponentIndexOption(int label) {
        this.label = label;
    }

    public int label() {
        return label;
    }
}
