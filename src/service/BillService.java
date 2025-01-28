package service;

import model.transaction.Bill;
import model.transaction.BillType;
import model.transaction.Loan;
import model.user.Admin;
import model.user.Reader;
import model.user.User;
import repository.BillRepository;

import java.util.List;

public class BillService {
    private final BillRepository billRepository;
    private final UserService userService;
    private static final double LATE_FEE_PER_DAY = 1.0; // günlük gecikme ücreti
    private static final double BORROW_FEE = 5.0; //ödünç alma ücreti

    public BillService(BillRepository billRepository, UserService userService) {
        this.billRepository = billRepository;
        this.userService = userService;
    }

    //ödünç alma fee oluşturma
    public Bill createBorrowFee(Loan loan) {
        return billRepository.save(loan, BORROW_FEE, BillType.BORROW_FEE);
    }

    //gecikme fee oluşturma
    public Bill createLateFee(Loan loan) {
        checkAdminAccess();
        long overdueDays = loan.getOverdueDays();
        double amount = overdueDays * LATE_FEE_PER_DAY;
        return billRepository.save(loan, amount, BillType.LATE_FEE);
    }

    //fatura ödeme
    public void payBill(int billId) {
        Bill bill = billRepository.findById(billId);
        if (bill == null) {
            throw new IllegalArgumentException("Fatura bulunamadı");
        }

        checkReaderAccess(bill.getLoan().getReader().getId());

        if (bill.isPaid()) {
            throw new IllegalStateException("Bu fatura zaten ödenmiş");
        }

        bill.pay();
    }

    //tüm faturaları görüntüleme
    public List<Bill> getAllBills() {
        checkAdminAccess();
        return billRepository.findAll();
    }


    //ödenmemiş faturaları görüntüleme
    public List<Bill> getUnpaidBills() {
        checkAdminAccess();
        return billRepository.findUnpaidBills();
    }

    //kullanıcıya ait faturaları listeleme
    public List<Bill> getReaderBills(int readerId) {
        checkReaderAccess(readerId);
        Reader reader = (Reader) userService.getUserById(readerId);
        return billRepository.findByReader(reader);
    }


    //kullanıcıya ait ödenmemiş faturaları listeleme
    public List<Bill> getReaderUnpaidBills(int readerId) {
        checkReaderAccess(readerId);
        Reader reader = (Reader) userService.getUserById(readerId);
        return billRepository.findUnpaidBillsByReader(reader);
    }


    //access checkleme metodları
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
            throw new SecurityException("Sadece kendi faturalarınızı görüntüleyebilirsiniz");
        }
    }
}
