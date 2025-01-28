package service;

import model.book.Book;
import model.transaction.Loan;
import model.transaction.LoanStatus;
import model.user.Admin;
import model.user.Reader;
import model.user.User;
import repository.BookRepository;
import repository.LoanRepository;
import repository.UserRepository;

import java.util.List;

public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BillService billService;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository,
                       UserService userService,
                       BillService billService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.billService = billService;
    }

    //ödünç alma işlemi
    public Loan borrowBook(int bookId, int readerId) {
        //yetki kontrolü
        checkReaderAccess(readerId);

        //kitap kontrolü
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Kitap bulunamadı");
        }
        if (!book.isAvailable()) {
            throw new IllegalStateException("Kitap şu anda müsait değil");
        }

        //okuyucu kontrolü
        User user = userRepository.findById(readerId);
        if (!(user instanceof Reader)) {
            throw new IllegalArgumentException("Geçersiz okuyucu ID'si");
        }
        Reader reader = (Reader) user;

        //kitap limiti kontrolü
        if (!reader.canBorrowBook()) {
            throw new IllegalStateException("Maksimum kitap limitine ulaşıldı (5 kitap)");
        }

        //gecikmiş kitap kontrolü
        List<Loan> activeLoans = loanRepository.findActiveByReader(reader);
        boolean hasOverdueBooks = activeLoans.stream().anyMatch(Loan::isOverdue);
        if (hasOverdueBooks) {
            throw new IllegalStateException("Gecikmiş kitaplarınız var. Yeni kitap alamazsınız");
        }

        //ödünç alma işlemi
        Loan loan = loanRepository.save(book, reader);

        //otomatik fatura oluşturma
        try {
            billService.createBorrowFee(loan);
        } catch (Exception e) {
            // fatura oluşturma hatası ödünç alma işlemini etkilemesin
            System.out.println("Fatura oluşturulurken bir hata oluştu: " + e.getMessage());
        }

        return loan;
    }


    //iade işlemi
    public void returnBook(int loanId) {
        Loan loan = loanRepository.findById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Ödünç alma kaydı bulunamadı");
        }

        //yetki kontrolü
        checkReaderAccess(loan.getReader().getId());

        //durum kontrolü
        if (loan.getStatus() != LoanStatus.ACTIVE &&
                loan.getStatus() != LoanStatus.OVERDUE) {
            throw new IllegalStateException("Bu kitap zaten iade edilmiş");
        }

        //iade işlemi
        loanRepository.returnBook(loanId);

        //gecikme varsa otomatik fatura oluştur
        if (loan.isOverdue()) {
            try {
                billService.createLateFee(loan);
            } catch (Exception e) {
                System.out.println("Gecikme faturası oluşturulurken bir hata oluştu: " + e.getMessage());
            }
        }
    }


    //listeleme işlemleri
    public List<Loan> getAllLoans() {
        checkAdminAccess();
        return loanRepository.findAll();
    }

    public List<Loan> getActiveLoans() {
        checkAdminAccess();
        return loanRepository.findActiveLoans();
    }

    public List<Loan> getOverdueLoans() {
        checkAdminAccess();
        return loanRepository.findOverdueLoans();
    }

    public List<Loan> getReaderLoans(int readerId) {
        checkReaderAccess(readerId);
        Reader reader = (Reader) userRepository.findById(readerId);
        return loanRepository.findByReader(reader);
    }

    //yardımcı metodlar
    private void checkAdminAccess() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null || !(currentUser instanceof Admin)) {
            throw new SecurityException("Bu işlem için admin yetkisi gerekiyor");
        }
    }

    private void checkReaderAccess(int readerId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("Giriş yapmanız gerekiyor");
        }
        if (!(currentUser instanceof Admin) && currentUser.getId() != readerId) {
            throw new SecurityException("Sadece kendi işlemlerinizi yönetebilirsiniz");
        }
    }
}
