package sit.cashier;

import domain.Order;
import domain.OrderDetail;
import repository.member.MemMemberRepo;
import repository.order.DbmsOrderRepo;
import repository.order.FileOrderRepo;
import repository.order.MemOrderRepo;
import services.CashierService;

public class Cashier {
    public static void main(String[] args) {
        MemMemberRepo memberMem = new MemMemberRepo();
        DbmsOrderRepo orderDb = new DbmsOrderRepo();
        CashierService cashierService = new CashierService(memberMem, orderDb);
            OrderDetail od1 = new OrderDetail("noodle",35,1);
            OrderDetail od2 = new OrderDetail("noodle2",45,3);
            OrderDetail od3 = new OrderDetail("noodle3",55,6);
            Order o1 = new Order();
            Order o2 = new Order();
            o1.addOrderDetail(od1);
            o1.addOrderDetail(od2);
            o1.addOrderDetail(od3);
            o2.addOrderDetail(od2);
            o2.addOrderDetail(od3);
//            cashierService.saveOrder(o1);
//            cashierService.saveOrder(o2);
            cashierService.showAllOrderFromOldToNew();
    }

}
