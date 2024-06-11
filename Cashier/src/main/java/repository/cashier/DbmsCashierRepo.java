package repository.cashier;

import domain.Cashier;
import domain.Order;
import domain.Role;
import services.CashierRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbmsCashierRepo implements CashierRepository {

    private final String url = "jdbc:mysql://127.0.0.1:3306/cashier";
    private final String user = "root";
    private final String password;
    private Connection connection;

    public DbmsCashierRepo(String password) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        this.password = password;

        String sql = "CREATE TABLE IF NOT EXISTS cashiers ("
                + "cashier_id INT PRIMARY KEY AUTO_INCREMENT,"
                + "name VARCHAR(255) NOT NULL,"
                + "password VARCHAR(255) NOT NULL,"
                + "id_card VARCHAR(255) NOT NULL UNIQUE,"
                + "role VARCHAR(255) NOT NULL,"
                + "tel VARCHAR(255) NOT NULL UNIQUE"
                + ")";

        try {
            connection = DriverManager.getConnection(url, user, this.password);
            try (PreparedStatement statement = connection.prepareStatement(sql);) {
                statement.executeUpdate();
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
    public int calcOrder(Order order, int discount) {
        return order.getRawPrice() - discount;
    }

    @Override
    public Cashier addCashier(Cashier cashier) {
        String sql = "INSERT INTO cashiers (name, password, id_card, role, tel) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cashier.getName());
            statement.setString(2, cashier.getPassword());
            statement.setString(3, cashier.getIdCard());
            statement.setString(4, cashier.getRole().toString());
            statement.setString(5, cashier.getTel());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to add cashier: " + e.getMessage());
            return null;
        }
        return cashier;
    }

    @Override
    public Cashier removeCashier(int cashier_id) {
        Cashier deletedCashier = null;
        String sqlSelect = "SELECT * FROM cashiers WHERE cashier_id = ?";
        String sqlDelete = "DELETE FROM cashiers WHERE cashier_id = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect); PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete)) {
            selectStatement.setInt(1, cashier_id);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    deletedCashier = new Cashier(
                            resultSet.getInt("cashier_id"),
                            resultSet.getString("name"),
                            resultSet.getString("password"),
                            resultSet.getString("id_card"),
                            Role.valueOf(resultSet.getString("role")),
                            resultSet.getString("tel")
                    );
                }
            }
            deleteStatement.setInt(1, cashier_id);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to remove cashier: " + e.getMessage());
        }
        return deletedCashier;
    }

    @Override
    public Cashier updateCashier(int cashier_id, Cashier updatedCashier) {
        String sql = "UPDATE cashiers SET name = ?, password = ?, id_card = ?, role = ?, tel = ? WHERE cashier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedCashier.getName());
            statement.setString(2, updatedCashier.getPassword());
            statement.setString(3, updatedCashier.getIdCard());
            statement.setString(4, updatedCashier.getRole().toString());
            statement.setString(5, updatedCashier.getTel());
            statement.setInt(6, cashier_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to update cashier: " + e.getMessage());
            return null;
        }
        return updatedCashier;
    }

    @Override
    public List<Cashier> listAllCashier() {
        List<Cashier> cashiers = new ArrayList<>();
        String sql = "SELECT * FROM cashiers";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                cashiers.add(new Cashier(
                        resultSet.getInt("cashier_id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        resultSet.getString("id_card"),
                        Role.valueOf(resultSet.getString("role")),
                        resultSet.getString("tel")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch cashiers: " + e.getMessage());
        }
        return cashiers;
    }

    @Override
    public Cashier getCashierById(int cashier_id) {
        String sql = "SELECT * FROM cashiers WHERE cashier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cashier_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Cashier(
                            resultSet.getInt("cashier_id"),
                            resultSet.getString("name"),
                            resultSet.getString("password"),
                            resultSet.getString("id_card"),
                            Role.valueOf(resultSet.getString("role")),
                            resultSet.getString("tel")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to find cashier by id: " + e.getMessage());
        }
        return null;
    }
}
