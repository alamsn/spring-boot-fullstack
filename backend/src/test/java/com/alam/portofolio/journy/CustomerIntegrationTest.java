package com.alam.portofolio.journy;

import com.alam.portofolio.customer.Customer;
import com.alam.portofolio.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

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
    void canRegisterCustomer() {
        String CUSTOMER_URI = "/api/v1/customers";
        Customer customer = getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            customer.getName(), customer.getEmail(), customer.getAge()
        );

        // Send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(customer);

        // get customer by id
        assert allCustomers != null;
        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();
        customer.setId(id);

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(customer);

    }

    @Test
    void canDeleteCustomer() {
        String CUSTOMER_URI = "/api/v1/customers";
        Customer customer = getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                customer.getName(), customer.getEmail(), customer.getAge()
        );

        // Send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id
        assert allCustomers != null;
        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();

        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        String CUSTOMER_URI = "/api/v1/customers";
        Customer customer = getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                customer.getName(), customer.getEmail(), customer.getAge()
        );

        // Send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(customer);

        // get customer by id
        assert allCustomers != null;
        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();
        customer.setId(id);

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                customer.getName() + "mod", customer.getEmail() + "zzz", customer.getAge()
        );

        // update customer
        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get updated customer
        Customer expectedUpdateCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // is updated?
        assert expectedUpdateCustomer != null;
        assertThat(expectedUpdateCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(expectedUpdateCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(expectedUpdateCustomer.getAge()).isEqualTo(updateRequest.age());

    }
}
