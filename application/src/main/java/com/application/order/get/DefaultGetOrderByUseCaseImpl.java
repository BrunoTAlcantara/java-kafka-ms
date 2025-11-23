package com.application.order.get;

import com.domain.order.OrderGateway;
import com.domain.order.OrderID;

public class DefaultGetOrderByUseCaseImpl extends GetOrderByUseCase {

    private final OrderGateway orderGateway;

    public DefaultGetOrderByUseCaseImpl(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public OrderOutput execute(String id) {
        final var orderId = OrderID.from(id);
        return this.orderGateway.findById(orderId).map(OrderOutput::from)
                .orElseThrow(() -> new RuntimeException("Order cannot be found !!"));
    }
}
