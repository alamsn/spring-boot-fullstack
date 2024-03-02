package com.alam.portofolio.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{

    private static final List<Customer> customerList;

    static {
        customerList = new ArrayList<>();
        Customer alam = new Customer(
                1, "Alam", "alam@gmail.com", 24
        );
        Customer albert = new Customer(
                2, "Albert", "albert@gmail.com", 27
        );
        customerList.add(alam);
        customerList.add(albert);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customerList;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customerList.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerList.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customerList.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        customerList.removeIf(customer -> customer.getId().equals(customerId));
    }

    @Override
    public boolean existCustomerWithId(Integer customerId) {
        return customerList.stream().anyMatch(customer -> customer.getId().equals(customerId));
    }

    @Override
    public void updateCustomer(Customer customer) {
    }

}
