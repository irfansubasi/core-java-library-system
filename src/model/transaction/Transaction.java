package model.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Transaction {

    private int id;
    private LocalDateTime transactionDate;
    private TransactionType type;
    private String description;

    public Transaction(int id, TransactionType type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.transactionDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("İşlem ID: %d | Tür: %s | Tarih: %s | Açıklama: %s",
                id,
                type.getDisplayName(),
                transactionDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                description);
    }

}
