package com.alam.portofolio.customer;

import com.alam.portofolio.util.Constants;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age,
		Constants.Gender gender
) {
}
