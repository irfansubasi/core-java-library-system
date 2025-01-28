package repository;

import model.user.Admin;
import model.user.Reader;
import model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRepository {

    private Map<Integer, User> users;
    private int lastId;

    public UserRepository() {
        this.users = new HashMap<>();
        this.lastId = 0;
        //test
        createDefaultAdmin();
    }

    //test
    private void createDefaultAdmin(){
        Admin admin = new Admin(++lastId,"admin","admin","admin123");
        users.put(admin.getId(), admin);
    }

    //yeni okuyucu ekleme
    public Reader saveReader(String name, String username, String password) {
        // kullanıcı adı kontrolü
        if (findByUsername(username) != null) {
            return null; // kullanıcı adı mevcut
        }

        Reader reader = new Reader(++lastId, name, username, password);
        users.put(lastId, reader);
        return reader;
    }

    // yeni admin ekleme
    public Admin saveAdmin(String name, String username, String password) {
        // kullanıcı adı kontrolü
        if (findByUsername(username) != null) {
            return null; // kullanıcı adı mevcut
        }

        Admin admin = new Admin(++lastId, name, username, password);
        users.put(lastId, admin);
        return admin;
    }

    // ID'ye göre kullanıcı bulma
    public User findById(int id) {
        return users.get(id);
    }

    // kullanıcı adına göre kullanıcıyı bulma -- ilk bulduğunu getir
    public User findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    // tüm kullanıcıları listeleme
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    // sadece okuyucuları listeleme
    public List<Reader> findAllReaders() {
        return users.values().stream()
                .filter(user -> user instanceof Reader)
                .map(user -> (Reader) user)
                .collect(Collectors.toList());
    }

    // sadece adminleri listeleme
    public List<Admin> findAllAdmins() {
        return users.values().stream()
                .filter(user -> user instanceof Admin)
                .map(user -> (Admin) user)
                .collect(Collectors.toList());
    }

    // kullanıcı güncelleme -- burayı ekle
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            // kullanıcı adı değişikliği varsa ve yeni kullanıcı adı başka bir kullanıcı tarafından kullanılıyorsa
            User existingUser = findByUsername(user.getUsername());
            if (existingUser != null && existingUser.getId() != user.getId()) {
                return null; // güncelleme başarısız
            }
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    // kullanıcı silme
    public boolean delete(int id) {
        if (users.containsKey(id)) {
            users.remove(id);
            return true;
        }
        return false;
    }

    // giriş doğrulama
    public User authenticate(String username, String password) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username) &&
                        user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}
