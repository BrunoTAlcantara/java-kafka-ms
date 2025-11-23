package com.infrastructure.order.api.controller;

import com.application.order.create.CreateOrderCommand;
import com.application.order.create.CreateOrderOutput;
import com.application.order.create.CreateOrderUseCase;
import com.application.order.delete.DeleteOrderUseCase;
import com.application.order.get.GetOrderByUseCase;
import com.application.order.list.ListOrderUseCase;
import com.application.order.update.UpdateOrderCommand;
import com.application.order.update.UpdateOrderOutPut;
import com.application.order.update.UpdateOrderUseCase;
import com.domain.pagination.Pagination;
import com.domain.pagination.SearchQuery;
import com.domain.validators.Error;
import com.infrastructure.order.api.OrderApi;
import com.infrastructure.order.models.CreateOrderRequest;
import com.infrastructure.order.models.OrderListResponse;
import com.infrastructure.order.models.OrderResponse;
import com.infrastructure.order.models.UpdateOrderRequest;
import com.infrastructure.order.presenters.OrderPresenters;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(value = "/orders")
public class OrderController implements OrderApi {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByUseCase getOrderByUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final ListOrderUseCase listOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderByUseCase getOrderByUseCase, UpdateOrderUseCase updateOrderUseCase, DeleteOrderUseCase deleteOrderUseCase, ListOrderUseCase listOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByUseCase = getOrderByUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.listOrderUseCase = listOrderUseCase;
    }

    @Override
    public ResponseEntity<?> createOrder(CreateOrderRequest input) {

        final var command = CreateOrderCommand.apply(input.orderValue()
                , input.channel(), input.paymentStatus());

        final Function<List<Error>, ResponseEntity<?>> error = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateOrderOutput, ResponseEntity<?>> success = output ->
                ResponseEntity.created(URI.create("/order/" + output.id())).body(output);
        var orderUseCase = createOrderUseCase.execute(command);

        return !CollectionUtils.isEmpty(orderUseCase.errors()) ?
                error.apply(orderUseCase.errors()) :
                success.apply(orderUseCase);
    }

    @Override
    public Pagination<OrderListResponse> listOrders(String search, int page, int perPage, String sort, String direction) {
        return listOrderUseCase.execute(new SearchQuery(
                page, perPage, search, sort, direction
        )).map(OrderPresenters::present);
    }

    @Override
    public OrderResponse getById(String id) {
        return OrderPresenters.present(getOrderByUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateOrderRequest input) {

        final var command = UpdateOrderCommand.apply(id, input.orderValue()
                , input.channel(), input.paymentStatus());

        final Function<List<Error>, ResponseEntity<?>> error = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateOrderOutPut, ResponseEntity<?>> success = output ->
                ResponseEntity.created(URI.create("/order/" + output.id())).body(output);
        var orderUpdateUseCase = updateOrderUseCase.execute(command);

        return !CollectionUtils.isEmpty(orderUpdateUseCase.errors()) ?
                error.apply(orderUpdateUseCase.errors()) :
                success.apply(orderUpdateUseCase);
    }

    @Override
    public void deleteById(String id) {
        deleteOrderUseCase.execute(id);
    }
}
