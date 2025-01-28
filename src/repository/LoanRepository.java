package repository;

import model.book.Book;
import model.transaction.Loan;
import model.transaction.LoanStatus;
import model.user.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoanRepository {
    private Map<Integer, Loan> loans;
    private int lastId;

    public LoanRepository() {
        this.loans = new HashMap<>();
        this.lastId = 0;
    }

    // yeni ödünç alma kaydı oluşturma
    public Loan save(Book book, Reader reader) {
        Loan loan = new Loan(++lastId, book, reader);
        loans.put(lastId, loan);

        // kitap ve okuyucu durumlarını güncelleme
        book.setAvailable(false);
        reader.addBook(book);

        return loan;
    }

    // ID'ye göre ödünç alma kaydı bulma
    public Loan findById(int id) {
        return loans.get(id);
    }

    // tüm ödünç alma kayıtlarını listeleme
    public List<Loan> findAll() {
        return loans.values().stream().toList();
    }

    // iade edilmemiş ödünç alma kayıtlarını listeleme
    public List<Loan> findActiveLoans() {
        return loans.values().stream()
                .filter(loan -> loan.getStatus() == LoanStatus.ACTIVE ||
                        loan.getStatus() == LoanStatus.OVERDUE)
                .collect(Collectors.toList());
    }

    // belirli bir okuyucunun ödünç alma kayıtlarını listeleme
    public List<Loan> findByReader(Reader reader) {
        return loans.values().stream()
                .filter(loan -> loan.getReader().getId() == reader.getId())
                .collect(Collectors.toList());
    }

    // belirli bir okuyucunun iade edilmemiş ödünç alma kayıtlarını listeleme
    public List<Loan> findActiveByReader(Reader reader) {
        return loans.values().stream()
                .filter(loan -> loan.getReader().getId() == reader.getId() &&
                        (loan.getStatus() == LoanStatus.ACTIVE ||
                                loan.getStatus() == LoanStatus.OVERDUE))
                .collect(Collectors.toList());
    }

    // gecikmiş ödünç alma kayıtlarını listeleme
    public List<Loan> findOverdueLoans() {
        return loans.values().stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    // kitap iade işlemi
    public boolean returnBook(int loanId) {
        Loan loan = loans.get(loanId);
        if (loan != null && loan.getStatus() == LoanStatus.ACTIVE) {
            loan.returnBook();
            return true;
        }
        return false;
    }
}
