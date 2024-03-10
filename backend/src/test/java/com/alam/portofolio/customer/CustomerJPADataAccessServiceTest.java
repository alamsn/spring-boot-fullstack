package com.alam.portofolio.customer;

import com.alam.portofolio.util.Constants;
import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
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
            random.nextInt(17,70),
			Constants.Gender.MALE);
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();

        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        Integer customerId = 3;

        // When
        underTest.selectCustomerById(customerId);

        // Then
        verify(customerRepository).findById(customerId);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = getCustomer();

        // When
        underTest.insertCustomer(customer);

        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        // Given
        String email = "alam@gmail.com";

        // When
        underTest.existsCustomerWithEmail(email);

        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomerById() {
        // Given
        Integer customerId = 3;

        // When
        underTest.deleteCustomerById(customerId);

        // Then
        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void existCustomerWithId() {
        // Given
        Integer customerId = 3;

        // When
        underTest.existCustomerWithId(customerId);

        // Then
        verify(customerRepository).existsCustomerById(customerId);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = getCustomer();

        // When
        underTest.updateCustomer(customer);

        // Then
        verify(customerRepository).save(customer);
    }
}