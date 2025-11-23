package com.application.order.list;

import com.application.UseCase;
import com.domain.pagination.Pagination;
import com.domain.pagination.SearchQuery;

public abstract class ListOrderUseCase extends UseCase<SearchQuery, Pagination<OrderListOutput>> {
}
