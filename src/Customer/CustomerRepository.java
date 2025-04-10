package Customer;

import java.sql.*;
import java.util.ArrayList;

public class CustomerRepository {
    public static final String URL = "jdbc:sqlite:webbutiken.db";


    public ArrayList<Customer> getAll() throws SQLException {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getLong("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("password")
                ));
            }
            return customers;
        } catch (SQLException e) {
            throw new SQLException("Kunddata kunde inte hämtas: " + e.getMessage());
        }
    }


    public Customer getCustomerById(long customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            customerId,
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("password")
                    );
                }
                throw new SQLException("Kund med ID " + customerId + " hittades inte.");
            }
        } catch (SQLException e) {
            throw e;
        }
    }


    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getPassword());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Kunde inte lägga till kund '" + customer.getName() + "'.");
            }
        } catch (SQLException e) {
            throw e;
        }
    }


    public void updateCustomerEmail(long customerId, String email) throws SQLException {
        String sql = "UPDATE customers SET email = ? WHERE customer_id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setLong(2, customerId);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Ingen kund med ID " + customerId + " hittades.");
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}