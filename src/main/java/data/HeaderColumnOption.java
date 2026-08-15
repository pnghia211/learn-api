package data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum HeaderColumnOption {
    ID("Id", "#", "ID", "Id"),
    DATE("Date"),
    STATUS("Status"),
    EMAIL("Email"),
    AMOUNT("Amount"),
    CATEGORY("Category"),
    NAME("Name"),
    PRICE("Price"),
    STOCK("Stock");

    private final String label;
    private final Set<String> headerAliases;

    HeaderColumnOption(String label, String... extraAliases) {
        this.label = label;
        Set<String> allAliases = new HashSet<>(Arrays.asList(extraAliases));
        allAliases.add(label);  // label itself is always a valid alias
        this.headerAliases = Set.copyOf(allAliases);
    }

    public String label() {
        return label;
    }

    public Set<String> getHeaderAliases() {
        return headerAliases;
    }

    public boolean matchesHeader(String actual) {
        return headerAliases.contains(actual);
    }

    public static HeaderColumnOption fromHeaderValue(String actualHeaderText) {
        for (HeaderColumnOption option : values()) {
            if (option.matchesHeader(actualHeaderText)) {
                return option;
            }
        }
        throw new IllegalArgumentException(
                "Actual header text '" + actualHeaderText +
                        "' has no known HeaderColumnOption mapping. Add an alias entry for it.");
    }
}