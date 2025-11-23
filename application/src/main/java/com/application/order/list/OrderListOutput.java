package com.application.order.list;

import com.domain.order.Order;
import com.domain.order.OrderID;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderListOutput(OrderID id, BigDecimal orderValue, String channel, String paymentStatus, Instant created,
                              Instant updated) {

    public static OrderListOutput from (final Order order){
        return new OrderListOutput(order.getId(),
                order.getOrderValue(),
                order.getChannel(),
                order.getPaymentStatus(),
                order.getCreated(),
                order.getUpdated());
    }
}
