package model.transaction;

public enum TransactionType {

    LOAN("Ödünç Alma"),
    RETURN("İade"),
    PAYMENT("Ödeme");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
