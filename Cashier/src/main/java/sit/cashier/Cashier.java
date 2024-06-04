package sit.cashier;


import java.io.Console;

public class Cashier {
    static final String USERNAME = "chubu";
    static final String PASSWORD = "0012";

    public static void main(String[] args) {
        boolean checkLogin = login();
        while (!checkLogin) {
            checkLogin = login();
        }
    }

    static boolean login() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Console not available. Exiting...");
            System.exit(1); // Exit if console is not available
        }

        String userName = console.readLine("Enter username: ");
        char[] passwordChars = console.readPassword("Enter password: ");
        String password = new String(passwordChars);

        if (password.equals(PASSWORD) && userName.equals(USERNAME)) {
            System.out.println("Login successful!...");
            return true;
        }
        System.out.println("Login failed!...");
        return false;
    }
}
