package com.alam.portofolio;

import com.alam.portofolio.customer.Customer;
import com.alam.portofolio.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class AlamsnApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlamsnApiApplication.class, args);
    }

    @Bean
    CommandLineRunner cmd(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            Customer customer = new Customer(
                firstName + " " + lastName,
                firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
                random.nextInt(70)
            );
            customerRepository.save(customer);
        };
    }
}
