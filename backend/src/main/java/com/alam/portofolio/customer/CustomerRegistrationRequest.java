package com.alam.portofolio.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
