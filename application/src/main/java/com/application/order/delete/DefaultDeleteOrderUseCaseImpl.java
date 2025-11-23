package com.application.order.delete;

import com.domain.order.OrderGateway;
import com.domain.order.OrderID;

public class DefaultDeleteOrderUseCaseImpl extends DeleteOrderUseCase{

  private final OrderGateway orderGateway;

    public DefaultDeleteOrderUseCaseImpl(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }


    @Override
    public void execute(String id) {
        this.orderGateway.deleteOrderById(OrderID.from(id));

    }
}
