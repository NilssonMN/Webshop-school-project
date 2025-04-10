package Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductService {
    private ProductRepository productRepository = new ProductRepository();


    public ArrayList<Product> getAllProducts() throws SQLException {
        return productRepository.getAllProducts();
    }


    public ArrayList<Product> searchProductsByName(String searchTerm) throws SQLException {
        if (searchTerm == null || searchTerm.trim().isEmpty())
            throw new IllegalArgumentException("Söktermen kan inte vara tom!");
        return productRepository.searchProductsByName(searchTerm);
    }


    public ArrayList<Product> searchProductsByCategory(String categoryName) throws SQLException {
        if (categoryName == null || categoryName.trim().isEmpty())
            throw new IllegalArgumentException("Kategorinamnet kan inte vara tomt!");
        return productRepository.searchProductsByCategory(categoryName);
    }


    public void updateProductPrice(int productId, double newPrice) throws SQLException {
        if (productId <= 0) throw new IllegalArgumentException("Produkt-ID måste vara ett positivt heltal!");
        if (newPrice < 0) throw new IllegalArgumentException("Priset kan inte vara negativt!");
        productRepository.updateProductPrice(productId, newPrice);
    }


    public void updateStockQuantity(int productId, int newStock) throws SQLException {
        if (productId <= 0) throw new IllegalArgumentException("Produkt-ID måste vara ett positivt heltal!");
        if (newStock < 0) throw new IllegalArgumentException("Lagersaldot kan inte vara negativt!");
        productRepository.updateStockQuantity(productId, newStock);
    }


    public void addProduct(Product product) throws SQLException {
        if (product == null) throw new IllegalArgumentException("Produkten kan inte vara null!");

        if (product.getManufacturerId() <= 0) throw new IllegalArgumentException("Tillverkare-ID måste vara ett positivt heltal!");

        if (product.getName() == null || product.getName().trim().isEmpty())
            throw new IllegalArgumentException("Produktnamn kan inte vara tomt!");

        if (product.getPrice() < 0) throw new IllegalArgumentException("Priset kan inte vara negativt!");

        if (product.getStockQuantity() < 0) throw new IllegalArgumentException("Lagersaldot kan inte vara negativt!");
        productRepository.addProduct(product);
    }


    public int getStockQuantity(int productId) throws SQLException {
        if (productId <= 0) throw new IllegalArgumentException("Produkt-ID måste vara ett positivt heltal!");
        try {
            return productRepository.getStockQuantity(productId);
        } catch (SQLException e) {
            String errorMsg = e.getMessage().contains("hittades inte")
                    ? "Produkt med ID " + productId + " finns inte. Kontrollera att ID:t är korrekt."
                    : "Kunde inte hämta lagersaldo för produkt med ID " + productId + ". Försök igen.";
            throw new SQLException(errorMsg);
        }
    }
}