package services;

import domain.Member;
import domain.Order;
import domain.OrderDetail;

import java.util.List;

public class CashierService {
    private final MemberRepository member;
    private final OrderRepository order;

    public CashierService(MemberRepository memberRepo, OrderRepository orderRepo) {
        member = memberRepo;
        order = orderRepo;
    }

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

    public Member updateInfo(Member updatedMember) {
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

    public boolean saveOrder(Order orders) {
        return this.order.addOrder(orders);
    }

    public boolean cancleOrder(int id){
        if(id <= 0){
            return false;
        }
        return this.order.removeOrder(id);
    }
    public Order searchOrder(int id){
        return this.order.findOrderById(id);
    }
    public List<OrderDetail> searchOrderDetail(int id){
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
