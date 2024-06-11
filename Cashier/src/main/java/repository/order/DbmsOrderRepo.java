package repository.order;

import domain.Order;
import domain.OrderDetail;
import services.OrderRepository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DbmsOrderRepo implements OrderRepository {

    private final String url = "jdbc:mysql://127.0.0.1:3306/cashier";
    private final String user = "root";
    private final String password;
    private Connection connection;

    public DbmsOrderRepo(String password) throws ClassNotFoundException, SQLException {
        this.password = password;

        String createOrder = "CREATE TABLE IF NOT EXISTS orders ("
                + "order_id INT PRIMARY KEY AUTO_INCREMENT,"
                + "discount INT,"
                + "raw_price INT NOT NULL"
                + ")";

        String createOrderDetail = "CREATE TABLE IF NOT EXISTS orders_detail ("
                + "order_detail_id INT PRIMARY KEY AUTO_INCREMENT,"
                + "order_id INT NOT NULL,"
                + "name VARCHAR(255) NOT NULL,"
                + "price INT NOT NULL,"
                + "quantity INT NOT NULL,"
                + "FOREIGN KEY (order_id) REFERENCES orders(order_id)"
                + ")";

        try {
            connection = DriverManager.getConnection(url, user, this.password);
            try (PreparedStatement statement1 = connection.prepareStatement(createOrder); PreparedStatement statement2 = connection.prepareStatement(createOrderDetail)) {
                statement1.executeUpdate();
                statement2.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Create Table Order Fail...", e);

            }
        } catch (SQLException e) {
            throw new SQLException("Failed to establish a database connection", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean addOrder(Order order) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO orders (discount, raw_price) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getDiscount());
            statement.setInt(2, order.getRawPrice());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    for (OrderDetail detail : order.getOrders()) {
                        addOrderDetail(orderId, detail);
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
        }
        return false;
    }

    private void addOrderDetail(int orderId, OrderDetail detail) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO orders_detail (order_id, name, price, quantity) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, orderId);
            statement.setString(2, detail.getName());
            statement.setInt(3, detail.getPrice());
            statement.setInt(4, detail.getQuantity());
            statement.executeUpdate();
        }
    }

    @Override
    public boolean removeOrder(int id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM orders WHERE order_id = ?")) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing order: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Order> getAllOrderFromOldToNew() {
        return getOrders("SELECT * FROM orders ORDER BY order_id ASC");
    }

    @Override
    public List<Order> getAllOrderFromNewToOld() {
        return getOrders("SELECT * FROM orders ORDER BY order_id DESC");
    }

    @Override
    public Order findOrderById(int id) {
        List<Order> orders = getOrders("SELECT * FROM orders WHERE order_id = " + id);
        return orders.isEmpty() ? null : orders.get(0);
    }

    @Override
    public List<OrderDetail> findOrderDetailById(int id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders_detail WHERE order_id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<OrderDetail> orderDetails = new LinkedList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");
                orderDetails.add(new OrderDetail(name, price, quantity));
            }
            return orderDetails;
        } catch (SQLException e) {
            System.err.println("Error getting order details: " + e.getMessage());
        }
        return null;
    }

    private List<Order> getOrders(String query) {
        List<Order> orders = new LinkedList<>();
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int discount = resultSet.getInt("discount");
                int rawPrice = resultSet.getInt("raw_price");
                Order order = new Order();
                order.setId(orderId);
                order.setDiscount(discount);
                order.setRawPrice(rawPrice);
                order.setOrders(findOrderDetailById(orderId));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting orders: " + e.getMessage());
        }
        return orders;
    }
}
