package com.infrastructure.order.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record UpdateOrderRequest(@JsonProperty("orderId") String orderId,
                                 @JsonProperty("orderValue") BigDecimal orderValue,
                                 @JsonProperty("channel") String channel,
                                 @JsonProperty("paymentStatus") String paymentStatus) {
}
