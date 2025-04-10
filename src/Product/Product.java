package Product;

public class Product {
    private int productId;
    private int manufacturerId;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;

    public Product(int productId, int manufacturerId, String name, String description, double price, int stockQuantity) {
        this.productId = productId;
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    @Override
    public String toString() {
        return "Produkt [ID: " + productId + ", Tillverkare-ID: " + manufacturerId + ", Namn: " + name +
                ", Beskrivning: " + description + ", Pris: " + price + ", Lager: " + stockQuantity + "]";
    }
}