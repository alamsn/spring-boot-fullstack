package com.alam.portofolio.customer;

import com.alam.portofolio.util.Constants;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age,
		Constants.Gender gender
) {
}
