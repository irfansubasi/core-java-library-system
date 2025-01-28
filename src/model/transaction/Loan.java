package model.transaction;

import model.book.Book;
import model.user.Reader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Loan extends Transaction {
    private Book book;
    private Reader reader;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private LoanStatus status;


    // Constructor
    public Loan(int id, Book book, Reader reader) {
        super(id, TransactionType.LOAN,
                String.format("%s kitabı %s tarafından ödünç alındı",
                        book.getTitle(), reader.getName()));
        this.book = book;
        this.reader = reader;
        this.dueDate = LocalDateTime.now().plusDays(15); // 15 günlük ödünç alma süresi
        this.status = LoanStatus.ACTIVE;
    }

    // Getters
    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    // İade işlemi -- burada kitap iade edildikten sonra teslim tarihi, müsaitlik durumu gibi işlemler ayarlanıyor
    public void returnBook() {
        this.returnDate = LocalDateTime.now();
        this.status = LoanStatus.RETURNED;
        this.book.setAvailable(true);
        this.reader.removeBook(this.book);
        this.setDescription(String.format("%s kitabı %s tarafından iade edildi",
                book.getTitle(), reader.getName()));
    }



    // Gecikme kontrolü -- kitap gecikti mi kontrolü
    public boolean isOverdue() {
        if (status == LoanStatus.RETURNED) {
            return false;
        }
        boolean isLate = LocalDateTime.now().isAfter(dueDate);
        if (isLate && status == LoanStatus.ACTIVE) {
            this.status = LoanStatus.OVERDUE;
        }
        return isLate;
    }

    // Gecikme gün sayısı -- chronounit ile günler arasındaki farklar
    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDateTime.now());
    }

    @Override
    public String toString() {
        String baseInfo = String.format("Ödünç ID: %d | Kitap: %s | Okuyucu: %s | Durum: %s",
                getId(), book.getTitle(), reader.getName(), status.getDisplayName());

        String dateInfo = String.format(" | Alış: %s | Teslim Tarihi: %s",
                getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if (returnDate != null) {
            dateInfo += String.format(" | İade: %s",
                    returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (isOverdue()) {
            dateInfo += String.format(" | Gecikme: %d gün", getOverdueDays());
        }

        return baseInfo + dateInfo;
    }
}
