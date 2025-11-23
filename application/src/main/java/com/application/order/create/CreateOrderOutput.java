package com.application.order.create;

import com.domain.order.Order;
import com.domain.validators.Error;

import java.util.List;

public record CreateOrderOutput(String id, List<Error> errors) {

    public static CreateOrderOutput outPutFrom(final String id, List<Error> errors) {
        return new CreateOrderOutput(id, errors);
    }


    public static CreateOrderOutput outPutFrom(final Order order) {
        return new CreateOrderOutput(order.getId().valueId(), null);
    }

    public static CreateOrderOutput withErrors(List<Error> errors) {
        return new CreateOrderOutput(null, errors);
    }


}
