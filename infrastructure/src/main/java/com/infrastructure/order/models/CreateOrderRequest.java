package com.infrastructure.order.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CreateOrderRequest(@JsonProperty("orderValue") BigDecimal orderValue,
                                 @JsonProperty("channel") String channel,
                                 @JsonProperty("paymentStatus") String paymentStatus) {
}
