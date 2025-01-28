package repository;

import model.transaction.Bill;
import model.transaction.BillType;
import model.transaction.Loan;
import model.user.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BillRepository {
    private Map<Integer, Bill> bills;
    private int lastId;

    public BillRepository() {
        this.bills = new HashMap<>();
        this.lastId = 0;
    }

    public Bill save(Loan loan, double amount, BillType billType) {
        Bill bill = new Bill(++lastId, loan, amount, billType);
        bills.put(lastId, bill);
        return bill;
    }

    //idye göre bulma
    public Bill findById(int id) {
        return bills.get(id);
    }

    //tüm faturaları listeleme
    public List<Bill> findAll() {
        return bills.values().stream().toList();
    }

    //okuyucunun faturaları
    public List<Bill> findByReader(Reader reader) {
        return bills.values().stream()
                .filter(bill -> bill.getLoan().getReader().getId() == reader.getId())
                .collect(Collectors.toList());
    }

    //ödenmemiş faturalar
    public List<Bill> findUnpaidBills() {
        return bills.values().stream()
                .filter(bill -> !bill.isPaid())
                .collect(Collectors.toList());
    }

    //okuyucuya göre ödenmemiş faturalar
    public List<Bill> findUnpaidBillsByReader(Reader reader) {
        return bills.values().stream()
                .filter(bill -> bill.getLoan().getReader().getId() == reader.getId()
                        && !bill.isPaid())
                .collect(Collectors.toList());
    }
}