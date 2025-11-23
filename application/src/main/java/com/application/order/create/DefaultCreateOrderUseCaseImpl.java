package com.application.order.create;

import com.domain.order.Order;
import com.domain.order.OrderGateway;
import com.domain.validators.handlers.NotificationEntity;

public class DefaultCreateOrderUseCaseImpl extends CreateOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultCreateOrderUseCaseImpl(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public CreateOrderOutput execute(CreateOrderCommand command) {

        final var orderValue = command.orderValue();
        final var channel = command.channel();
        final var paymentStatus = command.paymentStatus();

        final var order = Order.newOrder(orderValue, channel, paymentStatus);
        final var notification = NotificationEntity.create();
        order.validate(notification);

        return notification.hasError() ? CreateOrderOutput.withErrors(notification.getErrorsEntity())
                : CreateOrderOutput.outPutFrom(orderGateway.persist(order));
    }
}
