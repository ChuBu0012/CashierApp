/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository.cashier;

import domain.Cashier;
import domain.Order;
import services.CashierRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileCashierRepo implements CashierRepository {

    private static final String PATH_FILE = "cashiers.txt";

    @Override
    public int calcOrder(Order order, int discount) {
        return order.getRawPrice() - discount;
    }

    @Override
    public Cashier addCashier(Cashier cashier) {
        List<Cashier> existingCashiers = findAllCashiers();

        // Check for duplicate idCard
        if (existingCashiers.stream().anyMatch(c -> c.getIdCard().equals(cashier.getIdCard()))) {
            System.out.println("Already have this ID card number...");
            return null;
        }

        cashier.setCashier_id(generateNewId(existingCashiers));
        existingCashiers.add(cashier);
        writeToFile(existingCashiers);
        return cashier;
    }

    @Override
    public Cashier removeCashier(int cashier_id) {
        List<Cashier> cashiers = findAllCashiers();
        Cashier cashierToRemove = cashiers.stream()
                .filter(c -> c.getCashier_id() == cashier_id)
                .findFirst()
                .orElse(null);

        if (cashierToRemove != null) {
            cashiers.remove(cashierToRemove);
            writeToFile(cashiers);
            return cashierToRemove;
        } else {
            System.out.println("Cashier not found.");
            return null;
        }
    }

    @Override
    public Cashier updateCashier(int cashier_id, Cashier updatedCashier) {
        List<Cashier> cashiers = findAllCashiers();
        for (int i = 0; i < cashiers.size(); i++) {
            if (cashiers.get(i).getCashier_id() == cashier_id) {
                cashiers.set(i, updatedCashier);
                writeToFile(cashiers);
                return updatedCashier;
            }
        }
        System.out.println("Cashier not found.");
        return null;
    }

    @Override
    public List<Cashier> listAllCashier() {
        return findAllCashiers();
    }

    @Override
    public Cashier getCashierById(int cashier_id) {
        return findAllCashiers().stream()
                .filter(c -> c.getCashier_id() == cashier_id)
                .findFirst()
                .orElse(null);
    }

    private List<Cashier> findAllCashiers() {
        List<Cashier> cashiers = new LinkedList<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(PATH_FILE));
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            while (true) {
                try {
                    cashiers.add((Cashier) ois.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File Not Found : " + e.getMessage());
        }
        return cashiers;
    }

    private int generateNewId(List<Cashier> cashiers) {
        return cashiers.isEmpty() ? 1 : cashiers.stream()
                .mapToInt(Cashier::getCashier_id)
                .max()
                .orElse(0) + 1;
    }

    private void writeToFile(List<Cashier> cashiers) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(PATH_FILE));
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            for (Cashier cashier : cashiers) {
                oos.writeObject(cashier);
            }
        } catch (IOException e) {
            System.out.println("Error saving cashiers: " + e.getMessage());
        }
    }
}