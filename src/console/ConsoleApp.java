package console;

import model.book.Category;
import repository.BillRepository;
import repository.BookRepository;
import repository.LoanRepository;
import repository.UserRepository;
import service.BillService;
import service.BookService;
import service.LoanService;
import service.UserService;

public class ConsoleApp {
    private final MenuManager menuManager;
    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;
    private final BillService billService;

    public ConsoleApp() {
        // Repository'leri oluştur
        BookRepository bookRepository = new BookRepository();
        UserRepository userRepository = new UserRepository();
        LoanRepository loanRepository = new LoanRepository();
        BillRepository billRepository = new BillRepository();

        // Service'leri oluştur
        this.userService = new UserService(userRepository, loanRepository);
        this.billService = new BillService(billRepository, userService);
        this.bookService = new BookService(bookRepository, loanRepository);
        this.loanService = new LoanService(loanRepository, bookRepository, userRepository, userService, billService);


        // MenuManager'ı oluştur
        this.menuManager = new MenuManager(userService, bookService, loanService, billService);

        // Test verilerini yükle
        loadTestData();
    }

    public void start() {
        System.out.println("Kütüphane Yönetim Sistemi'ne Hoş Geldiniz!");
        menuManager.showMainMenu();
        System.out.println("İyi günler!");
    }

    private void loadTestData() {
        try {
            userService.login("admin", "admin123");

            // Test kitapları
            bookService.addBook("1984", "George Orwell", Category.FICTION);
            bookService.addBook("Suç ve Ceza", "Fyodor Dostoyevski", Category.LITERATURE);
            bookService.addBook("Yapay Zeka", "John McCarthy", Category.COMPUTER_SCIENCE);
            bookService.addBook("İnsan Ne İle Yaşar", "Lev Tolstoy", Category.PHILOSOPHY);
            bookService.addBook("Dönüşüm", "Franz Kafka", Category.FICTION);

            // Test kullanıcıları
            userService.addReader("İrfan Subaşı", "irfan", "irfan123");

            userService.logout();

            System.out.println("Test verileri yüklendi.");
            System.out.println("Admin girişi için -> Kullanıcı adı: admin, Şifre: admin123");
            System.out.println("Okuyucu girişi için -> Kullanıcı adı: irfan, Şifre: irfan123");

        } catch (Exception e) {
            System.out.println("Test verileri yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.start();
    }
}
