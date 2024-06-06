package services;

import domain.*;

import java.util.List;

public class CashierService {
    private final MemberRepository member;
    private final OrderRepository order;
    private final CashierRepository cashier;

    public CashierService(MemberRepository memberRepo, OrderRepository orderRepo, CashierRepository cashierRepo) {
        member = memberRepo;
        order = orderRepo;
        cashier = cashierRepo;
    }

    // Cashier service
    public Cashier login(int cashier_id, String password) {
        int userMemId = 1234;
        String userMemPassword = "1234";
        if (cashier_id == userMemId && password.equals(userMemPassword)) {
            return this.cashier.addCashier(new Cashier(userMemId, "kiki", userMemPassword, "12345", Role.MANAGER, "0952222222"));
        }
        ;
        Cashier auth = cashier.getCashierById(cashier_id);
        if (cashier != null && auth.getPassword().equals(password)) {
            return auth;
        }
        return null;
    }

    public void checkBill(Order order, Member member, int discount, int collect) {
        if (member != null) {
            System.out.println("Member: " + member.getName());
        }
        System.out.println("Name\t\tQuantity\tPrice");
        order.getOrders().stream().forEach(ele -> {
            System.out.printf("%-12s\t%-7d\t%d\n", ele.getName(), ele.getQuantity(), ele.getPrice());
        });
        System.out.println("Price: " + (cashier.calcOrder(order, 0) - cashier.calcOrder(order, 0) * 7 / 100));
        System.out.println("VAT 7%: " + cashier.calcOrder(order, 0) * 7 / 100);
        if (discount > 0) {
            if (member.getPoint() >= discount) {
                order.setDiscount(discount);
                System.out.println("Discount: " + discount);
            }
        }
        System.out.println("Total Price: " + cashier.calcOrder(order, discount));
        if (collect > 0) {
            this.member.increasePoint(member.getTel(), collect);
            System.out.println("collect Point: " + "+" + collect);
        }
        this.order.addOrder(order);

    }

    public Cashier registerCashier(int cashier_id, String name, String password, String idCard, Role role, String tel) {
        if (name == null || name.isBlank() ||
                password == null || password.isBlank() ||
                idCard == null || idCard.isBlank() ||
                role == null ||
                tel == null || tel.isBlank()) {
            return null;
        }

        Cashier cashier = new Cashier(cashier_id, name, password, idCard, role, tel);
        return this.cashier.addCashier(cashier);
    }

    public Cashier searchCashier(int cashier_id) {
        return this.cashier.getCashierById(cashier_id);
    }

    public void showCashier() {
        this.cashier.listAllCashier().stream().forEach(ele -> {
            System.out.println(ele);
        });
    }

    public Cashier cancleCashier(int cashier_id) {
        if (cashier_id <= 0) {
            return null;
        }

        return cashier.removeCashier(cashier_id);
    }

    public Cashier updateInfoCashier(int cashier_id, String name, String password, String idCard, Role role, String tel) {
        if (cashier_id <= 0 ||
                name == null || name.isBlank() ||
                password == null || password.isBlank() ||
                idCard == null || idCard.isBlank() ||
                role == null ||
                tel == null || tel.isBlank()) {
            return null;
        }

        Cashier updatedCashier = new Cashier(cashier_id, name, password, idCard, role, tel);
        return cashier.updateCashier(cashier_id, updatedCashier);
    }

    // Member Service
    public Member registerMember(String name, String tel, String idCard) {
        if (name == null || name.isBlank()
                || tel == null || tel.isBlank()
                || idCard == null || idCard.isBlank()) {
            return null;
        }
        return member.addMember(new Member(name, tel, idCard));
    }

    public Member cancelMember(String idCard) {
        int memId = getIdByIdCard(idCard);
        if (memId != -1) {
            return member.removeMember(memId);
        }
        return null;
    }

    public int getIdBytel(String tel) {
        return member.findIdByTel(tel);
    }

    public int getIdByIdCard(String idCard) {
        return member.findIdByIdCard(idCard);
    }


    public Member getMember(String tel) {
        if (tel == null || tel.isBlank()) {
            return null;
        }
        return member.findMember(tel);
    }

    public List<Member> getAllMembers() {
        return member.findAllMembers();
    }

    public Member updateInfoMember(Member updatedMember) {
        if (updatedMember == null) {
            return null;
        }

        return member.updateMember(updatedMember);
    }

    public int collectPoint(String tel, int price) {
        if (tel == null || tel.isBlank()) {
            return -1;
        }
        return member.increasePoint(tel, price);
    }

    public int discount(String tel, int price) {
        if (tel == null || tel.isBlank()) {
            return -1;
        }
        return member.decreasePoint(tel, price);
    }

    public void showAllMember() {
        member.getStream().forEach(ele -> System.out.println(ele));
    }

    //    OrderService
    public boolean saveOrder(Order orders) {
        return this.order.addOrder(orders);
    }

    public boolean cancleOrder(int id) {
        if (id <= 0) {
            return false;
        }
        return this.order.removeOrder(id);
    }

    public Order searchOrder(int id) {
        return this.order.findOrderById(id);
    }

    public List<OrderDetail> searchOrderDetail(int id) {
        return this.order.findOrderDetailById(id);
    }

    public void showAllOrderFromNewToOld() {
        order.getAllOrderFromNewToOld()
                .stream()
                .forEach((order) -> {
                    System.out.println("Order NO." + order.getId());
                    System.out.println("\tOrderDetail:");

                    order.getOrders().stream().forEach(od -> {
                        System.out.println("\t\tname: " + od.getName());
                        System.out.println("\t\tprice: " + od.getPrice());
                        System.out.println("\t\tquantity: " + od.getQuantity());
                        System.out.println("\t\ttotal price: " + od.totalPrice());
                        System.out.println();
                    });
                });
    }

    public void showAllOrderFromOldToNew() {
        order.getAllOrderFromOldToNew()
                .stream()
                .forEach((order) -> {
                    System.out.println("Order NO." + order.getId());
                    System.out.println("\tOrderDetail:");

                    order.getOrders().stream().forEach(od -> {
                        System.out.println("\t\tname: " + od.getName());
                        System.out.println("\t\tprice: " + od.getPrice());
                        System.out.println("\t\tquantity: " + od.getQuantity());
                        System.out.println("\t\ttotal price: " + od.totalPrice());
                        System.out.println();
                    });
                });
    }
}
