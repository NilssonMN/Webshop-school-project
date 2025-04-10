package Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductController {
    private ProductService productService = new ProductService();

    public void runMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            showMenu();
            String select = scanner.nextLine();

            switch (select) {
                case "1" -> listAllProducts();
                case "2" -> searchProductsByName(scanner);
                case "3" -> searchProductsByCategory(scanner);
                case "4" -> updateProductPrice(scanner);
                case "5" -> updateStockQuantity(scanner);
                case "6" -> addNewProduct(scanner);
                case "7" -> running = false;
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
            System.out.println();
        }
        System.out.println("Återgår till huvudmenyn...");
    }

    private void showMenu() {
        System.out.println("!=== Hantera Produkter ===!");
        System.out.println("1. Visa alla produkter");
        System.out.println("2. Sök produkter efter namn");
        System.out.println("3. Sök produkter efter kategori");
        System.out.println("4. Uppdatera pris");
        System.out.println("5. Uppdatera lagersaldo");
        System.out.println("6. Lägg till ny produkt");
        System.out.println("7. Återgå till huvudmenyn");
        System.out.print("Välj ett alternativ: ");
    }

    private void listAllProducts() {
        try {
            ArrayList<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                System.out.println("Inga produkter hittades i databasen.");
            } else {
                for (Product p : products) {
                    printProduct(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Fel: Kunde inte hämta produkterna. Kontrollera att databasen är tillgänglig.");
        }
    }

    private void searchProductsByName(Scanner scanner) {
        System.out.print("Ange sökterm för namn: ");
        String searchTerm = scanner.nextLine();
        if (searchTerm.trim().isEmpty()) {
            System.out.println("Fel: Söktermen kan inte vara tom!");
            return;
        }
        try {
            ArrayList<Product> products = productService.searchProductsByName(searchTerm);
            if (products.isEmpty()) {
                System.out.println("Inga produkter hittades med namn som matchar '" + searchTerm + "'.");
            } else {
                for (Product p : products) {
                    printProduct(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Fel: Kunde inte söka efter produkter med namn '" + searchTerm + "'. Försök igen.");
        }
    }

    private void searchProductsByCategory(Scanner scanner) {
        System.out.print("Ange kategorinamn: ");
        String categoryName = scanner.nextLine();
        if (categoryName.trim().isEmpty()) {
            System.out.println("Fel: Kategorinamnet kan inte vara tomt!");
            return;
        }
        try {
            ArrayList<Product> products = productService.searchProductsByCategory(categoryName);
            if (products.isEmpty()) {
                System.out.println("Inga produkter hittades i kategorin '" + categoryName + "'.");
            } else {
                for (Product p : products) {
                    printProduct(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Fel: Kunde inte söka efter produkter i kategori '" + categoryName + "'. Försök igen.");
        }
    }

    private void updateProductPrice(Scanner scanner) {
        System.out.print("Ange produkt-ID: ");
        int productId = readId(scanner);
        if (productId == -1) return;
        try {

            int currentStock = productService.getStockQuantity(productId);
            System.out.println("Nuvarande lagersaldo för produkt-ID " + productId + ": " + currentStock + " st");
        } catch (SQLException e) {
            System.out.println("Fel: " + e.getMessage());
            return;
        }
        System.out.print("Ange nytt pris: ");
        try {
            double newPrice = Double.parseDouble(scanner.nextLine());
            if (newPrice < 0) {
                System.out.println("Fel: Priset kan inte vara negativt!");
                return;
            }
            productService.updateProductPrice(productId, newPrice);
            System.out.println("Priset för produkt med ID " + productId + " har uppdaterats till '" + newPrice + "'.");
        } catch (NumberFormatException e) {
            System.out.println("Fel: Priset måste vara ett giltigt nummer!");
        } catch (SQLException e) {
            System.out.println("Fel: Kunde inte uppdatera pris för produkt med ID " + productId + ". Försök igen.");
        }
    }

    private void updateStockQuantity(Scanner scanner) {
        System.out.print("Ange produkt-ID: ");
        int productId = readId(scanner);
        if (productId == -1) return;
        try {

            int currentStock = productService.getStockQuantity(productId);
            System.out.println("Nuvarande lagersaldo för produkt-ID " + productId + ": " + currentStock + " st");
        } catch (SQLException e) {
            System.out.println("Fel: " + e.getMessage());
            return;
        }
        System.out.print("Ange nytt lagersaldo: ");
        try {
            int newStock = Integer.parseInt(scanner.nextLine());
            if (newStock < 0) {
                System.out.println("Fel: Lagersaldot kan inte vara negativt!");
                return;
            }
            productService.updateStockQuantity(productId, newStock);
            System.out.println("Lagersaldot för produkt med ID " + productId + " har uppdaterats till '" + newStock + "'.");
        } catch (NumberFormatException e) {
            System.out.println("Fel: Lagersaldot måste vara ett giltigt nummer!");
        } catch (SQLException e) {
            System.out.println("Fel: Kunde inte uppdatera lagersaldo för produkt med ID " + productId + ". Försök igen.");
        }
    }

    private void addNewProduct(Scanner scanner) {
        System.out.print("Ange tillverkare-ID: ");
        int manufacturerId = readId(scanner);
        if (manufacturerId == -1) return;
        System.out.print("Ange produktnamn: ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            System.out.println("Fel: Produktnamn kan inte vara tomt!");
            return;
        }
        System.out.print("Ange beskrivning (tryck Enter om ingen): ");
        String description = scanner.nextLine();
        System.out.print("Ange pris: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
            if (price < 0) {
                System.out.println("Fel: Priset kan inte vara negativt!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Fel: Priset måste vara ett giltigt nummer!");
            return;
        }
        System.out.print("Ange lagersaldo: ");
        int stockQuantity;
        try {
            stockQuantity = Integer.parseInt(scanner.nextLine());
            if (stockQuantity < 0) {
                System.out.println("Fel: Lagersaldot kan inte vara negativt!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Fel: Lagersaldot måste vara ett giltigt nummer!");
            return;
        }
        try {
            productService.addProduct(new Product(0, manufacturerId, name, description, price, stockQuantity));
            System.out.println("Produkt '" + name + "' har lagts till i databasen!");
        } catch (SQLException e) {
            System.out.println("Fel: Kunde inte lägga till produkt '" + name + "'. Kontrollera att alla uppgifter är korrekta.");
        }
    }

    private int readId(Scanner scanner) {
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (id <= 0) {
                System.out.println("Fel: ID måste vara ett positivt heltal!");
                return -1;
            }
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Fel: Ange ett giltigt nummer för ID!");
            return -1;
        }
    }

    private void printProduct(Product p) {
        System.out.println("Produkt-ID: " + p.getProductId());
        System.out.println("Tillverkare-ID: " + p.getManufacturerId());
        System.out.println("Namn: " + p.getName());
        System.out.println("Beskrivning: " + (p.getDescription() != null ? p.getDescription() : "Ej angivet"));
        System.out.println("Pris: " + p.getPrice());
        System.out.println("Lagersaldo: " + p.getStockQuantity());
        System.out.println("---");
    }
}