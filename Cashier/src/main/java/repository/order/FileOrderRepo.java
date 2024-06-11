package repository.order;

import domain.Order;
import domain.OrderDetail;
import services.OrderRepository;

import java.io.*;
import java.util.*;

public class FileOrderRepo implements OrderRepository {

    private static final String PATH_FILE = "/orders.txt";
    private List<Order> orders = new ArrayList<>();

    public FileOrderRepo() {
        readFromFile();
    }

    private void readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(getClass().getResourceAsStream(PATH_FILE))) {
            orders = (List<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    private void writeToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH_FILE))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public boolean addOrder(Order order) {
        order.setId(generateNewId());
        boolean success = orders.add(order);
        writeToFile();
        return success;
    }

    @Override
    public boolean removeOrder(int id) {
        boolean removed = orders.removeIf(order -> order.getId() == id);
        if (removed) {
            writeToFile();
        }
        return removed;
    }

    @Override
    public List<Order> getAllOrderFromOldToNew() {
        return new ArrayList<>(orders);
    }

    @Override
    public List<Order> getAllOrderFromNewToOld() {
        List<Order> reversed = new ArrayList<>(orders);
        Collections.reverse(reversed);
        return reversed;
    }

    @Override
    public Order findOrderById(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<OrderDetail> findOrderDetailById(int id) {
        Order order = findOrderById(id);
        return order != null ? order.getOrders() : null;
    }

    private int generateNewId() {
        return orders.stream()
                .mapToInt(Order::getId)
                .max()
                .orElse(0) + 1;
    }
}
