package Product;

import java.sql.*;
import java.util.ArrayList;

public class ProductRepository {
    public static final String URL = "jdbc:sqlite:webbutiken.db";


    public ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();

             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getInt("manufacturer_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity")
                ));
            }
            return products;
        } catch (SQLException e) {
            throw new SQLException("Kunde inte hämta alla produkter: " + e.getMessage());
        }
    }


    public ArrayList<Product> searchProductsByName(String searchTerm) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE UPPER(name) LIKE UPPER(?)";

        try (Connection conn = DriverManager.getConnection(URL);

             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("product_id"),
                            rs.getInt("manufacturer_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity")
                    ));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new SQLException("Kunde inte söka efter produkter med namn '" + searchTerm + "': " + e.getMessage());
        }
    }


    public ArrayList<Product> searchProductsByCategory(String categoryName) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT p.* FROM products p " +
                "JOIN products_categories pc ON p.product_id = pc.product_id " +
                "JOIN categories c ON pc.category_id = c.category_id " +
                "WHERE UPPER(c.name) = UPPER(?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoryName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("product_id"),
                            rs.getInt("manufacturer_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity")
                    ));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new SQLException("Kunde inte söka efter produkter i kategori '" + categoryName + "': " + e.getMessage());
        }
    }


    public void updateProductPrice(int productId, double newPrice) throws SQLException {
        String sql = "UPDATE products SET price = ? WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);

             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, productId);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Ingen produkt hittades med ID " + productId + " att uppdatera pris för.");
            }
        } catch (SQLException e) {
            throw new SQLException("Kunde inte uppdatera pris för produkt med ID " + productId + ": " + e.getMessage());
        }
    }


    public void updateStockQuantity(int productId, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = ? WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);

             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newStock);
            pstmt.setInt(2, productId);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Ingen produkt hittades med ID " + productId + " att uppdatera lagersaldo för.");
            }
        } catch (SQLException e) {
            throw new SQLException("Kunde inte uppdatera lagersaldo för produkt med ID " + productId + ": " + e.getMessage());
        }
    }


    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (manufacturer_id, name, description, price, stock_quantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);

             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, product.getManufacturerId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getStockQuantity());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Kunde inte lägga till produkt '" + product.getName() + "' i databasen.");
            }
        } catch (SQLException e) {
            throw new SQLException("Kunde inte lägga till produkt '" + product.getName() + "': " + e.getMessage());
        }
    }


    public int getStockQuantity(int productId) throws SQLException {
        String sql = "SELECT stock_quantity FROM products WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);

             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("stock_quantity");
                throw new SQLException("Produkt med ID " + productId + " hittades inte i databasen.");
            }
        } catch (SQLException e) {
            throw new SQLException("Kunde inte hämta lagersaldo för produkt med ID " + productId + ": " + e.getMessage());
        }
    }

}