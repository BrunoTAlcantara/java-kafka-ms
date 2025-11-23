package com.domain;


public abstract class Aggregation<ID extends UniqueIdentifier> extends Entity<ID> {
    protected Aggregation(final ID id) {
        super(id);
    }
}
