package repository;

import model.book.Book;
import model.book.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookRepository {

    private Map<Integer, Book> books;
    private int lastId;

    public BookRepository() {
        this.books = new HashMap<>();
        this.lastId = 0;
    }

    //yeni kitap ekleme --
    public Book save(String title, String author, Category category){
        lastId++;
        Book book = new Book(lastId,title,author,category);
        books.put(lastId, book);
        return book;
    }

    //kitabı idye göre bulma
    public Book findById(int id){
        return books.get(id);
    }

    //tüm kitapları listele
    public List<Book> findAll(){
        return books.values().stream().toList();
    }

    //müsait kitapları listeleme
    public List<Book> findAvailableBooks(){
        return books.values().stream().filter(Book::isAvailable).collect(Collectors.toList());
    }

    //kategoriye göre listeleme
    public List<Book> findByCategory(Category category){
        return books.values().stream().filter(book -> book.getCategory() == category).collect(Collectors.toList());
    }

    //kitap güncelleme -- ekle
    public Book update(Book book){
        if(books.containsKey(book.getId())){
            books.put(book.getId(), book);
            return book;
        }
        return null;
    }

    //kitap silme
    public boolean delete(int id){
        if(books.containsKey(id)){
            books.remove(id);
            return true;
        }
        return false;
    }

    //adına göre kitapları bulma
    public List<Book> findByTitle(String title){
        return books.values().stream().filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    //yazara göre kitapları bulma
    public List<Book> findByAuthor(String author){
        return books.values().stream().filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }
}
