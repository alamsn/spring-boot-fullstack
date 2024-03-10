package com.alam.portofolio.customer;

import com.alam.portofolio.exceptions.DuplicateResourceException;
import com.alam.portofolio.exceptions.RequestValidationException;
import com.alam.portofolio.exceptions.ResourceNotFoundException;
import com.alam.portofolio.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomerById(Integer customerId) {
        return customerDAO.selectCustomerById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "customer with id [%s] not found".formatted(customerId)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        //cek email exist
        if (customerDAO.existsCustomerWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("email already taken");
        }
        Customer customer = new Customer(
            customerRegistrationRequest.name(),
            customerRegistrationRequest.email(),
            customerRegistrationRequest.age(),
			Constants.Gender.MALE);
        customerDAO.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer customerId) {
        if (!customerDAO.existCustomerWithId(customerId)) {
            throw new ResourceNotFoundException(
                "customer with id [%s] does not exist".formatted(customerId));
        }
        customerDAO.deleteCustomerById(customerId);
    }

    public void updateCustomerIfAnyChanges(Integer customerId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = getCustomerById(customerId);
        Customer updateCustomer = new Customer(
                customerUpdateRequest.name(),
                customerUpdateRequest.email(),
                customerUpdateRequest.age(),
			Constants.Gender.MALE);
            updateCustomer.setId(customer.getId());
            if (!customer.equals(updateCustomer)) {
                if (!customerDAO.existsCustomerWithEmail(updateCustomer.getEmail())
                    || customer.getEmail().equals(updateCustomer.getEmail())) {
                    customerDAO.updateCustomer(updateCustomer);
                } else {
                    throw new DuplicateResourceException("email already taken");
                }
            } else {
                throw new RequestValidationException("no data change found");
            }
    }
}
