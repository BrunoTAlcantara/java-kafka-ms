package com.domain.order;

import com.domain.pagination.Pagination;
import com.domain.pagination.SearchQuery;

import java.util.Optional;

public interface OrderGateway {

    Order persist(Order order);
    Order update(Order order);
    Optional<Order> findById(OrderID id);
    void deleteOrderById(OrderID id);
    Pagination<Order> findAllOrders(SearchQuery query);
}
