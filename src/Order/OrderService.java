package Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService() {
        this.orderRepository = new OrderRepository();
    }


    public List<Order> getOrderHistoryByCustomer(long customerId) throws SQLException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Kund-ID får inte vara negativt eller noll.");
        }
        return orderRepository.getOrderHistoryByCustomer(customerId);
    }


    public long createOrder(long customerId) throws SQLException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Kund-ID får inte vara negativt eller noll.");
        }
        return orderRepository.createOrder(customerId);
    }


    public long createManyOrders(long customerId, Scanner scanner) throws SQLException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Kund-ID får inte vara negativt eller noll.");
        }
        return orderRepository.createManyOrders(customerId, scanner);
    }
}