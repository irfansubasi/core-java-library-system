package model.book;

public enum Category {

    FICTION("Kurgu"),
    SCIENCE("Bilim"),
    HISTORY("Tarih"),
    LITERATURE("Edebiyat"),
    PHILOSOPHY("Felsefe"),
    PSYCHOLOGY("Psikoloji"),
    COMPUTER_SCIENCE("Bilgisayar Bilimi");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
