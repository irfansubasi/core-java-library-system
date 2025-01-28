package model.transaction;

public enum LoanStatus {

    ACTIVE("Devam Ediyor"),
    RETURNED("İade Edildi"),
    OVERDUE("Gecikmiş"),
    LOST("Kayıp");

    private final String displayName;

    LoanStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
