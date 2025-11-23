package com.infrastructure.order;

import com.domain.order.Order;
import com.domain.order.OrderGateway;
import com.domain.order.OrderID;
import com.domain.pagination.Pagination;
import com.domain.pagination.SearchQuery;
import com.infrastructure.order.persistence.OrderJPARepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


import java.util.Optional;
import static com.infrastructure.utils.InfraStructureUtils.like;
import static com.infrastructure.utils.InfraStructureUtils.createAtToday;

@Component
public class OrderSQLGateway implements OrderGateway {

    private final OrderJPARepository orderJPARepository;

    public OrderSQLGateway(final OrderJPARepository orderJPARepository) {
        this.orderJPARepository = orderJPARepository;
    }

    @Override
    public Order persist(Order order) {
        return this.orderJPARepository.save(OrderEntity.from(order)).aggregation();
    }

    @Override
    public Order update(Order order) {
        return persist(order);
    }

    @Override
    public Optional<Order> findById(OrderID id) {
        return this.orderJPARepository.findById(id.valueId())
                .map(OrderEntity::aggregation);
    }

    @Override
    public void deleteOrderById(OrderID id) {
        final String orderId = id.valueId();
        if (this.orderJPARepository.existsById(orderId)) {
            this.orderJPARepository.deleteById(orderId);
        }
    }

    @Override
    public Pagination<Order> findAllOrders(SearchQuery query) {
        final var pages = PageRequest.of(query.currentPage(), query.page(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort()));

        final var specification = Optional.ofNullable(query.terms())
                .filter(s -> !s.isBlank())
                .map(this::filterAndExecute)
                .orElse(null);

        final var todaySpecification = createAtToday();
        final var combinedSpecification = Specification.where(specification).and(todaySpecification);
        final var pageResult = this.orderJPARepository
                .findAll(Specification.where(combinedSpecification), pages);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(OrderEntity::aggregation).toList()
        );
    }

    private Specification<OrderEntity> filterAndExecute(String query) {
        final Specification<OrderEntity> orderId = like("id", query);
        final Specification<OrderEntity> channel = like("channel", query);
        return orderId.or(channel);
    }
}
