package com.alam.portofolio.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
