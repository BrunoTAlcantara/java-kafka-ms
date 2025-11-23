package com.domain.pagination;

public record SearchQuery(int currentPage, int page, String terms, String sort, String direction) {
}
