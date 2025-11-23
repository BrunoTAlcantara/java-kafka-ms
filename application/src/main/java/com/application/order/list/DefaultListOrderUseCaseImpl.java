package com.application.order.list;

import com.domain.order.OrderGateway;
import com.domain.pagination.Pagination;
import com.domain.pagination.SearchQuery;

public class DefaultListOrderUseCaseImpl extends ListOrderUseCase {

    private final OrderGateway orderGateway;

    public DefaultListOrderUseCaseImpl(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public Pagination<OrderListOutput> execute(SearchQuery searchQuery) {
        return this.orderGateway.findAllOrders(searchQuery)
                .map(OrderListOutput::from);

    }
}
