package service;

import model.book.Book;
import model.book.Category;
import repository.BookRepository;
import repository.LoanRepository;

import java.util.List;

public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public BookService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    // ADMİN İŞLEMLERİ


    //kitap ekleme
    public Book addBook(String title, String author, Category category) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Kitap başlığı boş olamaz");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Yazar adı boş olamaz");
        }
        return bookRepository.save(title.trim(), author.trim(), category);
    }

    //kitap silme
    public boolean removeBook(int bookId) {
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Kitap bulunamadı");
        }
        // kitap ödünç alınmışsa silinemez
        if (!book.isAvailable()) {
            throw new IllegalStateException("Ödünç alınmış kitap silinemez");
        }
        return bookRepository.delete(bookId);
    }

    //kitap güncelleme -- bunu ekle
    public Book updateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Kitap bilgisi boş olamaz");
        }
        Book existingBook = bookRepository.findById(book.getId());
        if (existingBook == null) {
            throw new IllegalArgumentException("Güncellenecek kitap bulunamadı");
        }
        return bookRepository.update(book);
    }

    // GENEL İŞLEMLER

    //tüm kitapları listeleme
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //müsait kitapları listeleme
    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }

    //idye göre kitabı bulma
    public Book getBookById(int id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new IllegalArgumentException("Kitap bulunamadı");
        }
        return book;
    }

    //adına göre kitabı bulma
    public List<Book> searchBooksByTitle(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Arama metni boş olamaz");
        }
        return bookRepository.findByTitle(query.trim());
    }

    //kategoriye göre kitabı bulma
    public List<Book> getBooksByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Kategori boş olamaz");
        }
        return bookRepository.findByCategory(category);
    }

    //yazara göre kitabı bulma
    public List<Book> getBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Yazar adı boş olamaz");
        }
        return bookRepository.findByAuthor(author.trim());
    }



}
