package com.infrastructure.config.usecases;

import com.application.order.create.CreateOrderUseCase;
import com.application.order.create.DefaultCreateOrderUseCaseImpl;
import com.application.order.delete.DefaultDeleteOrderUseCaseImpl;
import com.application.order.delete.DeleteOrderUseCase;
import com.application.order.get.DefaultGetOrderByUseCaseImpl;
import com.application.order.get.GetOrderByUseCase;
import com.application.order.list.DefaultListOrderUseCaseImpl;
import com.application.order.list.ListOrderUseCase;
import com.application.order.update.DefaultUpdateOrderUseCaseImpl;
import com.application.order.update.UpdateOrderUseCase;
import com.domain.order.OrderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    private final OrderGateway orderGateway;

    public OrderUseCaseConfig(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase() {
        return new DefaultCreateOrderUseCaseImpl(orderGateway);
    }

    @Bean
    public UpdateOrderUseCase updateOrderUseCase() {
        return new DefaultUpdateOrderUseCaseImpl(orderGateway);
    }

    @Bean
    public GetOrderByUseCase getOrderByUseCase() {
        return new DefaultGetOrderByUseCaseImpl(orderGateway);
    }

    @Bean
    public ListOrderUseCase listOrderUseCase() {
        return new DefaultListOrderUseCaseImpl(orderGateway);
    }

    @Bean
    public DeleteOrderUseCase deleteOrderUseCase() {
        return new DefaultDeleteOrderUseCaseImpl(orderGateway);
    }

}
