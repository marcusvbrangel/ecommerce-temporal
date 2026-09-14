package com.market.ecommerce.order.domain;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record EmailAddress(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public EmailAddress {

        Objects.requireNonNull(value, "Email address is required");

        value = value.trim().toLowerCase(Locale.ROOT);

        if (!EMAIL_PATTERN.matcher(value).matches()) {

            throw new IllegalArgumentException("Invalid email address");

        }

        if (value.length() > 320) {

            throw new IllegalArgumentException("Email address cannot exceed 320 characters");

        }

    }

}
