package repository.order;

import domain.Order;
import domain.OrderDetail;
import services.OrderRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MemOrderRepo implements OrderRepository {
    private static int nextId;
    private final List<Order> orders;

    public MemOrderRepo() {
        nextId = 0;
        orders = new LinkedList<>();
    }

    @Override
    public boolean addOrder(Order order) {
        order.setId(++nextId);
        return orders.add(order);
    }

    @Override
    public boolean removeOrder(int id) {
        return orders.remove(id) == null ? false : true;
    }

    @Override
    public List<Order> getAllOrderFromOldToNew() {
        return orders;
    }

    @Override
    public List<Order> getAllOrderFromNewToOld() {
        // สร้างสำเนาของลิสต์เพื่อไม่ให้แก้ไขลิสต์ต้นฉบับ
        LinkedList<Order> reversedOrders = new LinkedList<>(orders);
        // เรียงลำดับสำเนาของลิสต์ใหม่ให้กลับด้าน
        Collections.reverse(reversedOrders);
        // คืนค่าลิสต์ที่เรียงลำดับใหม่
        return reversedOrders;
    }

    @Override
    public Order findOrderById(int id) {
        return orders.get(id - 1);
    }

    @Override
    public List<OrderDetail> findOrderDetailById(int id) {
        return orders.get(id - 1).getOrders();
    }
}
