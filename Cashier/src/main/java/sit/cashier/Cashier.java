package sit.cashier;

import domain.Member;
import domain.Order;
import domain.OrderDetail;
import domain.Role;
import repository.cashier.DbmsCashierRepo;
import repository.cashier.FileCashierRepo;
import repository.member.DbmsMemberRepo;
import repository.member.FileMemberRepo;
import repository.order.DbmsOrderRepo;
import repository.order.FileOrderRepo;
import services.CashierService;

public class Cashier {
    public static void main(String[] args) {
        DbmsOrderRepo order = new DbmsOrderRepo();
        DbmsMemberRepo mem = new DbmsMemberRepo();
        DbmsCashierRepo cashier = new DbmsCashierRepo();

        CashierService cashierService = new CashierService(mem, order, cashier);
        Order order1 = new Order();
        order1.addOrderDetail(new OrderDetail("pizza", 69, 1));
        order1.addOrderDetail(new OrderDetail("burger", 49, 2));
        order1.addOrderDetail(new OrderDetail("fries", 29, 1));
        order1.addOrderDetail(new OrderDetail("salad", 35, 1));
        order1.addOrderDetail(new OrderDetail("soda", 15, 3));
        order1.addOrderDetail(new OrderDetail("ice cream", 25, 2));
        cashierService.checkBill(order1, new Member("most", "095", "123"), 0, 0);
        cashierService.registerCashier(10012, "Most", "1234", "1102400123456", Role.MANAGER, "0924445555");
        cashierService.showCashier();
    }

}
