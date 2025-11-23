package com.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(int currentPage, int page, long total, List<T> items) {

    public <S> Pagination<S> map(final Function<T, S> mapper) {
        final List<S> newLists = this.items.stream().map(mapper).toList();
        return new Pagination<>(currentPage(), page(), total(), newLists);
    }
}
