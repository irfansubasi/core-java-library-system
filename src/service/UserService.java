package service;

import model.transaction.Loan;
import model.user.Admin;
import model.user.Reader;
import model.user.User;
import repository.LoanRepository;
import repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private User currentUser; // giriş yapmış kullanıcı

    public UserService(UserRepository userRepository, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.currentUser = null;
    }

    // GİRİŞ İŞLEMLERİ

    //login olma
    public User login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı adı veya şifre boş olamaz");
        }

        User user = userRepository.authenticate(username.trim(), password.trim());
        if (user == null) {
            throw new IllegalArgumentException("Kullanıcı adı veya şifre hatalı");
        }

        this.currentUser = user;
        return user;
    }

    //logout olma
    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // ADMİN İŞLEMLERİ

    //okuyucu ekleme
    public Reader addReader(String name, String username, String password) {
        checkAdminAccess();
        validateUserData(name, username, password);

        Reader reader = userRepository.saveReader(name.trim(), username.trim(), password.trim());
        if (reader == null) {
            throw new IllegalStateException("Bu kullanıcı adı zaten kullanılıyor");
        }
        return reader;
    }

    //admin ekleme
    public Admin addAdmin(String name, String username, String password) {
        checkAdminAccess();
        validateUserData(name, username, password);

        Admin admin = userRepository.saveAdmin(name.trim(), username.trim(), password.trim());
        if (admin == null) {
            throw new IllegalStateException("Bu kullanıcı adı zaten kullanılıyor");
        }
        return admin;
    }

    //kullanıcı silme
    public boolean removeUser(int userId) {
        checkAdminAccess();

        // admin kendini silemesin
        if (currentUser.getId() == userId) {
            throw new IllegalStateException("Kendi hesabınızı silemezsiniz");
        }

        // aktif ödünç alma işlemi olan kullanıcı silinemez
        User user = userRepository.findById(userId);
        if (user instanceof Reader) {
            List<Loan> activeLoans = loanRepository.findActiveByReader((Reader) user);
            if (!activeLoans.isEmpty()) {
                throw new IllegalStateException("Aktif ödünç alma işlemi olan kullanıcı silinemez");
            }
        }

        return userRepository.delete(userId);
    }

    //tüm kullanıcıları listeleme
    public List<User> getAllUsers() {
        checkAdminAccess();
        return userRepository.findAll();
    }

    //tüm okuyucuları listeleme
    public List<Reader> getAllReaders() {
        checkAdminAccess();
        return userRepository.findAllReaders();
    }

    //tüm adminleri listeleme
    public List<Admin> getAllAdmins() {
        checkAdminAccess();
        return userRepository.findAllAdmins();
    }

    //idye göre kullanıcı bulma
    public User getUserById(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Kullanıcı bulunamadı");
        }
        return user;
    }


    // yardımcı metodlar

    //admin yetkisi denetleme
    private void checkAdminAccess() {
        if (currentUser == null || !(currentUser instanceof Admin)) {
            throw new SecurityException("Bu işlem için admin yetkisi gerekiyor");
        }
    }

    //registerda validate etme
    private void validateUserData(String name, String username, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("İsim boş olamaz");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı adı boş olamaz");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Şifre boş olamaz");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Şifre en az 6 karakter olmalıdır");
        }
    }
}
