package data;

public enum TableLabel {
    WITH_INFINITE_SCROLL("with-infinite-scroll"),
    USAGE("usage"),
    WITH_COLUMN_VISIBILITY("with-column-visibility"),
    WITH_ROW_ACTIONS("with-row-actions"),
    WITH_ROW_SELECTION("with-row-selection"),
    WITH_COLUMN_FOOTER("with-column-footer"),
    WITH_COLUMN_SPAN("with-column-span"),
    WITH_COLUMN_SORTING("with-column-sorting"),
    WITH_TREE_DATA("with-tree-data"),
    WITH_PAGINATION("with-pagination"),
    WITH_ROW_PINNING("with-row-pinning"),
    WITH_GROUPED_ROWS("with-grouped-rows");

    private final String label;

    TableLabel(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

}
