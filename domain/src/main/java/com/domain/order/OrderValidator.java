package com.domain.order;


import com.domain.validators.Error;
import com.domain.validators.ValidationHandlerDomain;
import com.domain.validators.Validator;


public class OrderValidator extends Validator {

    private Order order;

    protected OrderValidator(Order order, ValidationHandlerDomain handler) {
        super(handler);
        this.order = order;
    }


    @Override
    public void validate() {

        final var orderId = this.order.getId();
        final var orderValue = this.order.getOrderValue();
        final var channel = this.order.getChannel();
        final var paymentStatus = this.order.getPaymentStatus();

        if (orderId == null) {
            this.validatorHandler().append(new Error("'orderId' cannot be null"));
            return;
        }
        if (orderId.valueId() == null || orderId.valueId().isBlank()){
            this.validatorHandler().append(new Error("'orderId' cannot not be empty"));
        }

        if (orderValue == null) {
            this.validatorHandler().append(new Error("'orderValue' cannot not be null"));
            return;
        }
        if (orderValue.doubleValue() < 0) {
            this.validatorHandler().append(new Error("'orderValue' cannot not be empty"));
        }
        if (channel == null) {
            this.validatorHandler().append(new Error("'channel' cannot not be null"));
            return;
        }
        if (channel.isBlank()) {
            this.validatorHandler().append(new Error("'channel' cannot not be empty"));
        }

        if (paymentStatus == null) {
            this.validatorHandler().append(new Error("'paymentStatus' should not be null"));
            return;
        }
        if (paymentStatus.isBlank()) {
            this.validatorHandler().append(new Error("'paymentStatus' should not be empty"));
        }

    }
}
