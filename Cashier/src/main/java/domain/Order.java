package domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Order implements Serializable {
    private int id;
    private List<OrderDetail> orders;
    private int discount;
    private int rawPrice;

    public Order(){
        orders = new LinkedList<OrderDetail>();
    }

    public boolean addOrderDetail(OrderDetail order){
        return orders.add(order);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderDetail> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetail> orders) {
        this.orders = orders;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getRawPrice() {
        return rawPrice;
    }

    public void setRawPrice(int rawPrice) {
        this.rawPrice = rawPrice;
    }

}
