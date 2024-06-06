package services;

import domain.Cashier;
import domain.Member;
import domain.Order;

import java.util.List;

public interface CashierRepository {
    int calcOrder(Order order, int discount);
    Cashier addCashier(Cashier cashier);
    Cashier removeCashier(int cashier_id);
    Cashier updateCashier(int cashier_id, Cashier cashier);
    List<Cashier> listAllCashier();
    Cashier getCashierById(int cashier_id);


}
