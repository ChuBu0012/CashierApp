package services;

import domain.Order;
import domain.OrderDetail;

import java.util.List;

public interface OrderRepository {
    boolean addOrder(Order order);
    boolean removeOrder(int id);
    List<Order> getAllOrderFromOldToNew();
    List<Order> getAllOrderFromNewToOld();
    Order findOrderById(int id);
    List<OrderDetail> findOrderDetailById(int id);
}
