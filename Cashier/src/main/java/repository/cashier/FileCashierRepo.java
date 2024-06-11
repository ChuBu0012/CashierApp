package repository.cashier;

import domain.Cashier;
import domain.Order;
import services.CashierRepository;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FileCashierRepo implements CashierRepository {

    private static final String PATH_FILE = "/cashiers.txt";
    private List<Cashier> cashiers;

    public FileCashierRepo() {
        this.cashiers = findAllCashiers();
    }

    @Override
    public int calcOrder(Order order, int discount) {
        return order.getRawPrice() - discount;
    }

    @Override
    public Cashier addCashier(Cashier cashier) {
        if (cashiers.stream().anyMatch(c -> c.getIdCard().equals(cashier.getIdCard()))) {
            System.out.println("Already have this ID card number...");
            return null;
        }

        cashiers.add(cashier);
        writeToFile();
        return cashier;
    }

    @Override
    public Cashier removeCashier(int cashier_id) {
        Cashier cashierToRemove = cashiers.stream()
                .filter(c -> c.getCashier_id() == cashier_id)
                .findFirst()
                .orElse(null);

        if (cashierToRemove != null) {
            cashiers.remove(cashierToRemove);
            writeToFile();
            return cashierToRemove;
        } else {
            System.out.println("Cashier not found.");
            return null;
        }
    }

    @Override
    public Cashier updateCashier(int cashier_id, Cashier updatedCashier) {
        for (int i = 0; i < cashiers.size(); i++) {
            if (cashiers.get(i).getCashier_id() == cashier_id) {
                cashiers.set(i, updatedCashier);
                writeToFile();
                return updatedCashier;
            }
        }
        System.out.println("Cashier not found.");
        return null;
    }

    @Override
    public List<Cashier> listAllCashier() {
        return cashiers.stream().collect(Collectors.toList());
    }

    @Override
    public Cashier getCashierById(int cashier_id) {
        return cashiers.stream()
                .filter(c -> c.getCashier_id() == cashier_id)
                .findFirst()
                .orElse(null);
    }

    private List<Cashier> findAllCashiers() {
        List<Cashier> findCashiers = new LinkedList<>();
        try (InputStream inputStream = getClass().getResourceAsStream(PATH_FILE);
             ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            while (true) {
                try {
                    findCashiers.add((Cashier) ois.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File Not Found : " + e.getMessage());
        }
        return findCashiers;
    }

    private void writeToFile() {
        try (OutputStream outputStream = new FileOutputStream(PATH_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
            for (Cashier cashier : cashiers) {
                oos.writeObject(cashier);
            }
        } catch (IOException e) {
            System.out.println("Error saving cashiers: " + e.getMessage());
        }
    }
}
