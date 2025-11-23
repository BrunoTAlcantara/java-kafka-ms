package com.infrastructure.order.presenters;

import com.application.order.get.OrderOutput;
import com.application.order.list.OrderListOutput;
import com.domain.order.Order;
import com.infrastructure.order.models.OrderListResponse;
import com.infrastructure.order.models.OrderResponse;

public class OrderPresenters {

    public static OrderListResponse present(final OrderListOutput output) {
        return new OrderListResponse(
                output.id().valueId(),
                output.orderValue(),
                output.channel(),
                output.paymentStatus(),
                output.created(),
                output.updated()
        );
    }


    public static OrderResponse present(final OrderOutput output) {
        return new OrderResponse(
                output.id().valueId(),
                output.orderValue(),
                output.channel(),
                output.paymentStatus(),
                output.created(),
                output.updated()
        );
    }

    public static OrderOutput from(final Order order) {
        return new OrderOutput(
                order.getId(),
                order.getOrderValue(),
                order.getChannel(),
                order.getPaymentStatus(),
                order.getCreated(),
                order.getUpdated()
        );
    }
}
