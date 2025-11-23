package com.infrastructure.order.persistence;

import com.infrastructure.order.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJPARepository extends JpaRepository<OrderEntity, String> {
    Page<OrderEntity> findAll(Specification<OrderEntity> where, Pageable pageable);
}
