package Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerController {
    private CustomerService customerService = new CustomerService();

    public void runMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            showMenu();
            String select = scanner.nextLine();

            switch (select) {
                case "1" -> listAllCustomers();
                case "2" -> getCustomerById(scanner);
                case "3" -> addNewCustomer(scanner);
                case "4" -> updateCustomerEmail(scanner);
                case "5" -> running = false;
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
            System.out.println();
        }
        System.out.println("Återgår till huvudmenyn...");
    }


    private void showMenu() {
        System.out.println("!=== Hantera Kunder ===!");
        System.out.println("1. Hämta alla kunder");
        System.out.println("2. Hämta en kund efter id");
        System.out.println("3. Lägg till en ny kund");
        System.out.println("4. Uppdatera kundens e-post");
        System.out.println("5. Tillbaka till huvudmenyn");
        System.out.print("Välj ett alternativ: ");
    }


    private void listAllCustomers() {
        try {
            ArrayList<Customer> customers = customerService.getAllCustomers();
            if (customers.isEmpty()) {
                System.out.println("Inga kunder hittades i databasen.");
            } else {
                for (Customer c : customers) {
                    printCustomer(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Fel: Kunde inte hämta kunderna. Kontrollera att databasen är tillgänglig.");
        }
    }


    private void getCustomerById(Scanner scanner) {
        System.out.print("Ange id: ");
        int id = readId(scanner);
        if (id == -1) return;
        try {
            Customer customer = customerService.getCustomerById(id);
            printCustomer(customer);
        } catch (SQLException e) {
            System.out.println(e.getMessage().contains("hittades inte")
                    ? "Fel: Kund med ID " + id + " finns inte i databasen. Kontrollera ID:t."
                    : "Fel: Kunde inte hämta kund med ID " + id + ". Försök igen senare.");
        }
    }


    private void addNewCustomer(Scanner scanner) {
        System.out.print("Ange kundens namn: ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            System.out.println("Fel: Namn kan inte vara tomt!");
            return;
        }
        System.out.print("Ange kundens e-post (tryck Enter om ingen): ");
        String email = scanner.nextLine();
        System.out.print("Ange kundens telefon (tryck Enter om ingen): ");
        String phone = scanner.nextLine();
        System.out.print("Ange kundens adress (tryck Enter om ingen): ");
        String address = scanner.nextLine();
        System.out.print("Ange kundens lösenord: ");
        String password = scanner.nextLine();
        if (password.trim().isEmpty()) {
            System.out.println("Fel: Lösenord kan inte vara tomt!");
            return;
        }
        try {
            customerService.addCustomer(new Customer(0, name, email, phone, address, password));
            System.out.println("Kund '" + name + "' har lagts till i databasen!");
        } catch (SQLException e) {
            System.out.println("Fel: Kunde inte lägga till kund '" + name + "'. Kontrollera att alla uppgifter är korrekta.");
        } catch (IllegalArgumentException e) {
            System.out.println("Fel: " + e.getMessage());
        }
    }


    private void updateCustomerEmail(Scanner scanner) {
        System.out.print("Ange kundens ID: ");
        int id = readId(scanner);
        if (id == -1) return;
        System.out.print("Ange ny e-post: ");
        String newEmail = scanner.nextLine();
        if (newEmail.trim().isEmpty()) {
            System.out.println("Fel: E-post kan inte vara tom!");
            return;
        }
        try {
            customerService.updateCustomerEmail(id, newEmail);
            System.out.println("E-post för kund med ID " + id + " har uppdaterats till '" + newEmail + "'.");
        } catch (SQLException e) {
            System.out.println(e.getMessage().contains("hittades inte")
                    ? "Fel: Kund med ID " + id + " finns inte i databasen. Kontrollera ID:t."
                    : "Fel: Kunde inte uppdatera e-post för kund med ID " + id + ". Försök igen.");
        } catch (IllegalArgumentException e) {
            System.out.println("Fel: " + e.getMessage());
        }
    }


    private int readId(Scanner scanner) {
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (id <= 0) {
                System.out.println("Fel: Kund-ID måste vara ett positivt heltal!");
                return -1;
            }
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Fel: Ange ett giltigt nummer för kund-ID!");
            return -1;
        }
    }


    private void printCustomer(Customer c) {
        System.out.println("KundId: " + c.getCustomerId());
        System.out.println("Namn: " + c.getName());
        System.out.println("E-post: " + (c.getEmail() != null ? c.getEmail() : "Ej angivet"));
        System.out.println("Telefon: " + (c.getPhone() != null ? c.getPhone() : "Ej angivet"));
        System.out.println("Adress: " + (c.getAddress() != null ? c.getAddress() : "Ej angivet"));
        System.out.println("Lösenord: " + c.getPassword());
        System.out.println("---");
    }
}