package com.application.order.update;

import java.math.BigDecimal;

public record UpdateOrderCommand(String orderId, BigDecimal orderValue, String channel, String paymentStatus) {

    public static UpdateOrderCommand apply(String orderId, BigDecimal orderValue, String channel, String paymentStatus) {
        return new UpdateOrderCommand(orderId, orderValue, channel, paymentStatus);
    }

}
