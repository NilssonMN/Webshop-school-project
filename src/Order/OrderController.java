package Order;

import Customer.Customer;
import Customer.CustomerRepository;
import Product.Product;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class OrderController {
    private final OrderService orderService;
    private final CustomerRepository customerRepository;


    public OrderController() {
        this.orderService = new OrderService();
        this.customerRepository = new CustomerRepository();
    }


    public void runMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            showMenu();
            String select = scanner.nextLine();

            switch (select) {
                case "1" -> getOrderHistoryByCustomer(scanner);
                case "2" -> createOrder(scanner);
                case "3" -> createManyOrders(scanner);
                case "4" -> running = false;
                default -> System.out.println("Ogiltigt val, välj ett nummer mellan 1 och 4.");
            }
            System.out.println();
        }
        System.out.println("Återgår till huvudmenyn...");
    }


    private void showMenu() {
        System.out.println("!=== Hantera Ordrar ===!");
        System.out.println("1. Se orderhistorik för en kund");
        System.out.println("2. Lägga en order");
        System.out.println("3. Lägga en order med flera produkter");
        System.out.println("4. Återgå till huvudmenyn");
        System.out.print("Välj ett alternativ: ");
    }


    private void getOrderHistoryByCustomer(Scanner scanner) {
        System.out.print("Ange kund-ID: ");
        try {
            long customerId = Long.parseLong(scanner.nextLine());
            if (customerId <= 0) {
                System.out.println("Fel: Kund-ID måste vara ett positivt tal.");
                return;
            }
            List<Order> orders = orderService.getOrderHistoryByCustomer(customerId);
            Customer customer = customerRepository.getCustomerById(customerId);
            if (customer == null) {
                System.out.println("Fel: Ingen kund hittades med ID " + customerId + ".");
                return;
            }
            System.out.println("Kundinformation för kund-ID " + customerId + ":");
            System.out.println("Namn: " + customer.getName() +
                    ", Email: " + customer.getEmail() +
                    ", Telefon: " + customer.getPhone() +
                    ", Adress: " + customer.getAddress() +
                    ", Lösenord: " + customer.getPassword());
            System.out.println("Orderhistorik:");
            if (orders.isEmpty()) {
                System.out.println("Ingen orderhistorik hittades för kund-ID " + customerId + ".");
            } else {
                for (Order order : orders) {
                    System.out.println("Order ID: " + order.getOrderId() + ", Datum: " + order.getOrderDate() +
                            ", Totalt pris: " + String.format("%.2f", order.getTotalPrice()));
                    List<Product> products = order.getProducts();
                    List<Integer> quantities = order.getQuantities();
                    if (products.isEmpty()) {
                        System.out.println("  Inga produkter i denna order.");
                    } else {
                        for (int i = 0; i < products.size(); i++) {
                            Product product = products.get(i);
                            int quantity = quantities.get(i);
                            System.out.println("  Namn: " + product.getName() + ", Pris: " + product.getPrice() + ", Antal: " + quantity);
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Fel: Ange ett giltigt kund-ID (ett positivt heltal).");
        } catch (IllegalArgumentException e) {
            System.out.println("Fel: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Ett databasfel uppstod vid hämtning av orderhistorik eller kunddata: " + e.getMessage());
        }
    }


    private void createOrder(Scanner scanner) {
        System.out.print("Ange kund-ID: ");
        try {
            long customerId = Long.parseLong(scanner.nextLine());
            if (customerId <= 0) {
                System.out.println("Fel: Kund-ID måste vara ett positivt tal.");
                return;
            }
            long orderId = orderService.createOrder(customerId);
            System.out.println("Order med ID " + orderId + " har lagts för kund-ID " + customerId + " med datum " + LocalDate.now() + ".");
        } catch (NumberFormatException e) {
            System.out.println("Fel: Ange ett giltigt kund-ID (ett positivt heltal).");
        } catch (IllegalArgumentException e) {
            System.out.println("Fel: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Ett databasfel uppstod vid skapande av order: " + e.getMessage());
        }
    }


    private void createManyOrders(Scanner scanner) {
        System.out.print("Ange kund-ID: ");
        try {
            long customerId = Long.parseLong(scanner.nextLine());
            if (customerId <= 0) {
                System.out.println("Fel: Kund-ID måste vara ett positivt tal.");
                return;
            }
            long orderId = orderService.createManyOrders(customerId, scanner);
            System.out.println("Order med ID " + orderId + " har lagts för kund-ID " + customerId + " med datum " + LocalDate.now() + ".");
        } catch (NumberFormatException e) {
            System.out.println("Fel: Ange ett giltigt kund-ID (ett positivt heltal).");
        } catch (IllegalArgumentException e) {
            System.out.println("Fel: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Ett databasfel uppstod: " + e.getMessage());
        }
    }
}