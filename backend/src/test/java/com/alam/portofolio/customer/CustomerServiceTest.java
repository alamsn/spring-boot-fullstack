package com.alam.portofolio.customer;

import com.alam.portofolio.exceptions.DuplicateResourceException;
import com.alam.portofolio.exceptions.RequestValidationException;
import com.alam.portofolio.exceptions.ResourceNotFoundException;
import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @NotNull
    private static Customer getCustomer() {
        Random random = new Random();
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        return new Customer(
            firstName + " " + lastName,
            firstName.toLowerCase() + "." + lastName.toLowerCase() + "_"
                + UUID.randomUUID() + "@gmail.com",
            random.nextInt(17,70)
        );
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void getCustomerById() {
        // Given
        Integer customerId = 1;
        Customer customer = getCustomer();
        customer.setId(customerId);

        // When
        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));
        Customer actual = underTest.getCustomerById(customerId);


        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getCustomerByIdFails() {
        // Given
        Integer customerId = 10;

        // When
        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> underTest.getCustomerById(customerId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("customer with id [%s] not found".formatted(customerId));
    }

    @Test
    void addCustomer() {
        // Given
        Integer customerId = 10;
        String email = "alam@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            "alam", email, 19
        );
        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(false);

        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());
        Customer customer = customerArgumentCaptor.getValue();

        assertThat(customer.getId()).isNull();
        assertThat(customer.getName()).isEqualTo(request.name());
        assertThat(customer.getEmail()).isEqualTo(request.email());
        assertThat(customer.getAge()).isEqualTo(request.age());
    }

    @Test
    void addCustomerFails() {
        // Given
        String email = "alam@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            "alam", email, 19
        );

        // When
        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(true);

        // Then
        assertThatThrownBy(() -> underTest.addCustomer(request))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessage("email already taken");

        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerByIdSuccess() {
        // Given
        Integer customerId = 10;

        // When
        when(customerDAO.existCustomerWithId(customerId)).thenReturn(true);
        underTest.deleteCustomerById(customerId);

        // Then
        verify(customerDAO).deleteCustomerById(customerId);
    }

    @Test
    void deleteCustomerByIdFailed() {
        // Given
        Integer customerId = 10;

        // When
        when(customerDAO.existCustomerWithId(customerId)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> underTest.deleteCustomerById(customerId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("customer with id [%s] does not exist".formatted(customerId));

        verify(customerDAO, never()).deleteCustomerById(customerId);
    }

    @Test
    void updateCustomerIfAnyChanges() {
        // Given
        Integer customerId = 10;
        String email = "alam@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            "alam", email, 19
        );

        // When

        // Then
    }


    @Test
    void updateCustomerSuccess() {
        // Given
        Integer customerId = 1;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("newName", "newEmail@gmail.com", 25);

        Customer existingCustomer = new Customer("oldName", "oldEmail@gmail.com", 20);
        existingCustomer.setId(customerId);

        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerDAO.existsCustomerWithEmail(updateRequest.email())).thenReturn(false);

        // When
        underTest.updateCustomerIfAnyChanges(customerId, updateRequest);

        // Then
        verify(customerDAO).updateCustomer(any());
    }

    @Test
    void updateCustomerDuplicateEmail() {
        // Given
        Integer customerId = 1;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("newName", "newEmail@gmail.com", 25);

        Customer existingCustomer = new Customer("oldName", "oldEmail@gmail.com", 20);
        existingCustomer.setId(customerId);

        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerDAO.existsCustomerWithEmail(updateRequest.email())).thenReturn(true);

        // Then
        assertThrows(DuplicateResourceException.class,
            () -> underTest.updateCustomerIfAnyChanges(customerId, updateRequest));

        // Verify that updateCustomer is never called
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void updateCustomerNoChanges() {
        // Given
        Integer customerId = 1;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("oldName", "oldEmail@gmail.com", 20);

        Customer existingCustomer = new Customer("oldName", "oldEmail@gmail.com", 20);
        existingCustomer.setId(customerId);

        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(existingCustomer));

        // Then
        assertThrows(RequestValidationException.class,
            () -> underTest.updateCustomerIfAnyChanges(customerId, updateRequest));

        // Verify that updateCustomer is never called
        verify(customerDAO, never()).updateCustomer(any());
    }
}