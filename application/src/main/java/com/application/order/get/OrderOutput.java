package com.application.order.get;

import com.domain.order.Order;
import com.domain.order.OrderID;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderOutput(OrderID id, BigDecimal orderValue, String channel, String paymentStatus, Instant created,
                          Instant updated) {

    public static OrderOutput from(Order order){
        return new OrderOutput(order.getId(),
                order.getOrderValue(),
                order.getChannel(), order.getPaymentStatus(),
                order.getCreated(),
                order.getUpdated());
    }
}
