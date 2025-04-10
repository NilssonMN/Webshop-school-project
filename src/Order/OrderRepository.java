package Order;

import Product.Product;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderRepository {
    public static final String URL = "jdbc:sqlite:webbutiken.db";


    public List<Order> getOrderHistoryByCustomer(long customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT o.order_id, o.customer_id, o.order_date, " +
                             "op.product_id, p.name AS product_name, p.price, op.quantity " +
                             "FROM orders o " +
                             "LEFT JOIN orders_products op ON o.order_id = op.order_id " +
                             "LEFT JOIN products p ON op.product_id = p.product_id " +
                             "WHERE o.customer_id = ?")) {
            pstmt.setLong(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            long lastOrderId = -1;
            Order order = null;
            while (rs.next()) {
                long orderId = rs.getLong("order_id");
                if (orderId != lastOrderId) {
                    order = new Order(orderId, rs.getLong("customer_id"), rs.getObject("order_date", LocalDate.class));
                    orders.add(order);
                    lastOrderId = orderId;
                }
                int productId = rs.getInt("product_id");
                if (!rs.wasNull()) {
                    int orderQuantity = rs.getInt("quantity");
                    if (orderQuantity < 0) throw new SQLException("Negativt antal för produkt-ID " + productId);
                    Product product = new Product(productId, 0, rs.getString("product_name"), "", rs.getDouble("price"), 0);
                    assert order != null;
                    order.addProduct(product, orderQuantity);
                }
            }
            if (orders.isEmpty()) throw new SQLException("Inga ordrar hittades för kund-ID " + customerId);
        }
        return orders;
    }


    public long createOrder(long customerId) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, order_date) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, customerId);
            pstmt.setObject(2, LocalDate.now());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Kunde inte skapa order för kund-ID " + customerId);
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Kunde inte hämta order-ID för kund-ID " + customerId);
                }
            }
        }
    }


    public long createManyOrders(long customerId, Scanner scanner) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement orderPstmt = conn.prepareStatement(
                     "INSERT INTO orders (customer_id, order_date) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            orderPstmt.setLong(1, customerId);
            orderPstmt.setObject(2, LocalDate.now());
            orderPstmt.executeUpdate();
            ResultSet rs = orderPstmt.getGeneratedKeys();
            rs.next();
            long orderId = rs.getLong(1);

            PreparedStatement checkPstmt = conn.prepareStatement(
                    "SELECT price, stock_quantity FROM products WHERE product_id = ?");
            PreparedStatement productPstmt = conn.prepareStatement(
                    "INSERT INTO orders_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)");

            while (true) {
                System.out.print("Ange produkt-ID (0 för att sluta): ");
                int productId;
                try {
                    productId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Fel: Ange ett giltigt produkt-ID.");
                    continue;
                }
                if (productId == 0) break;

                checkPstmt.setInt(1, productId);
                ResultSet checkRs = checkPstmt.executeQuery();
                if (!checkRs.next()) throw new SQLException("Produkt-ID " + productId + " finns inte.");
                Product product = new Product(productId, 0, "", "", checkRs.getDouble("price"), checkRs.getInt("stock_quantity"));

                System.out.println("Lagersaldo för produkt-ID " + productId + ": " + product.getStockQuantity() + " st");
                System.out.print("Ange antal: ");
                int quantity;
                try {
                    quantity = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Fel: Ange ett giltigt antal.");
                    continue;
                }

                if (quantity > product.getStockQuantity()) throw new SQLException("För lite i lager för produkt-ID " + productId);

                productPstmt.setLong(1, orderId);
                productPstmt.setInt(2, productId);
                productPstmt.setInt(3, quantity);
                productPstmt.setDouble(4, product.getPrice());
                productPstmt.executeUpdate();
                System.out.println("Lade till " + quantity + " st av produkt-ID " + productId);
            }
            return orderId;
        }
    }
}