package Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerService {
    private CustomerRepository customerRepository = new CustomerRepository();


    public ArrayList<Customer> getAllCustomers() throws SQLException {
        return customerRepository.getAll();
    }


    public Customer getCustomerById(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Kund-ID måste vara ett positivt heltal!");
        return customerRepository.getCustomerById(id);
    }


    public void addCustomer(Customer customer) throws SQLException {
        if (customer == null) throw new IllegalArgumentException("Kundobjektet får inte vara null!");

        if (customer.getName() == null || customer.getName().trim().isEmpty())
            throw new IllegalArgumentException("Namnet kan inte vara tomt!");

        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty())
            throw new IllegalArgumentException("Lösenordet kan inte vara tomt!");

        if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty() && !customer.getEmail().contains("@"))
            throw new IllegalArgumentException("Ogiltig e-postadress (måste innehålla '@')!");
        customerRepository.addCustomer(customer);
    }


    public void updateCustomerEmail(int customerId, String email) throws SQLException {
        if (customerId <= 0) throw new IllegalArgumentException("Kund-ID måste vara ett positivt heltal!");
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("E-postadressen kan inte vara tom!");
        if (!email.contains("@")) throw new IllegalArgumentException("Ogiltig e-postadress (måste innehålla '@')!");
        customerRepository.updateCustomerEmail(customerId, email);
    }
}