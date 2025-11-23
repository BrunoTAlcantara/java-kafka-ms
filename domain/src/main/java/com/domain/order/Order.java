package com.domain.order;

import com.domain.Aggregation;
import com.domain.validators.ValidationHandlerDomain;

import java.math.BigDecimal;
import java.time.Instant;

public class Order extends Aggregation<OrderID> implements Cloneable {

    private BigDecimal orderValue;
    private String channel;
    private String paymentStatus;
    private Instant created;
    private Instant updated;

    protected Order(OrderID orderID, BigDecimal orderValue, String channel, String paymentStatus, Instant created, Instant updated) {
        super(orderID);
        this.orderValue = orderValue;
        this.channel = channel;
        this.paymentStatus = paymentStatus;
        this.created = created;
        this.updated = updated;
    }

    public static Order newOrder(BigDecimal orderValue, String channel, String paymentStatus) {
        final var id = OrderID.uniqueID();
        final var now = Instant.now();
        return new Order(id, orderValue, channel, paymentStatus, now, now);
    }

    public static Order aggregate(OrderID id, BigDecimal orderValue, String channel, String paymentStatus, Instant created, Instant updated) {
        return new Order(id, orderValue, channel, paymentStatus, created, updated);
    }

    public Order update(String orderId, BigDecimal orderValue, String channel, String paymentStatus) {
        this.orderValue = orderValue;
        this.channel = channel;
        this.paymentStatus = paymentStatus;
        this.updated = Instant.now();
        return this;
    }

    @Override
    public void validate(ValidationHandlerDomain handler) {
        new OrderValidator(this, handler).validate();
    }

    public String getChannel() {
        return channel;
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getUpdated() {
        return updated;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return null;
    }
}
