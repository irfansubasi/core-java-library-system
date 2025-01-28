package console;

import model.book.Book;
import model.book.Category;
import model.transaction.Bill;
import model.transaction.Loan;
import model.user.Admin;
import model.user.Reader;
import model.user.User;
import service.BillService;
import service.BookService;
import service.LoanService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

public class MenuManager {
    private final Scanner scanner;
    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;
    private final BillService billService;

    public MenuManager(UserService userService, BookService bookService, LoanService loanService, BillService billService) {
        this.scanner = new Scanner(System.in);
        this.userService = userService;
        this.bookService = bookService;
        this.loanService = loanService;
        this.billService = billService;
    }

    //ana menu
    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== KÜTÜPHANE SİSTEMİ ===");
            System.out.println("1. Giriş Yap");
            System.out.println("0. Çıkış");

            int choice = getIntInput("Seçiminiz: ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 0:
                    System.out.println("Program sonlandırılıyor...");
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }

    //login işlemleri menu
    private void login() {
        System.out.println("\n=== GİRİŞ ===");
        String username = getStringInput("Kullanıcı Adı: ");
        String password = getStringInput("Şifre: ");

        try {
            User user = userService.login(username, password);
            System.out.println("Giriş başarılı! Hoş geldiniz, " + user.getName());

            if (user instanceof Admin) {
                showAdminMenu();
            } else {
                showReaderMenu();
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    //admin işlemleri menu
    private void showAdminMenu() {
        while (true) {
            System.out.println("\n=== ADMİN MENÜSÜ ===");
            System.out.println("1. Kitap İşlemleri");
            System.out.println("2. Kullanıcı İşlemleri");
            System.out.println("3. Ödünç İşlemleri");
            System.out.println("4. Fatura Listesi");
            System.out.println("0. Çıkış Yap");

            int choice = getIntInput("Seçiminiz: ");

            switch (choice) {
                case 1:
                    showBookManagementMenu();
                    break;
                case 2:
                    showUserManagementMenu();
                    break;
                case 3:
                    showLoanManagementMenu();
                    break;
                case 4:
                    showBillListMenu();
                    break;
                case 0:
                    userService.logout();
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }

    //okuyucu işlemleri menu
    private void showReaderMenu() {
        while (true) {
            System.out.println("\n=== OKUYUCU MENÜSÜ ===");
            System.out.println("1. Kitapları Listele");
            System.out.println("2. Kitap Ara");
            System.out.println("3. Kitap Ödünç Al");
            System.out.println("4. Kitap İade Et");
            System.out.println("5. Ödünç Aldığım Kitaplar");
            System.out.println("6. Faturalarım");
            System.out.println("0. Çıkış Yap");

            int choice = getIntInput("Seçiminiz: ");

            switch (choice) {
                case 1:
                    listBooks();
                    break;
                case 2:
                    showSearchBookMenu();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    showMyLoans();
                    break;
                case 6:
                    showMyBills();
                    break;
                case 0:
                    userService.logout();
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }


    // input alma yardımcı metodlar
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Lütfen geçerli bir sayı girin!");
            }
        }
    }

    private Category getCategoryInput() {
        while (true) {
            System.out.println("\nKategoriler:");
            Category[] categories = Category.values();
            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i].getDisplayName());
            }

            int choice = getIntInput("Kategori seçin (1-" + categories.length + "): ");
            if (choice >= 1 && choice <= categories.length) {
                return categories[choice - 1];
            }
            System.out.println("Geçersiz seçim!");
        }
    }



    // admin kitap işlemleri menüsü
    private void showBookManagementMenu() {
        while (true) {
            System.out.println("\n=== KİTAP İŞLEMLERİ ===");
            System.out.println("1. Kitap Ekle");
            System.out.println("2. Kitap Sil");
            System.out.println("3. Kitapları Listele");
            System.out.println("4. Kategoriye Göre Listele");
            System.out.println("5. Kitap Güncelle");
            System.out.println("0. Geri");

            int choice = getIntInput("Seçiminiz: ");

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    listBooks();
                    break;
                case 4:
                    listBooksByCategory();
                    break;
                case 5:
                    updateBook();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }

    // Admin - Kullanıcı İşlemleri Menüsü
    private void showUserManagementMenu() {
        while (true) {
            System.out.println("\n=== KULLANICI İŞLEMLERİ ===");
            System.out.println("1. Okuyucu Ekle");
            System.out.println("2. Admin Ekle");
            System.out.println("3. Kullanıcı Sil");
            System.out.println("4. Tüm Kullanıcıları Listele");
            System.out.println("5. Tüm Okuyucuları Listele");
            System.out.println("6. Tüm Adminleri Listele");
            System.out.println("0. Geri");

            int choice = getIntInput("Seçiminiz: ");

            switch (choice) {
                case 1:
                    addReader();
                    break;
                case 2:
                    addAdmin();
                    break;
                case 3:
                    removeUser();
                    break;
                case 4:
                    listUsers();
                    break;
                case 5:
                    listReaders();
                    break;
                case 6:
                    listAdmins();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }

    // Admin - Ödünç İşlemleri Menüsü
    private void showLoanManagementMenu() {
        while (true) {
            System.out.println("\n=== ÖDÜNÇ İŞLEMLERİ ===");
            System.out.println("1. Tüm Ödünç Kayıtları");
            System.out.println("2. Aktif Ödünç Kayıtları");
            System.out.println("3. Gecikmiş Kitaplar");
            System.out.println("0. Geri");

            int choice = getIntInput("Seçiminiz: ");

            switch (choice) {
                case 1:
                    listAllLoans();
                    break;
                case 2:
                    listActiveLoans();
                    break;
                case 3:
                    listOverdueLoans();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }

    private void showBillListMenu() {
        while (true) {
            System.out.println("\n=== FATURA LİSTESİ ===");
            System.out.println("1. Tüm Faturaları Listele");
            System.out.println("2. Ödenmemiş Faturaları Listele");
            System.out.println("0. Geri");

            int choice = getIntInput("Seçiminiz: ");

            switch (choice) {
                case 1:
                    listAllBills();
                    break;
                case 2:
                    listUnpaidBills();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }


    private void showSearchBookMenu() {
        while (true) {
            System.out.println("\n=== KİTAP ARA ===");
            System.out.println("1. ID'ye Göre Ara");
            System.out.println("2. Adına Göre Ara");
            System.out.println("3. Yazara Göre Ara");
            System.out.println("0. Geri");

            int choice = getIntInput("Seçiminiz: ");

            switch (choice) {
                case 1:
                    searchBookByID();
                    break;
                case 2:
                    searchBooksByTitle();
                    break;
                case 3:
                    searchBooksByAuthor();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }
        }
    }

    // Kitap İşlemleri
    private void addBook() {
        try {
            System.out.println("\n=== YENİ KİTAP EKLE ===");
            String title = getStringInput("Kitap Adı: ");
            String author = getStringInput("Yazar: ");
            Category category = getCategoryInput();

            Book book = bookService.addBook(title, author, category);
            System.out.println("Kitap başarıyla eklendi: " + book);
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void removeBook() {
        try {
            System.out.println("\n=== KİTAP SİL ===");
            listBooks();
            int bookId = getIntInput("Silinecek Kitap ID: ");

            if (bookService.removeBook(bookId)) {
                System.out.println("Kitap başarıyla silindi.");
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    //kitap güncelle
    private void updateBook(){
        try {
            System.out.println("\n=== KİTAP GÜNCELLE ===");
            listBooks();

            int bookId = getIntInput("Güncellenecek Kitap ID: ");
            Book existingBook = bookService.getBookById(bookId);

            if (existingBook == null) {
                System.out.println("Kitap bulunamadı!");
                return;
            }

            System.out.println("\nMevcut Bilgiler:");
            System.out.println(existingBook);

            // yeni değerleri al
            String title = getStringInput("Yeni Kitap Adı (Enter ile geç): ");
            String author = getStringInput("Yeni Yazar (Enter ile geç): ");

            System.out.println("\nKategoriler:");
            Category[] categories = Category.values();
            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i].getDisplayName());
            }
            System.out.println("0. Değiştirme");
            int categoryChoice = getIntInput("Yeni Kategori Seçimi: ");

            // değiştirilmeyen alanlar için mevcut değerleri kullan
            Book updatedBook = new Book(
                    existingBook.getId(),
                    title.isEmpty() ? existingBook.getTitle() : title,
                    author.isEmpty() ? existingBook.getAuthor() : author,
                    categoryChoice == 0 ? existingBook.getCategory() : categories[categoryChoice - 1]
            );
            updatedBook.setAvailable(existingBook.isAvailable()); // mevcut durumu koru

            // güncelleme işlemini yap
            bookService.updateBook(updatedBook);
            System.out.println("Kitap başarıyla güncellendi!");
            System.out.println("Yeni Bilgiler: " + updatedBook);

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void listBooks() {
        System.out.println("\n=== TÜM KİTAPLAR ===");
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("Hiç kitap bulunamadı.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void listBooksByCategory() {
        try {
            Category category = getCategoryInput();
            System.out.println("\n=== " + category.getDisplayName() + " KATEGORİSİNDEKİ KİTAPLAR ===");
            List<Book> books = bookService.getBooksByCategory(category);
            if (books.isEmpty()) {
                System.out.println("Bu kategoride kitap bulunamadı.");
            } else {
                books.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }


    // Kullanıcı İşlemleri
    private void addReader() {
        try {
            System.out.println("\n=== YENİ OKUYUCU EKLE ===");
            String name = getStringInput("Ad Soyad: ");
            String username = getStringInput("Kullanıcı Adı: ");
            String password = getStringInput("Şifre: ");

            Reader reader = userService.addReader(name, username, password);
            System.out.println("Okuyucu başarıyla eklendi: " + reader);
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void addAdmin() {
        try {
            System.out.println("\n=== YENİ ADMİN EKLE ===");
            String name = getStringInput("Ad Soyad: ");
            String username = getStringInput("Kullanıcı Adı: ");
            String password = getStringInput("Şifre: ");

            Admin admin = userService.addAdmin(name, username, password);
            System.out.println("Admin başarıyla eklendi: " + admin);
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void removeUser() {
        try {
            System.out.println("\n=== KULLANICI SİL ===");
            listUsers();
            int userId = getIntInput("Silinecek Kullanıcı ID: ");

            if (userService.removeUser(userId)) {
                System.out.println("Kullanıcı başarıyla silindi.");
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void listUsers() {
        System.out.println("\n=== TÜM KULLANICILAR ===");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Hiç kullanıcı bulunamadı.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private void listReaders() {
        System.out.println("\n=== OKUYUCULAR ===");
        List<Reader> readers = userService.getAllReaders();
        if (readers.isEmpty()) {
            System.out.println("Hiç okuyucu bulunamadı.");
        } else {
            readers.forEach(System.out::println);
        }
    }

    private void listAdmins() {
        System.out.println("\n=== ADMİNLER ===");
        List<Admin> admins = userService.getAllAdmins();
        if (admins.isEmpty()) {
            System.out.println("Hiç admin bulunamadı.");
        } else {
            admins.forEach(System.out::println);
        }
    }

    // Ödünç İşlemleri
    private void borrowBook() {
        try {
            System.out.println("\n=== KİTAP ÖDÜNÇ AL ===");
            listAvailableBooks();
            int bookId = getIntInput("Ödünç Alınacak Kitap ID: ");

            Loan loan = loanService.borrowBook(bookId, userService.getCurrentUser().getId());
            System.out.println("Kitap başarıyla ödünç alındı: " + loan);
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            System.out.println("\n=== KİTAP İADE ET ===");
            showMyLoans();
            int loanId = getIntInput("İade Edilecek Ödünç ID: ");

            loanService.returnBook(loanId);
            System.out.println("Kitap başarıyla iade edildi.");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void listAllLoans() {
        System.out.println("\n=== TÜM ÖDÜNÇ KAYITLARI ===");
        List<Loan> loans = loanService.getAllLoans();
        if (loans.isEmpty()) {
            System.out.println("Hiç ödünç kaydı bulunamadı.");
        } else {
            loans.forEach(System.out::println);
        }
    }

    private void listActiveLoans() {
        System.out.println("\n=== AKTİF ÖDÜNÇ KAYITLARI ===");
        List<Loan> loans = loanService.getActiveLoans();
        if (loans.isEmpty()) {
            System.out.println("Aktif ödünç kaydı bulunamadı.");
        } else {
            loans.forEach(System.out::println);
        }
    }

    private void listOverdueLoans() {
        System.out.println("\n=== GECİKMİŞ KİTAPLAR ===");
        List<Loan> loans = loanService.getOverdueLoans();
        if (loans.isEmpty()) {
            System.out.println("Gecikmiş kitap bulunmuyor.");
        } else {
            loans.forEach(System.out::println);
        }
    }

    private void showMyLoans() {
        System.out.println("\n=== ÖDÜNÇ ALDIĞIM KİTAPLAR ===");
        List<Loan> loans = loanService.getReaderLoans(userService.getCurrentUser().getId());
        if (loans.isEmpty()) {
            System.out.println("Ödünç aldığınız kitap bulunmuyor.");
        } else {
            loans.forEach(System.out::println);
        }
    }


    private void listAvailableBooks() {
        System.out.println("\n=== MÜSAİT KİTAPLAR ===");
        List<Book> books = bookService.getAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("Müsait kitap bulunmuyor.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void searchBooksByTitle() {
        try {
            System.out.println("\n=== KİTAP ADINA GÖRE ARA ===");
            String query = getStringInput("Arama Metni: ");

            List<Book> books = bookService.searchBooksByTitle(query);
            if (books.isEmpty()) {
                System.out.println("Aranan kriterlere uygun kitap bulunamadı.");
            } else {
                System.out.println("\nBulunan Kitaplar:");
                books.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void searchBookByID() {
        try {
            System.out.println("\n=== ID'YE GÖRE ARA ===");
            int id = getIntInput("ID Giriniz: ");

            Book book = bookService.getBookById(id);
            System.out.println(book);

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void searchBooksByAuthor() {
        try {
            System.out.println("\n=== YAZARA GÖRE ARA ===");
            String query = getStringInput("Arama Metni: ");

            List<Book> books = bookService.getBooksByAuthor(query);
            if (books.isEmpty()) {
                System.out.println("Aranan kriterlere uygun kitap bulunamadı.");
            } else {
                System.out.println("\nBulunan Kitaplar:");
                books.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    // Fatura işlem metodları

    private void listAllBills() {
        System.out.println("\n=== TÜM FATURALAR ===");
        List<Bill> bills = billService.getAllBills();
        if (bills.isEmpty()) {
            System.out.println("Hiç fatura bulunamadı.");
        } else {
            bills.forEach(System.out::println);
        }
    }

    private void listUnpaidBills() {
        System.out.println("\n=== ÖDENMEMİŞ FATURALAR ===");
        List<Bill> bills = billService.getUnpaidBills();
        if (bills.isEmpty()) {
            System.out.println("Ödenmemiş fatura bulunamadı.");
        } else {
            bills.forEach(System.out::println);
        }
    }

    private void showMyBills() {
        try {
            System.out.println("\n=== FATURALARIM ===");
            int readerId = userService.getCurrentUser().getId();
            List<Bill> bills = billService.getReaderBills(readerId);

            if (bills.isEmpty()) {
                System.out.println("Hiç faturanız bulunmuyor.");
                return;
            }

            bills.forEach(System.out::println);

            System.out.println("\n1. Fatura Öde");
            System.out.println("0. Geri");

            int choice = getIntInput("Seçiminiz: ");
            if (choice == 1) {
                payBill();
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void payBill() {
        try {
            System.out.println("\n=== FATURA ÖDE ===");
            int readerId = userService.getCurrentUser().getId();
            List<Bill> unpaidBills = billService.getReaderUnpaidBills(readerId);

            if (unpaidBills.isEmpty()) {
                System.out.println("Ödenmemiş faturanız bulunmuyor.");
                return;
            }

            System.out.println("Ödenmemiş Faturalar:");
            unpaidBills.forEach(System.out::println);

            int billId = getIntInput("Ödenecek Fatura ID: ");
            billService.payBill(billId);
            System.out.println("Fatura başarıyla ödendi.");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

}
