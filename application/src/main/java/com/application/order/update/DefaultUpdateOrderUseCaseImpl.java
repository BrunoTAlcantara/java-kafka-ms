package com.application.order.update;

import com.domain.order.OrderGateway;
import com.domain.order.OrderID;
import com.domain.validators.handlers.NotificationEntity;

public class DefaultUpdateOrderUseCaseImpl extends UpdateOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultUpdateOrderUseCaseImpl(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public UpdateOrderOutPut execute(UpdateOrderCommand command) {

        final var orderId = OrderID.from(command.orderId());
        final var orderValue = command.orderValue();
        final var channel = command.channel();
        final var paymentStatus = command.paymentStatus();

        final var orderUpdate = this.orderGateway.findById(orderId).orElseThrow(() -> new RuntimeException("Not found"));
        final var notification = NotificationEntity.create();

        orderUpdate.update(orderId.valueId(), orderValue, channel, paymentStatus).validate(notification);

        return notification.hasError() ?
                UpdateOrderOutPut.withErrors(orderId.valueId(), notification.getErrorsEntity()) :
                UpdateOrderOutPut.applyOutPut(this.orderGateway.update(orderUpdate));
    }
}
