package model.transaction;

public enum BillType {

    BORROW_FEE("Ödünç Alma Ücreti"),
    LATE_FEE("Gecikme Ücreti");

    private final String displayName;

    BillType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
