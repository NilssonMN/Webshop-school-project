package Order;

import Product.Product;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long orderId;
    private long customerId;
    private LocalDate orderDate;
    private List<Product> products;
    private List<Integer> quantities;
    private double totalPrice;

    public Order(long orderId, long customerId, LocalDate orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.products = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public void addProduct(Product product, int orderQuantity) {
        this.products.add(product);
        this.quantities.add(orderQuantity);
        this.totalPrice += product.getPrice() * orderQuantity;
    }

    public long getOrderId() {
        return orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "Order [ID: " + orderId + ", Kund-ID: " + customerId + ", Datum: " + orderDate + ", Totalt pris: " + totalPrice + "]";
    }
}