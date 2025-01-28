package model.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill {
    private int id;
    private Loan loan;
    private double amount;
    private boolean isPaid;
    private LocalDateTime createdAt;
    private BillType billType;


    // Constructor
    public Bill(int id, Loan loan, double amount, BillType billType) {
        this.id = id;
        this.loan = loan;
        this.amount = amount;
        this.billType = billType;
        this.isPaid = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters ve Setters
    public int getId() {
        return id;
    }

    public Loan getLoan() {
        return loan;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BillType getBillType() {
        return billType;
    }

    // Ödeme işlemi -- ödeme işlemi gerçekleşince ödeme durumu değişir
    public void pay() {
        this.isPaid = true;
    }

    @Override
    public String toString() {
        return String.format("Fatura ID: %d | Tür: %s | Tutar: %.2f TL | Durum: %s | Tarih: %s | Kitap: %s | Okuyucu: %s",
                id,
                billType.getDisplayName(),
                amount,
                isPaid ? "Ödendi" : "Ödenmedi",
                createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                loan.getBook().getTitle(),
                loan.getReader().getName());
    }
}
