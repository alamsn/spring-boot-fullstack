package com.alam.portofolio.customer;

import com.alam.portofolio.AbsctractTestcontainers;
import com.alam.portofolio.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbsctractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
            getJDBCTemplate(),
            customerRowMapper
        );
    }

    @NotNull
    private static Customer getCustomer() {
        Random random = new Random();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        return new Customer(
            firstName + " " + lastName,
            firstName.toLowerCase() + "." + lastName.toLowerCase() + "_"
                + UUID.randomUUID() + "@gmail.com",
            random.nextInt(17,70),
			Constants.Gender.MALE);
    }

    @NotNull
    private Integer getCustomerId(Customer customer) {
        return underTest.selectAllCustomers().stream()
            .filter(c -> c.getEmail().equals(customer.getEmail()))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = getCustomer();
        underTest.insertCustomer(customer);

        // When
        List<Customer> customerList = underTest.selectAllCustomers();

        // Then
        assertThat(customerList).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        Customer customer = getCustomer();
        underTest.insertCustomer(customer);

        Integer customerId = getCustomerId(customer);

        // When
        Optional<Customer> actual = underTest.selectCustomerById(customerId);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c ->
            {
                assertThat(c.getId()).isEqualTo(customerId);
                assertThat(c.getName()).isEqualTo(customer.getName());
                assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                assertThat(c.getAge()).isEqualTo(customer.getAge());
            }
        );
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        int id = -1;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = getCustomer();
        underTest.insertCustomer(customer);

        Integer customerId = getCustomerId(customer);

        // When
        Optional<Customer> actual = underTest.selectCustomerById(customerId);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c ->
        {
           assertThat(c.getId()).isEqualTo(customerId);
           assertThat(c.getName()).isEqualTo(customer.getName());
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }



    @Test
    void existsCustomerWithEmail() {
        // Given
        Customer customer = getCustomer();
        underTest.insertCustomer(customer);

        // When
        boolean isCustomerExist = underTest.existsCustomerWithEmail(customer.getEmail());

        // Then
        assertThat(isCustomerExist).isTrue();
    }

    @Test
    void deleteCustomer() {
        // Given
        Customer customer = getCustomer();
        underTest.insertCustomer(customer);
        Integer customerId = getCustomerId(customer);
        underTest.deleteCustomerById(customerId);

        // When
        Optional<Customer> actual = underTest.selectCustomerById(customerId);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void existCustomerWithId() {
        // Given
        Customer customer = getCustomer();
        underTest.insertCustomer(customer);
        Integer customerId = getCustomerId(customer);

        // When
        boolean actual = underTest.existCustomerWithId(customerId);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = getCustomer();
        underTest.insertCustomer(customer);
        Integer customerId = getCustomerId(customer);
        Optional<Customer> actual1 = underTest.selectCustomerById(customerId);

        Customer updateCustomer = getCustomer();
        updateCustomer.setId(customerId);
        underTest.updateCustomer(updateCustomer);
        Optional<Customer> actual2 = underTest.selectCustomerById(customerId);

        // When
        boolean isUpdated = !actual1.equals(actual2);

        // Then
        assertThat(isUpdated).isTrue();
    }

    @Test
    void willNoUpdateCustomer() {
        // Given
        Customer customer = getCustomer();
        underTest.insertCustomer(customer);
        Integer customerId = getCustomerId(customer);
        Optional<Customer> actual1 = underTest.selectCustomerById(customerId);

        underTest.updateCustomer(customer);
        Optional<Customer> actual2 = underTest.selectCustomerById(customerId);

        // When
        boolean isNotUpdated = actual1.equals(actual2);

        // Then
        assertThat(isNotUpdated).isTrue();
    }
}