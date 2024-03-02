package com.alam.portofolio.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer customerId);
    void insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    void deleteCustomerById(Integer customerId);
    boolean existCustomerWithId(Integer customerId);
    void updateCustomer(Customer customer);

}
