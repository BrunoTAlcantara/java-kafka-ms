package com.infrastructure.order.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderListResponse(@JsonProperty("id") String orderId,
                                @JsonProperty("orderValue") BigDecimal orderValue,
                                @JsonProperty("channel") String channel,
                                @JsonProperty("paymentStatus") String paymentStatus,
                                @JsonProperty("created") Instant created,
                                @JsonProperty("updated") Instant updated
) {
}
