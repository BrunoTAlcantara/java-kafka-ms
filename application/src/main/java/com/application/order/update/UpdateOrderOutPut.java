package com.application.order.update;

import com.application.order.create.CreateOrderOutput;
import com.domain.order.Order;
import com.domain.validators.Error;

import java.util.List;

public record UpdateOrderOutPut(String id, List<Error> errors) {

    public static UpdateOrderOutPut applyOutPut(final String id, List<Error> errors) {
        return new UpdateOrderOutPut(id, null);
    }

    public static UpdateOrderOutPut applyOutPut(final Order order) {
        return new UpdateOrderOutPut(order.getId().valueId(), null);
    }

    public static UpdateOrderOutPut withErrors(final String orderId, List<Error> errors) {
        return new UpdateOrderOutPut(orderId, errors);
    }
}
