package com.infrastructure.order;

import com.domain.order.Order;
import com.domain.order.OrderID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity(name = "Order")
@Table(name = "order_oms")
public class OrderEntity {

    @Id
    private String id;
    @Column(name = "order_value", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal orderValue;
    @Column(name = "channel",nullable = false)
    private String channel;
    @Column(name = "payment_status",nullable = false)
    private String paymentStatus;
    @Column(name = "created",nullable = false, columnDefinition = "DATETIME(6)")
    private Instant created;
    @Column(name = "updated",nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updated;

    public OrderEntity() {
    }


    private OrderEntity(final String id,
                        final BigDecimal orderValue,
                        final String channel,
                        final String paymentStatus,
                        final Instant created,
                        final Instant updated) {

        this.id = id;
        this.orderValue = orderValue;
        this.channel = channel;
        this.paymentStatus = paymentStatus;
        this.created = created;
        this.updated = updated;

    }

    public Order aggregation() {
        return Order.aggregate(OrderID.from(getId()),
                getOrderValue(),
                getChannel(),
                getPaymentStatus(),
                getCreated(),
                getUpdated());
    }

    public OrderEntity aggregation2() {
        return from(Order.aggregate(OrderID.from(getId()),
                getOrderValue(),
                getChannel(),
                getPaymentStatus(),
                getCreated(),
                getUpdated()));
    }

    public static OrderEntity from(final Order order) {
        return new OrderEntity(order.getId().valueId(),
                order.getOrderValue(),
                order.getChannel(),
                order.getPaymentStatus(),
                order.getCreated(),
                order.getUpdated());
    }


    public String getId() {
        return id;
    }

    public Instant getCreated() {
        return created;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Instant getUpdated() {
        return updated;
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public String getChannel() {
        return channel;
    }

    public void setId(String id) {
        this.id = id;
    }

}
