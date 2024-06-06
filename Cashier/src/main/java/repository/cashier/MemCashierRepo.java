package repository.cashier;

import domain.Cashier;
import domain.Order;
import services.CashierRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemCashierRepo implements CashierRepository {
    private static int nextId;
    private final Map<Integer, Cashier> repo;

    public MemCashierRepo() {
        nextId = 0;
        this.repo = new HashMap<>();
    }

    @Override
    public int calcOrder(Order order, int discount) {
        return order.getRawPrice() - discount;
    }

    @Override
    public Cashier addCashier(Cashier cashier) {
        repo.put(++nextId, cashier);
        return cashier;
    }

    @Override
    public Cashier removeCashier(int cashier_id) {
        return repo.remove(cashier_id);
    }

    @Override
    public Cashier updateCashier(int cashier_id, Cashier updatedCashier) {
        repo.put(cashier_id, updatedCashier);
        return updatedCashier;
    }

    @Override
    public List<Cashier> listAllCashier() {
        return repo.values().stream().collect(Collectors.toList());
    }

    @Override
    public Cashier getCashierById(int cashier_id) {
        return repo.get(cashier_id);
    }
}