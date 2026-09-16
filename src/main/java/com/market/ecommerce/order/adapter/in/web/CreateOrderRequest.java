package com.market.ecommerce.order.adapter.in.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull
        UUID customerId,

        @NotBlank
        @Email
        String customerEmail,

        @NotEmpty
        List<@Valid Item> items

) {

    public record Item(

        @NotNull
        UUID productId,

        @Min(1)
        int quantity

    ) {}

}
