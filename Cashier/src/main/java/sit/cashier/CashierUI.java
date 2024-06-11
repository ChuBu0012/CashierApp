package sit.cashier;

import java.util.Scanner;
import repository.cashier.*;
import repository.member.*;
import repository.order.*;
import services.CashierService;
import domain.Cashier;
import domain.Member;
import domain.Order;
import domain.OrderDetail;
import domain.Role;
import java.io.Console;
import java.sql.SQLException;
import java.util.InputMismatchException;

public class CashierUI {

    private static CashierService cashierService;
    private static Cashier cashier;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException {
        selectDatabase();

        boolean runningMain = true;

        while (runningMain) {
            System.out.println("1.Login");
            System.out.println("2.Exit");
            System.out.print("Enter Your Choice : ");

            try {
                int choice1 = sc.nextInt();

                if (choice1 == 1) {
                    login();
                    while (cashier != null) {
                        mainMenu();
                        System.out.print("Enter Your Choice : ");
                        int choice2 = sc.nextInt();
                        switch (choice2) {
                            case 1:
                                PaymentMenu();
                                break;
                            case 2:
                                MemberMenu();
                                break;
                            case 3:
                                if (cashier.getRole().equals(Role.MANAGER)) {
                                    CashierManagerMenu();
                                } else {
                                    CashierGeneralMenu();
                                }
                                break;
                            case 4:
                                OrderMenu();
                                break;
                            case 5:
                                System.out.println("Logout...");
                                cashier = null;
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }
                } else if (choice1 == 2) {
                    System.out.println("Exit program");
                    runningMain = false;
                    sc.close();
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }
    }

    public static void selectDatabase() throws ClassNotFoundException {
        boolean runningSelectDatabase = true;

        while (runningSelectDatabase) {

            System.out.println("select your database");
            System.out.println("1.sql");
            System.out.println("2.file");
            System.out.println("3.memory");
            System.out.print("Enter your choice : ");
            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        try {

                        System.out.print("Enter sql password : ");
                        String password = sc.next();
                        DbmsOrderRepo orderRepo1 = new DbmsOrderRepo(password);
                        DbmsMemberRepo memRepo1 = new DbmsMemberRepo(password);
                        DbmsCashierRepo cashierRepo1 = new DbmsCashierRepo(password);
                        cashierService = new CashierService(memRepo1, orderRepo1, cashierRepo1);
                        runningSelectDatabase = false;
                    } catch (SQLException | RuntimeException e) {
                        
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                    case 2:
                        FileOrderRepo orderRepo2 = new FileOrderRepo();
                        FileMemberRepo memRepo2 = new FileMemberRepo();
                        FileCashierRepo cashierRepo2 = new FileCashierRepo();
                        cashierService = new CashierService(memRepo2, orderRepo2, cashierRepo2);
                        runningSelectDatabase = false;
                        break;
                    case 3:
                        MemOrderRepo orderRepo3 = new MemOrderRepo();
                        MemMemberRepo memRepo3 = new MemMemberRepo();
                        MemCashierRepo cashierRepo3 = new MemCashierRepo();
                        cashierService = new CashierService(memRepo3, orderRepo3, cashierRepo3);
                        runningSelectDatabase = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }

    }

    public static void login() {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return;
        }

        System.out.println("Please login");
        int cashierId = 0;

        while (true) {
            try {
                String input = console.readLine("Enter Your Cashier ID: ");
                cashierId = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid Cashier ID.");
            }
        }

        char[] passwordArray = console.readPassword("Enter Your Password: ");
        String password = new String(passwordArray);

        cashier = cashierService.login(cashierId, password);

        if (cashier != null) {
            System.out.println("Login successful. Welcome, " + cashier.getName());
        } else {
            System.out.println("Login failed. Invalid ID or password.");
        }
    }

    public static void mainMenu() {
        System.out.println("1. Payment");
        System.out.println("2. Member service");
        System.out.println("3. Cashier service");
        System.out.println("4. Order service");
        System.out.println("5. Logout");
    }

    public static void PaymentMenu() {
        boolean runningPayment = true;
        Order newOrder = new Order();
        while (runningPayment) {
            menuFood();
            System.out.print("Enter Menu Number: ");
            int chooseOrder;
            int quantity;
            try {
                chooseOrder = sc.nextInt();
                if (chooseOrder == 11) {
                    runningPayment = false;
                    continue;
                }
                System.out.print("Enter Quantity: ");
                quantity = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            switch (chooseOrder) {
                case 1:
                    newOrder.addOrderDetail(new OrderDetail("Spaghetti homemade sausage & garlic", 155, quantity));
                    break;
                case 2:
                    newOrder.addOrderDetail(new OrderDetail("Gabriello ravioli", 160, quantity));
                    break;
                case 3:
                    newOrder.addOrderDetail(new OrderDetail("Lasagna", 130, quantity));
                    break;
                case 4:
                    newOrder.addOrderDetail(new OrderDetail("Pizza Hawaiian", 210, quantity));
                    break;
                case 5:
                    newOrder.addOrderDetail(new OrderDetail("Pumpkin cream soup", 89, quantity));
                    break;
                case 6:
                    newOrder.addOrderDetail(new OrderDetail("Tuna salad", 89, quantity));
                    break;
                case 7:
                    newOrder.addOrderDetail(new OrderDetail("Macaroni & Cheese", 175, quantity));
                    break;
                case 8:
                    newOrder.addOrderDetail(new OrderDetail("Tiramisu", 219, quantity));
                    break;
                case 9:
                    newOrder.addOrderDetail(new OrderDetail("Gelato (cup)", 230, quantity));
                    break;
                case 10:
                    newOrder.addOrderDetail(new OrderDetail("Risotto", 179, quantity));
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
            System.out.print("Do you want to order more? (yes/no)");
            String continueOrder = sc.next();
            if (continueOrder.equalsIgnoreCase("no")) {
                System.out.print("Do you have membership? (yes/no)");
                String haveMember = sc.next();
                if (haveMember.equals("yes")) {
                    System.out.print("Enter your customer's tel no.: ");
                    String tel = sc.next();

                    Member member = cashierService.getMember(tel);
                    System.out.println("Name: " + member.getName());
                    System.out.println("Point: " + member.getPoint());
                    if (member.getPoint() > 0) {
                        System.out.print("Use Point to redeem a discount? (yes/no): ");
                        String usePoint = sc.next();

                        if (usePoint.equals("yes")) {
                            System.out.print("How many Point will be used?:");
                            int discount = sc.nextInt();

                            while (discount > member.getPoint()) {
                                System.out.println("Insufficient Points!");

                                System.out.print("How many Point will be used?:");
                                discount = sc.nextInt();
                            }

                            cashierService.checkBill(newOrder, member, discount, 0);
                            runningPayment = false;
                        } else if (usePoint.equals("no")) {
                            cashierService.checkBill(newOrder, member, 0, Math.round(newOrder.getRawPrice() / 20));
                            runningPayment = false;
                        } else {
                            System.out.println("Invalid answer. Please try again.");
                        }
                    }

                } else if (haveMember.equals("no")) {
                    cashierService.checkBill(newOrder, null, 0, 0);
                    runningPayment = false;
                } else {
                    System.out.println("Invalid answer. Please try again.");

                }
            } else if (!continueOrder.equalsIgnoreCase("no") || !continueOrder.equalsIgnoreCase("yes")) {
                System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    public static void MemberMenu() {
        boolean runningMemberMenu = true;
        while (runningMemberMenu) {
            System.out.println("Now you are in \"Member service\" !");
            System.out.println("Please choose :");
            System.out.println("1. Register");
            System.out.println("2. Cancel membership");
            System.out.println("3. Member's information");
            System.out.println("4. Update information");
            System.out.println("5. Show all member");
            System.out.println("6. Go back");
            System.out.print("Enter Your Choice : ");

            int choice;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.next();
                    System.out.print("Enter Tel: ");
                    String tel = sc.next();
                    System.out.print("Enter idCard: ");
                    String idCard = sc.next();
                    Member member = cashierService.registerMember(name, tel, idCard);
                    if (member != null) {
                        System.out.println("Member registered successfully.");
                    } else {
                        System.out.println("Failed to register member. Please try again.");
                    }
                    break;
                case 2:
                    System.out.print("Enter Member idCard: ");
                    String removeIdCard = sc.next();
                    if (!Check()) {
                        break;
                    }
                    Member removeMember = cashierService.cancelMember(removeIdCard);
                    if (removeMember != null) {
                        System.out.println("Member : " + removeMember.getName() + "remove successfully");
                    } else {
                        System.out.println("Failed to remove member. Please try again.");
                    }
                    break;
                case 3:
                    System.out.print("Enter Member Tel: ");
                    String searchTel = sc.next();
                    Member searchMember = cashierService.getMember(searchTel);
                    if (searchMember == null) {
                        System.out.println("Failed to Search member. Please try again.");
                    } else {
                        System.out.println("Name : " + searchMember.getName());
                        System.out.println("Tel : " + searchMember.getTel());
                        System.out.println("IdCard : " + searchMember.getIdCard());
                        System.out.println("Point : " + searchMember.getPoint());
                    }
                    break;
                case 4:
                    System.out.print("Enter Member Tel: ");
                    String updateTel = sc.next();
                    Member updateMember = cashierService.getMember(updateTel);

                    if (updateMember == null) {
                        System.out.println("Failed to Search member. Please try again.");
                        break;
                    }

                    System.out.print("Enter New Name: ");
                    String newName = sc.next();
                    System.out.print("Enter New Tel: ");
                    String newTel = sc.next();

                    if (newName.isBlank() || newName == null || newTel.isBlank() || newTel == null) {
                        System.out.println("Invalid Tel. Please try again.");
                        break;
                    }
                    updateMember.setName(newName);
                    updateMember.setTel(newTel);
                    updateMember = cashierService.updateInfoMember(updateMember);

                    if (updateMember != null) {
                        System.out.println("Update successfully");
                    } else {
                        System.out.println("Failed to Update cashier information. Please try again.");
                    }
                    break;
                case 5:
                    cashierService.showAllMember();
                    break;
                case 6:
                    runningMemberMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void CashierGeneralMenu() {
        boolean runningCashierGeneralMenu = true;
        while (runningCashierGeneralMenu) {
            System.out.println("Now you are in \"Cashier service\" !");
            System.out.println("Please choose :");
            System.out.println("1. Search cashier");
            System.out.println("2. Show all cashier");
            System.out.println("3. Go back");
            System.out.print("Enter Your Choice : ");

            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Cashier ID: ");
                        int searchId = sc.nextInt();
                        Cashier searchCashier = cashierService.searchCashier(searchId);
                        if (searchCashier == null) {
                            System.out.println("Failed to Search cashier. Please try again.");
                        } else {
                            System.out.println("Id : " + searchCashier.getCashier_id());
                            System.out.println("Name : " + searchCashier.getName());
                            System.out.println("PassWord : " + searchCashier.getPassword());
                            System.out.println("IdCard : " + searchCashier.getIdCard());
                            System.out.println("Role : " + searchCashier.getRole());
                            System.out.println("Tel : " + searchCashier.getTel());
                        }
                        break;
                    case 2:
                        cashierService.showCashier();
                        break;
                    case 3:
                        runningCashierGeneralMenu = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
    }

    public static void CashierManagerMenu() {
        boolean runningCashierManagerMenu = true;

        while (runningCashierManagerMenu) {
            System.out.println("Now you are in \"Cashier service\" !");
            System.out.println("Please choose :");
            System.out.println("1. Register");
            System.out.println("2. Search cashier");
            System.out.println("3. Show all cashier");
            System.out.println("4. Update cashier");
            System.out.println("5. Remove cashier");
            System.out.println("6. Go back");
            System.out.print("Enter Your Choice : ");

            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Cashier ID: ");
                        int id = sc.nextInt();
                        System.out.print("Enter Name: ");
                        String name = sc.next();
                        System.out.print("Enter Password: ");
                        String password = sc.next();
                        System.out.print("Enter ID Card: ");
                        String idCard = sc.next();
                        System.out.print("Enter Role (MANAGER, GENERAL): ");
                        Role role = Role.valueOf(sc.next());
                        System.out.print("Enter Telephone: ");
                        String tel = sc.next();

                        Cashier cashier = cashierService.registerCashier(id, name, password, idCard, role, tel);
                        if (cashier != null) {
                            System.out.println("Cashier registered successfully.");
                        } else {
                            System.out.println("Failed to register cashier. Please try again.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter Cashier ID: ");
                        int searchId = sc.nextInt();
                        Cashier searchCashier = cashierService.searchCashier(searchId);
                        if (searchCashier == null) {
                            System.out.println("Failed to Search cashier. Please try again.");
                        } else {
                            System.out.println("Id : " + searchCashier.getCashier_id());
                            System.out.println("Name : " + searchCashier.getName());
                            System.out.println("PassWord : " + searchCashier.getPassword());
                            System.out.println("IdCard : " + searchCashier.getIdCard());
                            System.out.println("Role : " + searchCashier.getRole());
                            System.out.println("Tel : " + searchCashier.getTel());
                        }
                        break;
                    case 3:
                        cashierService.showCashier();
                        break;
                    case 4:
                        System.out.print("Enter Cashier ID: ");
                        int newId = sc.nextInt();
                        System.out.print("Enter New Name: ");
                        String newName = sc.next();
                        System.out.print("Enter New Password: ");
                        String newPassword = sc.next();
                        System.out.print("Enter New ID Card: ");
                        String newIdCard = sc.next();
                        System.out.print("Enter New Role (MANAGER, GENERAL): ");
                        Role newRole = Role.valueOf(sc.next());
                        System.out.print("Enter New Telephone: ");
                        String newTel = sc.next();
                        Cashier newInfo = new Cashier(newId, newName, newPassword, newIdCard, newRole, newTel);
                        Cashier newCashier = cashierService.updateInfoCashier(newInfo);
                        if (newCashier != null) {
                            System.out.println("Update successfully");
                        } else {
                            System.out.println("Failed to Update cashier infomation. Please try again.");
                        }
                        break;
                    case 5:
                        System.out.print("Enter Cashier ID: ");
                        int removeId = sc.nextInt();
                        if (!Check()) {
                            break;
                        }
                        Cashier removeCashier = cashierService.cancleCashier(removeId);
                        if (removeCashier != null) {
                            System.out.println("Cashier : " + removeCashier.getName() + " remove successfully");
                        } else {
                            System.out.println("Failed to remove cashier. Please try again.");
                        }
                        break;
                    case 6:
                        runningCashierManagerMenu = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
    }

    public static void OrderMenu() {
        boolean runningOrderMenu = true;

        while (runningOrderMenu) {
            System.out.println("Now you are in \"Order service\" !");
            System.out.println("Please choose :");
            System.out.println("1. Cancel order");
            System.out.println("2. Search order");
            System.out.println("3. Show all order");
            System.out.println("4. Go back");
            System.out.print("Enter Your Choice : ");

            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Order ID: ");
                        int removeId = sc.nextInt();
                        if (!Check()) {
                            break;
                        }
                        if (cashierService.cancleOrder(removeId)) {
                            System.out.println("remove successfully");
                        } else {
                            System.out.println("Failed to remove order. Please try again.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter Order ID: ");
                        int searchId = sc.nextInt();
                        Order searchOrder = cashierService.searchOrder(searchId);
                        if (searchOrder == null) {
                            System.out.println("This order is not in the system");
                        } else {
                            System.out.print("Do you want to view order detail of this order?(yes/no)");
                            String ans = sc.next();
                            if (ans.equals("yes")) {
                                System.out.println("Id : " + searchOrder.getId());
                                System.out.printf("%-40s%-20s%-10s\n", "Name", "Price (Bath)", "Quantity");

                                searchOrder.getOrders().forEach(ele -> {
                                    System.out.printf("%-40s%-20d%-10d\n", ele.getName(), ele.getPrice(), ele.getQuantity());
                                });

                                System.out.println("Discount : " + searchOrder.getDiscount());
                                System.out.println("RawPrice : " + searchOrder.getRawPrice());

                            } else if (ans.equals("no")) {
                                System.out.println("Id : " + searchOrder.getId());
                                System.out.println("Discount : " + searchOrder.getDiscount());
                                System.out.println("RawPrice : " + searchOrder.getRawPrice());
                            } else {
                                System.out.println("Invalid choice. Please try again.");
                            }
                        }
                        break;
                    case 3:
                        System.out.println("");
                        System.out.println("1. Show orders from New to Old");
                        System.out.println("2. Show orders from Old to New");
                        System.out.print("Enter your choice:");
                        int orderChoice = sc.nextInt();
                        if (orderChoice == 1) {
                            cashierService.showAllOrderFromNewToOld();
                        } else if (orderChoice == 2) {
                            cashierService.showAllOrderFromOldToNew();
                        } else {
                            System.out.println("Invalid choice.");
                        }
                        break;
                    case 4:
                        runningOrderMenu = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
    }

    public static boolean Check() {
        boolean runningCheck = true;
        boolean going = true;

        while (runningCheck) {
            System.out.println("Are you sure?");
            System.out.println("\"yes or no\"");
            String check = sc.next();

            if (check.equals("yes")) {
                going = true;
                break;
            } else if (check.equals("no")) {
                going = false;
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        return going;
    }

    public static void menuFood() {
        System.out.println("Please choose menu :");
        System.out.println("1. Spaghetti homemade sausage & garlic\t155 Bath");
        System.out.println("2. Gabriello ravioli\t\t\t160 Bath");
        System.out.println("3. Lasagna\t\t\t\t130 Bath");
        System.out.println("4. Pizza Hawaiian\t\t\t210 Bath");
        System.out.println("5. Pumpkin cream soup\t\t\t89 Bath");
        System.out.println("6. Tuna salad\t\t\t\t89 Bath");
        System.out.println("7. Macaroni & Cheese\t\t\t175 Bath");
        System.out.println("8. Tiramisu\t\t\t\t219 Bath");
        System.out.println("9. Gelato (cup)\t\t\t\t230 Bath");
        System.out.println("10. Risotto\t\t\t\t179 Bath");
        System.out.println("11. Go Back");

    }

}
