package model.user;

import model.book.Book;

import java.util.ArrayList;
import java.util.List;

public class Reader extends User{

    private List<Book> borrowedBooks;
    private static final int MAX_BOOKS = 5;

    public Reader(int id, String name, String username, String password) {
        super(id, name, username, password, Role.READER);
        this.borrowedBooks = new ArrayList<>();
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

   public boolean canBorrowBook(){
        return borrowedBooks.size() < MAX_BOOKS;
   }

   public void addBook(Book book){
        if (canBorrowBook()){
            borrowedBooks.add(book);
        }
   }

   public void removeBook(Book book){
        borrowedBooks.remove(book);
   }
}
