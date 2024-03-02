package com.alam.portofolio.customer;

import com.alam.portofolio.AbsctractTestcontainers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbsctractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
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
            random.nextInt(17,70)
        );
    }

    @NotNull
    private Integer getCustomerId(Customer customer) {
        return underTest.findAll().stream()
            .filter(c -> c.getEmail().equals(customer.getEmail()))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        Customer customer = getCustomer();
        underTest.save(customer);

        // When
        boolean isCustomerExist = underTest.existsCustomerByEmail(customer.getEmail());

        // Then
        assertThat(isCustomerExist).isTrue();
    }

    @Test
    void existsCustomerById() {
        // Given
        Customer customer = getCustomer();
        underTest.save(customer);
        Integer customerId = getCustomerId(customer);

        // When
        var actual = underTest.existsCustomerById(customerId);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsIfEmailNotPresent() {
        // Given
        String notPresentEmail = "aaaaa@gmail.com";
        Customer customer = getCustomer();
        underTest.save(customer);

        // When
        boolean isCustomerExist = underTest.existsCustomerByEmail(notPresentEmail);

        // Then
        assertThat(isCustomerExist).isFalse();
    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        // Given
        Integer notPresentId = -1;
        Customer customer = getCustomer();
        underTest.save(customer);
        Integer customerId = getCustomerId(customer);

        // When
        var actual = underTest.existsCustomerById(notPresentId);

        // Then
        assertThat(actual).isFalse();
    }
}