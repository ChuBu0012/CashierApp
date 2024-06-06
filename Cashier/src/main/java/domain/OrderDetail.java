package domain;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private String name;
    private int price;
    private int quantity;

    public OrderDetail(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int totalPrice(){
        return this.price * this.quantity;
    }

    @Override
    public String toString() {
        return "name='" + name +
                ", price=" + price +
                ", quantity=" + quantity;
    }
}
